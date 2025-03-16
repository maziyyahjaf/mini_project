package com.example.maziyyah.light_touch.light_touch.configs;

// import org.slf4j.Logger;
// import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.hivemq.client.mqtt.MqttClient;
import com.hivemq.client.mqtt.mqtt3.Mqtt3AsyncClient;
// import com.hivemq.client.mqtt.mqtt5.Mqtt5Client;

// import static java.nio.charset.StandardCharsets.UTF_8;

// import java.nio.charset.StandardCharsets;

@Configuration
public class MQTTConfig {

    // private static final Logger logger = LoggerFactory.getLogger(MQTTConfig.class);

    @Value("${hivemq.host}")
    private String mqttHost;

    @Value("${hivemq.port}")
    private Integer mqttPort;

    @Value("${hive.mq.username}")
    private String mqttUsername;

    @Value("${hive.mq.password}")
    private String mqttPassword;

    private static final String CLIENT_ID = "springboot-hivemq-client";
    
    // create a MQTT client -> change to use 3.1.1??
    @Bean
    public Mqtt3AsyncClient createMqtt3Client() {
       Mqtt3AsyncClient client =MqttClient.builder()
                                            .useMqttVersion3()
                                            .serverHost(mqttHost)
                                            .serverPort(mqttPort)
                                            .identifier(CLIENT_ID)
                                            .sslWithDefaultConfig()
                                            .buildAsync();
        // client.connectWith()
        //         .simpleAuth()
        //         .username(mqttUsername)
        //         .password(StandardCharsets.UTF_8.encode(mqttPassword))
        //         .applySimpleAuth()
        //         .send()
        //         .whenComplete((connAck, throwable) -> {
        //             if (throwable != null) {
        //                 logger.error("Failed to connect: {}", throwable.getMessage());
        //             } else {
        //                 logger.info("Successfully connected to HiveMQ.");
        //             }
        //         });

        return client;
      
    }

    // // connect to HiveMQ cloud with TLS and username/pw?
    // @Bean
    // public void connectToHiveMQCloud(Mqtt3AsyncClient client) {
    //     client.connectWith()
    //             .simpleAuth()
    //             .username(mqttUsername)
    //             .password(UTF_8.encode(mqttPassword))
    //             .applySimpleAuth()
    //             .send();
        
    //     logger.info("Connected successfully");
    // }


}
