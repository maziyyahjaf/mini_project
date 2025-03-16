package com.example.maziyyah.light_touch.light_touch.controllers;

import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.maziyyah.light_touch.light_touch.services.DeviceService;
import com.example.maziyyah.light_touch.light_touch.models.ApiResponse;
import com.example.maziyyah.light_touch.light_touch.models.Device;

@RestController
@RequestMapping("/api")
public class DeviceController {

    private final DeviceService deviceService;
    
    public DeviceController(DeviceService deviceService) {
        this.deviceService = deviceService;
    }

    @GetMapping("/validate-device")
    public ResponseEntity<?> validateDeviceId(@RequestParam("deviceId") String deviceId) {

        Optional<Device> deviceOpt = deviceService.findAvailableDeviceById(deviceId);
        if (deviceOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                                .body(new ApiResponse("error", "The device ID you entered is invalid or already in use."));
        }
        
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse("valid", "Device is available."));
    }
    
}
