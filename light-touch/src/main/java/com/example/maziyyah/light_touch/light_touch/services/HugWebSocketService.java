package com.example.maziyyah.light_touch.light_touch.services;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;



@Service
public class HugWebSocketService {

    private static final Logger logger = LoggerFactory.getLogger(HugWebSocketService.class);

    private final SimpMessagingTemplate messagingTemplate;
    private final HugEventService hugEventService;

    public HugWebSocketService(SimpMessagingTemplate messagingTemplate, HugEventService hugEventService) {
        this.messagingTemplate = messagingTemplate;
        this.hugEventService = hugEventService;
    }

    public void triggerHugUpdate(String pairingId) {
        // get the current UTC date
        LocalDate utcToday = LocalDate.now(ZoneOffset.UTC);

        // get count from Redis or DB (fallback)
        Integer hugCount = getHugCount(pairingId, utcToday);

        // create update message
        Map<String, Object> message = Map.of(
            "type", "HUG_UPDATE",
            "pairingId", pairingId,
            "count", hugCount,
            "timestamp", Instant.now().toString()
        );

        // send hug count update to the correct pair
        messagingTemplate.convertAndSend("/topic/hugs/" + pairingId, message);
        logger.info("📡 WebSocket update sent! Pairing ID: {}, Hugs Today: {}, Message: {}", pairingId, hugCount, message);
    }


    // private String generatePairingId(String deviceId, String pairedDeviceId) {
    //     return deviceId.compareTo(pairedDeviceId) < 0 ? deviceId + "_" + pairedDeviceId : pairedDeviceId + "_" + deviceId;
    // }

    private Integer getHugCount(String pairingId, LocalDate date) {
        // try redis first
        Integer redisHugCount = hugEventService.getRedisHugCountForPairingAndDate(pairingId, date);
        if (redisHugCount != 0) {
            return redisHugCount;
        }

        // fallback to database count
        Integer sqlHugCount = hugEventService.getSQlHugCountForPairingAndDate(pairingId, date);
        return sqlHugCount;
    }

    
    
}
