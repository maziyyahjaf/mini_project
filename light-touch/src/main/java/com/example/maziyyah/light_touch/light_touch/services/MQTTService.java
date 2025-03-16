package com.example.maziyyah.light_touch.light_touch.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.example.maziyyah.light_touch.light_touch.repositories.MQTTRepository;
import com.hivemq.client.mqtt.datatypes.MqttQos;
// import com.hivemq.client.mqtt.datatypes.MqttTopic;
// import com.hivemq.client.mqtt.MqttClient;
import com.hivemq.client.mqtt.mqtt3.Mqtt3AsyncClient;
import com.hivemq.client.mqtt.mqtt3.message.publish.Mqtt3Publish;

import jakarta.annotation.PostConstruct;

// import jakarta.annotation.PostConstruct;
import static java.nio.charset.StandardCharsets.UTF_8;

import java.nio.charset.StandardCharsets;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

// mqtt service will call the mongo repo?
// save number of times of simultaneous hugs?
// what about missed notifications??


@Service
public class MQTTService {
    
    @Value("${hive.mq.username}")
    private String mqttUsername;

    @Value("${hive.mq.password}")
    private String mqttPassword;

    private static final Logger logger = LoggerFactory.getLogger(MQTTService.class);
    private static final String BASE_TOPIC = "toys/";


    private final Mqtt3AsyncClient client;
    private final HugEventService hugEventService;
    private final TelegramNotificationService telegramNotificationService;
    private final MQTTRepository mqttRepository;
    private final HugWebSocketService hugWebSocketService;

    public MQTTService(Mqtt3AsyncClient client, 
                        HugEventService hugEventService, 
                        TelegramNotificationService telegramNotificationService, 
                        MQTTRepository mqttRepository,
                        HugWebSocketService hugWebSocketService) {
        this.client = client;
        this.hugEventService = hugEventService;
        this.telegramNotificationService = telegramNotificationService;
        this.mqttRepository = mqttRepository;
        this.hugWebSocketService = hugWebSocketService;
    }

    @PostConstruct
    public void connectAndSubscribe() {
        // logger.info("mqqtusername: {} ", mqttUsername);
        // logger.info("mqqtpassword: {} ", mqttPassword);
        logger.info("mqqtusername1: {} ", mqttUsername);
        logger.info("mqqtpassword1: {} ", mqttPassword);
        if (mqttUsername == null || mqttPassword == null) {
            logger.error("MQTT Username or Password is NULL. Check application.properties.");
            return;
        }

        client.connectWith()
                .simpleAuth()
                .username(mqttUsername) 
                .password(UTF_8.encode(mqttPassword))
                .applySimpleAuth()
                .send()
                .whenComplete((ack, throwable) -> {
                    if (throwable != null) {
                        logger.error("Failed to connect: " + throwable.getMessage());
                    } else {
                        logger.info("Connected successfully");
                        client.subscribeWith()
                            .topicFilter("toys/#")
                            .qos(MqttQos.AT_LEAST_ONCE)
                            .callback(publish -> {
                                String topic = publish.getTopic().toString();
                                String receivedMessage = new String(publish.getPayloadAsBytes(), StandardCharsets.UTF_8);
                                logger.info("Received on {}: {}", publish.getTopic(), receivedMessage);

                                if (topic.contains("/spontaneous_hug")) {
                                    logger.info("Triggering detectSimultaneousHug...");
                                    detectSimultaneousHug(topic, receivedMessage);
                                }

                                if (topic.contains("/notifications")) {
                                    logger.info("Triggering missedNotifications");
                                    // first processed the message, then get the telegram chat id, then sennd the missed
                                    // notification updates via telegram
                                    missedNotifications(receivedMessage);
                                }

                            })
                            .send()
                            .whenComplete((subAck, subThrowable) -> {
                                if (subThrowable != null) {
                                    logger.error("Failed to subscribe: {}", subThrowable.getMessage());
                                } else {
                                    logger.info("Succesfully subscribed to wildcard topic: toys/#");
                                }
                            });

                    }
                });

        
    }

