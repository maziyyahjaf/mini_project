package com.example.maziyyah.light_touch.light_touch.controllers;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.maziyyah.light_touch.light_touch.models.EmotionLogDTO;
import com.example.maziyyah.light_touch.light_touch.models.EmotionInsights.EmotionWeeklyPatternDTO;
import com.example.maziyyah.light_touch.light_touch.services.EmotionInsightsService;

import io.jsonwebtoken.lang.Collections;
import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonNumber;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;
import jakarta.json.JsonValue;

@RestController
@RequestMapping("/api/insights")
public class EmotionInsightsController {

    private static final Logger logger = LoggerFactory.getLogger(EmotionInsightsController.class);
    
    @Autowired
    private EmotionInsightsService emotionInsightsService;

    @GetMapping(path = "/weekly")
    public ResponseEntity<?> getWeeklyPatternWithLogIDs() {

        // Retrieve the authenticated user's Firebase UID
         Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
         String firebaseUid = (String) authentication.getPrincipal();
         logger.info("Authenticated user UID: {}", firebaseUid);
         logger.info("Authenticated User in weekly pattern: {}", authentication.getPrincipal());
         logger.info("User Authorities in weekly pattern: {}", authentication.getAuthorities());

        // Optional<List<EmotionWeeklyPatternDTO>> weeklyPatternsOpt = emotionInsightsService.getWeeklyPatternWithLogIDs(firebaseUid);
        Optional<List<EmotionWeeklyPatternDTO>> weeklyPatternsOpt = emotionInsightsService.getWeeklyPatternWithLogIDsCompleteWeek(firebaseUid);
        if (weeklyPatternsOpt.isPresent() && !weeklyPatternsOpt.get().isEmpty()) {
            return ResponseEntity.ok().body(weeklyPatternsOpt.get());
        } else {
            // Return an empty list with 200 OK
            return ResponseEntity.ok(Collections.emptyList());
        }

    }

    @PostMapping(path = "logs-by-ids") 
    public ResponseEntity<?> getWeeklyLogsByIds(@RequestBody String logIds) {
        // Retrieve the authenticated user's Firebase UID
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String firebaseUid = (String) authentication.getPrincipal();
        logger.info("Authenticated user UID: {}", firebaseUid);
        logger.info("Authenticated User in weekly logs: {}", authentication.getPrincipal());
        logger.info("User Authorities in weekly logs by ID: {}", authentication.getAuthorities());

        // parse the payload
        JsonReader jsonReader = Json.createReader(new StringReader(logIds));
        JsonObject root = jsonReader.readObject();
        JsonArray logIdsArray = root.getJsonArray("logIds");

        List<Integer> logIdList = new ArrayList<>();

        for (JsonValue value: logIdsArray) {
            if (value instanceof JsonNumber) {
                JsonNumber jsonNumber = (JsonNumber) value;
                logIdList.add(jsonNumber.intValue());
            }
        }

        // call the service to get the emotion logs
        Optional<List<EmotionLogDTO>> emotionLogsOpt = emotionInsightsService.getListOfEmotionLogsByIds(logIdList, firebaseUid);

        if (emotionLogsOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        List<EmotionLogDTO> emotionLogs = emotionLogsOpt.get();
        JsonArrayBuilder jab = Json.createArrayBuilder();
        for (EmotionLogDTO dto: emotionLogs) {
            JsonObject jsonDTO = dto.toJSON();
            jab.add(jsonDTO);
        }

        JsonArray jsonArray = jab.build();
        return ResponseEntity.ok().body(jsonArray.toString());
        
    }

    @GetMapping(path = "daily")
    public ResponseEntity<?> getEmotionLogsByDate(@RequestParam("date") String dateString) {
        
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String firebaseUid = (String) authentication.getPrincipal();
        logger.info("Authenticated user UID: {}", firebaseUid);
        logger.info("Authenticated User in get emotion logs by date: {}", authentication.getPrincipal());
        logger.info("User Authorities in get emotion logs by date: {}", authentication.getAuthorities());

        // call the service to get the emotion logs by date
        Optional<List<EmotionLogDTO>> emotionLogsOpt = emotionInsightsService.getListOfEmotionLogsByDate(dateString, firebaseUid);
        
        if (emotionLogsOpt.isEmpty()) {
            return ResponseEntity.notFound().build();

        }
        List<EmotionLogDTO> emotionLogs = emotionLogsOpt.get();
        JsonArrayBuilder jab = Json.createArrayBuilder();
        for (EmotionLogDTO dto: emotionLogs) {
            JsonObject jsonDTO = dto.toJSON();
            jab.add(jsonDTO);
        }
        
        JsonArray jsonArray = jab.build();
        return ResponseEntity.ok().body(jsonArray.toString());
    }

    @GetMapping(path = "today/latest")
    public ResponseEntity<?> getLatestEmotionLogForToday(@RequestParam("date") String dateString) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String firebaseUid = (String) authentication.getPrincipal();
        logger.info("Authenticated user UID: {}", firebaseUid);
        logger.info("Authenticated User in get latest emotion log: {}", authentication.getPrincipal());
        logger.info("User Authorities in get latest emotion log: {}", authentication.getAuthorities());

        // call the service to get latest log for the day
        Optional<EmotionLogDTO> latestLogOpt = emotionInsightsService.getLatestEmotionLogForToday(dateString, firebaseUid);
        if (latestLogOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
            // show a message?>
        }

        EmotionLogDTO latestLog = latestLogOpt.get();
        JsonObject lastestLogJSON = latestLog.toJSON();
        return ResponseEntity.ok().body(lastestLogJSON.toString());

    }

    
}
