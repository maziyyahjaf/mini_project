package com.example.maziyyah.light_touch.light_touch.services;

import org.springframework.stereotype.Service;

import com.example.maziyyah.light_touch.light_touch.repositories.HugEventRepository;

@Service
public class HugEventService {

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
    
    
}
