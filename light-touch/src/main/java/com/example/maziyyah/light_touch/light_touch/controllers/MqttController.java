package com.example.maziyyah.light_touch.light_touch.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
// import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.maziyyah.light_touch.light_touch.services.MQTTService;

// import org.springframework.http.ResponseEntity;
// import org.springframework.web.bind.annotation.PostMapping;
// import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/mqtt")
public class MqttController {

    private final MQTTService mqttService;

    public MqttController(MQTTService mqttService) {
        this.mqttService = mqttService;
    }

    // @PostMapping("/publish")
    // public ResponseEntity<?> publishMoodLog(@RequestParam String message) {
    //     mqttService.publishMessage("userA", message);
        
    //     return ResponseEntity.ok().body("published");
    // }
    
    
    
}
