package com.example.maziyyah.light_touch.light_touch.controllers;

import java.io.StringReader;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
// import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.maziyyah.light_touch.light_touch.services.MQTTService;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;

// import org.springframework.http.ResponseEntity;
// import org.springframework.web.bind.annotation.PostMapping;
// import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/api/mqtt")
public class MqttController {

    private final MQTTService mqttService;

    public MqttController(MQTTService mqttService) {
        this.mqttService = mqttService;
    }

   @PostMapping(path = "/send-hug")
   public ResponseEntity<?> sendHugFromFrontEnd(@RequestBody String payload) {
        JsonReader jsonReader = Json.createReader(new StringReader(payload));
        JsonObject jsonObject = jsonReader.readObject();
        String pairedDeviceId = jsonObject.getString("pairedDeviceId");
        String topic = pairedDeviceId + "/tele_hug";
        String message = "Sent from front-end";
        mqttService.publishTelegramHug(topic, message);

        return ResponseEntity.ok().build();
   }


    
    
    
}
