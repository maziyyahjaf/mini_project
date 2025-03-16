package com.example.maziyyah.light_touch.light_touch.services;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.maziyyah.light_touch.light_touch.models.Device;
import com.example.maziyyah.light_touch.light_touch.repositories.DeviceRepository;

@Service
public class DeviceService {

    private final DeviceRepository deviceRepository;

    public DeviceService(DeviceRepository deviceRepository) {
        this.deviceRepository = deviceRepository;
    }

    public Optional<Device> findAvailableDeviceById(String deviceId) {
        return deviceRepository.findAvailableDeviceById(deviceId);
    }

  

    
    

    
}
