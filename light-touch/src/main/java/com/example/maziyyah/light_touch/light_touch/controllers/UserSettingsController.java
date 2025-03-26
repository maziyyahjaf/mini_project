package com.example.maziyyah.light_touch.light_touch.controllers;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.maziyyah.light_touch.light_touch.models.UserSettings;
import com.example.maziyyah.light_touch.light_touch.services.UserSettingsService;

@RestController
@RequestMapping("/api/user/settings")
public class UserSettingsController {

    private static final Logger logger = LoggerFactory.getLogger(UserSettingsController.class);
    @Autowired
    UserSettingsService userSettingsService;
    
    // for user settings information
    @GetMapping("")
    public ResponseEntity<?> fetchUserSettings() {
        // Retrieve the authenticated user's Firebase UID
         Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
         String firebaseUid = (String) authentication.getPrincipal();
         logger.info("Authenticated user UID: {}", firebaseUid);
         logger.info("Authenticated User in fetchUserSettings: {}", authentication.getPrincipal());
         logger.info("User Authorities in fetchUserSettings: {}", authentication.getAuthorities());

         Optional<UserSettings> userSettingsOpt = userSettingsService.fetchUserSettingsByFirebaseUid(firebaseUid);

         if (userSettingsOpt.isEmpty()) {
            logger.error("No user settings details for firebase uid: {}", firebaseUid);
                return ResponseEntity.badRequest().body("No user settings found?");
         }

         UserSettings userSettings = userSettingsOpt.get();

         return ResponseEntity.ok().body(userSettings);
    }
}
