package com.example.maziyyah.light_touch.light_touch.services;

import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.maziyyah.light_touch.light_touch.models.EmotionLogDTO;
import com.example.maziyyah.light_touch.light_touch.models.EmotionInsights.EmotionWeeklyPatternDTO;
import com.example.maziyyah.light_touch.light_touch.repositories.EmotionInsightsRepository;

@Service
public class EmotionInsightsService {
    
    @Autowired
    private EmotionInsightsRepository emotionInsightsRepository;


    public String getCurrentOffsetForUser(String firebaseUid) {
        // need to get the time zone for the firebase uid
        String timezone = emotionInsightsRepository.getUserTimeZone(firebaseUid); // Asia/Singapore
        ZoneId zoneId = ZoneId.of(timezone);
        ZoneOffset currentOffset = ZonedDateTime.now(zoneId).getOffset();
        
        return currentOffset.getId().replace("Z", "+00:00");

    }

    public Optional<List<EmotionWeeklyPatternDTO>> getWeeklyPatternWithLogIDsCompleteWeek(String firebaseUid) {
        String timezoneOffset = getCurrentOffsetForUser(firebaseUid);
        // int numOfDays = 7; // do i make this dynamic??
        return emotionInsightsRepository.getWeeklyPatternWithLogIDsCompleteWeek(timezoneOffset, firebaseUid);
    }

    public Optional<List<EmotionWeeklyPatternDTO>> getWeeklyPatternWithLogIDs(String firebaseUid) {
        String timezoneOffset = getCurrentOffsetForUser(firebaseUid);
        int numOfDays = 7; // do i make this dynamic??
        return emotionInsightsRepository.getWeeklyPatternWithLogIDs(timezoneOffset, firebaseUid, numOfDays);
    }

    public Optional<EmotionLogDTO> getEmotionLogById(String firebaseUid, String logId) {
        String timezoneOffset = getCurrentOffsetForUser(firebaseUid);
        return emotionInsightsRepository.getEmotionLogById(timezoneOffset, logId, firebaseUid);
    }

    public Optional<List<EmotionLogDTO>> getListOfEmotionLogsByIds(List<Integer> logIdList, String firebaseUid) {
        String timezoneOffset = getCurrentOffsetForUser(firebaseUid);
        return emotionInsightsRepository.getListOfEmotionLogsByIds(timezoneOffset, logIdList, firebaseUid);

    }
}
