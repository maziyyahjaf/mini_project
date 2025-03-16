package com.example.maziyyah.light_touch.light_touch.services;

import java.util.concurrent.CompletableFuture;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

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
        if (log.isSentToDevice()) {
            // maybe get the device id from redis instead?
            // for every user id we save the userid -> device id
            String deviceId = getDeviceIdForUserId(log);
            mqttService.publishMessage(deviceId, log.getEmotion());
        }


    }

    public String getDeviceIdForUserId(EmotionLog log) {
        String userId = log.getUserId();
        return userRepository.getDeviceIdForUserId(userId);
    }

    private void saveEmotionLog(EmotionLog log) {
        emotionLogrepository.saveEmotionLog(log);
    }



    



    
}
