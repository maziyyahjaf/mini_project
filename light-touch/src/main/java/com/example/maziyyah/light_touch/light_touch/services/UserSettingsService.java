package com.example.maziyyah.light_touch.light_touch.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.maziyyah.light_touch.light_touch.models.UserSettings;
import com.example.maziyyah.light_touch.light_touch.repositories.UserSettingsRepository;

@Service
public class UserSettingsService {
    @Autowired
    private UserSettingsRepository userSettingsRepository;

    public Optional<UserSettings> fetchUserSettingsByFirebaseUid(String firebaseUid) {
        return userSettingsRepository.fetchUserSettingsByFirebaseUid(firebaseUid);
    }
    
}
