package com.example.maziyyah.light_touch.light_touch.controllers;

import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.maziyyah.light_touch.light_touch.services.HugEventService;

@RestController
@RequestMapping("/api")
public class HugEventController {

    private static final Logger logger = LoggerFactory.getLogger(HugEventController.class);

    private final HugEventService hugEventService;
    
    public HugEventController(HugEventService hugEventService) {
        this.hugEventService = hugEventService;
    }


    @GetMapping("/hugs/count/{pairingId}")
    public ResponseEntity<?> getDailyHugCount(@PathVariable("pairingId") String pairingId) {

        // Retrieve the authenticated user's Firebase UID
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String firebaseUid = (String) authentication.getPrincipal();
        logger.info("Authenticated user UID: {}", firebaseUid);
        logger.info("Authenticated User: {}", authentication.getPrincipal());
        logger.info("User Authorities: {}", authentication.getAuthorities());

        // get current utc date
        LocalDate utcToday = LocalDate.now(ZoneOffset.UTC);

        Integer sqlHugCount = hugEventService.getSQlHugCountForPairingAndDate(pairingId, utcToday);

        return ResponseEntity.ok(Map.of("count", sqlHugCount));
        
    }
    
}
