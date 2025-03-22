package com.example.maziyyah.light_touch.light_touch.services;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.example.maziyyah.light_touch.light_touch.exceptions.HugEvents.ErrorRetrievingHugCount;
import com.example.maziyyah.light_touch.light_touch.models.HugEvent;
import com.example.maziyyah.light_touch.light_touch.repositories.HugEventRepository;

@Service
public class HugEventService {

    private static final Logger logger = LoggerFactory.getLogger(HugEventService.class);

    private final HugEventRepository hugEventRepository;

    public HugEventService(HugEventRepository hugEventRepository) {
        this.hugEventRepository = hugEventRepository;
    }

    public void logSpontaneousHug(String deviceId) {
        hugEventRepository.logSpontaneousHug(deviceId);
    }

    public Boolean checkIfPairedDeviceSentHug(String pairedDeviceId) {
        return hugEventRepository.checkIfPairedDeviceSentHug(pairedDeviceId);
    }

    public void storeHugEvent(String pairingId) {
        // create a new Hug Event record

        Instant timestamp = Instant.now();
        HugEvent hugEvent = new HugEvent(pairingId, timestamp);
        hugEventRepository.saveHugEvent(hugEvent);

    }

    public Integer getRedisHugCountForPairingAndDate (String pairingId, LocalDate date) {
        try {
            Object redisHugCountObj = hugEventRepository.getRedisHugCount(pairingId, date);
            // handle null case
            if (redisHugCountObj == null) {
                return 0;
            }
            Integer redisHugCount = Integer.parseInt( (String) redisHugCountObj);
            return redisHugCount;
        } catch (Exception ex) {
            logger.error("Error retrieving hug count from Redis for pairingId: {} and date: {}", pairingId, date, ex);
            throw new ErrorRetrievingHugCount("Failed to retrieve hug count from Redis.");
        }
        
    }

    public Integer getSQlHugCountForPairingAndDate(String pairingId, LocalDate date) {
        // convert date to the start of the day in UTC
        Instant startOfDay = date.atStartOfDay(ZoneOffset.UTC).toInstant();

        // convert the next day's start of the day in UTC
        Instant startOfNextDay = date.plusDays(1).atStartOfDay(ZoneOffset.UTC).toInstant();
        Integer sqlHugCount = hugEventRepository.getSQLHugCount(pairingId, startOfDay, startOfNextDay);

        return sqlHugCount;
    }
    
    
}
