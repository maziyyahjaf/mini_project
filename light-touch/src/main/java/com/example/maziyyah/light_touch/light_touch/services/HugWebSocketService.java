package com.example.maziyyah.light_touch.light_touch.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class HugWebSocketService {

    private static final Logger logger = LoggerFactory.getLogger(HugWebSocketService.class);

    private final SimpMessagingTemplate messagingTemplate;

    public HugWebSocketService(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    public void triggerHugUpdate(String deviceId, String pairedDeviceId) {
        // send hug count update to the correct pair
        double hugCount = Math.random();
        // hugCount++;
        String pairId = generatePairingId(deviceId, pairedDeviceId);
        messagingTemplate.convertAndSend("/topic/pair/" + pairId + "/hugs", hugCount);

        logger.info("📡 WebSocket update sent! Pair ID: {}, Hugs Today: {}", pairId, hugCount);
    }


    private String generatePairingId(String deviceId, String pairedDeviceId) {
        return deviceId.compareTo(pairedDeviceId) < 0 ? deviceId + "_" + pairedDeviceId : pairedDeviceId + "_" + deviceId;
    }

    
    
}
