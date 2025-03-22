package com.example.maziyyah.light_touch.light_touch.services;

import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
}
