package com.example.maziyyah.light_touch.light_touch.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.maziyyah.light_touch.light_touch.models.EmotionLog;
import com.example.maziyyah.light_touch.light_touch.services.EmotionLogService;

@RestController
@RequestMapping("/api/emotions")
public class EmotionLogController {


    // private final MQTTService mqttService;
    // private final EmotionLogRepository repository;

    private final EmotionLogService emotionLogService;

      // my goal now is to simulate sending emotion to the iot device using postman 
    // and to check if ithat works
    // @PostMapping("")
    // public ResponseEntity<?> sendingUserEmotion(@RequestBody String rawJsonString) {
        
    // }

    public EmotionLogController(EmotionLogService emotionLogService) {
        this.emotionLogService = emotionLogService;
    }

    // API to log new emotion
    // do you want it to be EmotionLog? or parse it from rawJsonString to emotion log??
    @PostMapping
    public ResponseEntity<String> logEmotion(@RequestBody EmotionLog log) {
        try {
            emotionLogService.saveAndPublishLogToDevice(log);
            return ResponseEntity.accepted().body("Emotion log processing started.");

        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Failed to process emotion log: " + ex.getMessage());
        }

    }
    
    // okay now 
    
}
