package com.example.maziyyah.light_touch.light_touch.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.maziyyah.light_touch.light_touch.models.DashboardSnapshotDTO;
import com.example.maziyyah.light_touch.light_touch.services.DashboardService;

@RestController
@RequestMapping(path = "/api")
public class DashboardController {

    private static final Logger logger = LoggerFactory.getLogger(DashboardController.class);
    @Autowired
    private DashboardService dashboardService;
    
    @GetMapping(path = "/dashboard")
    public ResponseEntity<?> getDashboardSnapshot(@RequestParam("date") String isoDateString,
                                                    @RequestParam("pairingId") String pairingId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String firebaseUid = (String) authentication.getPrincipal();
        logger.info("Authenticated user UID: {}", firebaseUid);
        logger.info("Authenticated User in get latest emotion log: {}", authentication.getPrincipal());
        logger.info("User Authorities in get latest emotion log: {}", authentication.getAuthorities());
        
        DashboardSnapshotDTO dto = dashboardService.getDashboardSnapshot(isoDateString, pairingId, firebaseUid);
        return ResponseEntity.ok().body(dto);
    }
}
