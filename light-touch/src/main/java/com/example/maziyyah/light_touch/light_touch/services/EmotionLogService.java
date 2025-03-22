package com.example.maziyyah.light_touch.light_touch.services;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.example.maziyyah.light_touch.light_touch.models.Emotion;
import com.example.maziyyah.light_touch.light_touch.models.EmotionLog;
import com.example.maziyyah.light_touch.light_touch.repositories.EmotionLogRepository;
import com.example.maziyyah.light_touch.light_touch.repositories.UserRepository;

@Service
public class EmotionLogService {

    private static final Logger logger = LoggerFactory.getLogger(EmotionLogService.class);

    private final MQTTService mqttService;
    private final EmotionLogRepository emotionLogrepository;
    private final UserRepository userRepository;


    public EmotionLogService(MQTTService mqttService, EmotionLogRepository emotionLogrepository, UserRepository userRepository) {
        this.mqttService = mqttService;
        this.emotionLogrepository = emotionLogrepository;
        this.userRepository = userRepository;

    }

    // get the user device id
    // if sent to device is true => publish message
    public void saveAndPublishLogToDevice(EmotionLog log) {
        // Save to database asynchronously
        try {
            CompletableFuture.runAsync(()-> saveEmotionLog(log));
        } catch (Exception ex) {
            logger.error("Failed to save emotion log asynchronously", ex);

        }
        
        // Publish to device immediately
        if (log.isSendToDevice()) {
            // maybe get the device id from redis instead?
            // for every user id we save the userid -> device id
            // can get deviceId from the client side
            // use device id to get the firebase uid?
            String deviceId = log.getDeviceId();
            if (deviceId.isEmpty()) {
                deviceId = emotionLogrepository.fetchDeviceIdBasedOnFirebaseUid(log.getFirebaseUid());
            }

            mqttService.publishMessage(deviceId, log.getEmotion(), log.getIntensity());
        }


    }


    private void saveEmotionLog(EmotionLog log) {
        emotionLogrepository.saveEmotionLog(log);
    }

    public Optional<List<Emotion>> getAllEmotions() {
        List<Emotion> emotions = emotionLogrepository.getAllEmotions();
        if (emotions != null && !emotions.isEmpty()) {
            return Optional.of(emotions);
        }
        return Optional.empty();
    }



    



    
}