    public void publishMessage(String deviceId, String message) {
        String topic = BASE_TOPIC + deviceId + "/mood";
        CompletableFuture<Mqtt3Publish> future = client.publishWith()
                                                .topic(topic)
                                                .payload(message.getBytes(UTF_8))
                                                .qos(MqttQos.AT_LEAST_ONCE)
                                                .send();

        future.whenComplete((ack, throwable) -> {
            if(throwable != null) {
                logger.info("Publish failed: {} ", throwable.getMessage());
            } else {
                logger.info("Published to {}: {}", topic, message);
            }
        });
    }

    public void detectSimultaneousHug(String topic, String receivedMessage) {
        String deviceID = extractDeviceId(topic);

        Optional<String> pairedDeviceOpt = mqttRepository.getPairedDeviceForDeviceId(deviceID);
        if (pairedDeviceOpt.isEmpty()) {
            logger.error("Paired Device Id not found for device id {}", deviceID);
        }

        String pairedDeviceId = pairedDeviceOpt.get();

        hugEventService.logSpontaneousHug(deviceID);

        if (hugEventService.checkIfPairedDeviceSentHug(pairedDeviceId)) {
            logger.info("❤️ Simultaneous hug detected between {} and {}!", deviceID, pairedDeviceId);
            publishSimultaneousHugMessage("simultaneous_hug", "true"); // Notify both devices
            // save event to mongo? save how many simultaneous hugs detected? -> maybe use web socket to update dashboard?
            hugWebSocketService.triggerHugUpdate(deviceID, pairedDeviceId);

        }

    }

    public void publishSimultaneousHugMessage(String topic, String message ) {
        String fullTopic = BASE_TOPIC + topic;
        CompletableFuture<Mqtt3Publish> future = client.publishWith()
                                                .topic(fullTopic)
                                                .payload(message.getBytes(UTF_8))
                                                .qos(MqttQos.AT_LEAST_ONCE)
                                                .send();

        future.whenComplete((ack, throwable) -> {
            if(throwable != null) {
                logger.info("Publish failed: {} ", throwable.getMessage());
            } else {
                logger.info("Published to {}: {}", fullTopic, message);
            }
        });
    }

    private String extractDeviceId(String topic) {
        String[] parts = topic.split("/");
        return parts.length > 1 ? parts[1] : "unknown_device";
    }


    public void missedNotifications(String receivedMessage) {
        logger.info("received message: {}", receivedMessage);
        String deviceId = receivedMessage;
     
        // check redis for the telegram chat id
        // Get the mapped Telegram chat ID
        Optional<String> telegramChatIdOpt = mqttRepository.getTelegramChatIdForDeviceId(deviceId);
        if (telegramChatIdOpt.isEmpty()) {
            logger.warn("No linked telegram chat id for device id {}", deviceId);
            return;
        }
        String telegramChatId = telegramChatIdOpt.get(); 

        // get the partners name..
        Optional<String> pairedDeviceIdOpt = mqttRepository.getPairedDeviceForDeviceId(deviceId);
        if (pairedDeviceIdOpt.isEmpty()) {
            logger.warn("No paired device found for device ID {}", deviceId);
            return; // Exit method early
        }
        String pairedDeviceId = pairedDeviceIdOpt.get();

        // Get partner name
        Optional<String> partnerNameOpt = Optional.ofNullable(mqttRepository.getName(pairedDeviceId));
        if (partnerNameOpt.isEmpty()) {
            logger.warn("No name found for paired device ID {}", pairedDeviceId);
            return; // Exit method early
        }

        String partnerName = partnerNameOpt.get();
        String message = String.format("You missed a notification from %s!", partnerName); // maybe dynamic??

        telegramNotificationService.sendTelegramNotificationWithButton(telegramChatId, message);
    }


    public void publishTelegramHug(String topic, String message ) {
        String fullTopic = BASE_TOPIC + topic;
        logger.info("full topic from telegram hug: {}", fullTopic);
        CompletableFuture<Mqtt3Publish> future = client.publishWith()
                                                .topic(fullTopic)
                                                .payload(message.getBytes(UTF_8))
                                                .qos(MqttQos.AT_LEAST_ONCE)
                                                .send();

        future.whenComplete((ack, throwable) -> {
            if(throwable != null) {
                logger.info("Publish failed: {} ", throwable.getMessage());
            } else {
                logger.info("Published to {}: {}", fullTopic, message);
            }
        });
    }



    

  

   

}
