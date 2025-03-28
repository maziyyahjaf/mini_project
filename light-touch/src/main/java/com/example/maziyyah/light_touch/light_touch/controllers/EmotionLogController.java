package com.example.maziyyah.light_touch.light_touch.controllers;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.core.Authentication;

import com.example.maziyyah.light_touch.light_touch.models.ApiResponse;
import com.example.maziyyah.light_touch.light_touch.models.Emotion;
import com.example.maziyyah.light_touch.light_touch.models.EmotionLog;
import com.example.maziyyah.light_touch.light_touch.models.EmotionLogDTO;
import com.example.maziyyah.light_touch.light_touch.services.EmotionLogService;
import com.example.maziyyah.light_touch.light_touch.utils.Utils;


import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonObject;

@RestController
@RequestMapping("/api/emotions")
public class EmotionLogController {

    private static final Logger logger = LoggerFactory.getLogger(EmotionLogController.class);
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

    // API to get emotion list
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getEmotions() {
        JsonArray result = null;
        Optional<List<Emotion>> emotionsOpt = emotionLogService.getAllEmotions();
        JsonArrayBuilder jab = Json.createArrayBuilder();
        
        if(emotionsOpt.isEmpty()) {
            logger.error("Empty emotion list?");
        }

        emotionsOpt.get().forEach(emotion -> {
            jab.add(emotion.toJsonObject());
        });

        result = jab.build();

        return ResponseEntity.status(HttpStatus.OK)
                            .contentType(MediaType.APPLICATION_JSON)
                            .body(result.toString());


    }


    // API to log new emotion
    // do you want it to be EmotionLog? or parse it from rawJsonString to emotion log??
    @PostMapping(path="/log")
    public ResponseEntity<?> logEmotion(@RequestBody String payload) {
         
        // Retrieve the authenticated user's Firebase UID
         Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
         String firebaseUid = (String) authentication.getPrincipal();
         logger.info("Authenticated user UID: {}", firebaseUid);
         logger.info("Authenticated User in logEmotion: {}", authentication.getPrincipal());
         logger.info("User Authorities in logEmotion: {}", authentication.getAuthorities());

         EmotionLog emotionLog = Utils.toEmotionLog(payload, firebaseUid);

        try {
            emotionLogService.saveAndPublishLogToDevice(emotionLog);
            
            return ResponseEntity.accepted().body(new ApiResponse("success", "Emotion log processing started."));

        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ApiResponse("fail", "Failed to process emotion log: " + ex.getMessage()));
        }

    }
    
    // okay now 
    // this is for saving the updated log
    @PutMapping(path = "/log/{logId}")
    public ResponseEntity<?> saveUpdatedLog(@PathVariable("logId") String logId, @RequestBody String payload) {
        // Retrieve the authenticated user's Firebase UID
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String firebaseUid = (String) authentication.getPrincipal();
        logger.info("Authenticated user UID: {}", firebaseUid);
        logger.info("Authenticated User in update logEmotion: {}", authentication.getPrincipal());
        logger.info("User Authorities in updated logEmotion: {}", authentication.getAuthorities());

        EmotionLog emotionLog = Utils.toUpdatedEmotionLog(payload);

        try {
            emotionLogService.saveUpdatedLogAndPublishToDevice(emotionLog);

            EmotionLogDTO dto = new EmotionLogDTO();
            dto.setEmotionLogId(emotionLog.getEmotionLogId());
            dto.setEmotion(emotionLog.getEmotion());
            dto.setIntensity(emotionLog.getIntensity());
            // need to get timezone for user.. not changing the time
            String timezone = emotionLogService.fetchUserTimezone(firebaseUid);
            Instant instant = Instant.parse(emotionLog.getTimestamp());
            LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, ZoneId.of(timezone));
            dto.setTimestamp(localDateTime);
            dto.setSendToDevice(emotionLog.isSendToDevice());
            dto.setNotes(emotionLog.getNotes());
            JsonObject dtoJson = dto.toJSON();

            return ResponseEntity.accepted().body(dtoJson.toString());


        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ApiResponse("fail", "Failed to process updated emotion log: " + ex.getMessage()));
        }

    }

    @DeleteMapping(path = "/log/{logId}")
    public ResponseEntity<?> deleteLog(@PathVariable("logId") int logId) {
        // Retrieve the authenticated user's Firebase UID
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String firebaseUid = (String) authentication.getPrincipal();
        logger.info("Authenticated user UID: {}", firebaseUid);
        logger.info("Authenticated User in delete logEmotion: {}", authentication.getPrincipal());
        logger.info("User Authorities in delete logEmotion: {}", authentication.getAuthorities());

        try{
            emotionLogService.deleteLog(firebaseUid, logId);

            return ResponseEntity.ok().body(new ApiResponse("success", String.format("deleted log %s ", logId)));

        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ApiResponse("fail", "Failed to delete emotion log: " + ex.getMessage()));
        }
    }
    


}
