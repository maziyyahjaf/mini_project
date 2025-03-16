package com.example.maziyyah.light_touch.light_touch.controllers;

import java.io.StringReader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


import com.example.maziyyah.light_touch.light_touch.models.Update;
import com.example.maziyyah.light_touch.light_touch.services.TelegramNotificationService;
import com.example.maziyyah.light_touch.light_touch.services.WebhookService;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;

@RestController
public class WebhookController {

    private final WebhookService webhookService;
    private final TelegramNotificationService telegramNotificationService;
    private static final Logger logger = LoggerFactory.getLogger(WebhookController.class);

    // will need MQTTService too?
    /// to publish a remote hug

    public WebhookController(WebhookService webhookService, TelegramNotificationService telegramNotificationService) {
        this.webhookService = webhookService;
        this.telegramNotificationService = telegramNotificationService;
    }

    @PostMapping("/webhook")
    public ResponseEntity<String> receiveWebhook(@RequestBody String requestBody) {
        
        // parse the incoming JSON string
        try (JsonReader jsonReader = Json.createReader(new StringReader(requestBody))) {
            JsonObject jsonObject = jsonReader.readObject();

            logger.info("received jsonObject: {} ", jsonObject.toString());

            Integer updateId = jsonObject.getInt("update_id");
            logger.info("update id: {}", updateId);

            // check for duplicate update ids using Redis
            boolean isDuplicate = webhookService.isDuplicateUpdate(updateId);
            if (isDuplicate) {
                logger.info("Duplicate update_id detected, ignoring: " + updateId); 
                return ResponseEntity.ok("ok");
            }

            if (jsonObject.containsKey("callback_query")) {
                logger.info("json object contains callback_query key");
                // process callback query
                webhookService.processCallbackQuery(jsonObject);

                // need to get the device id of paired partner based on chat_id
                // then send to mqtt service to publish remote hug?
                  // Stop further execution to prevent processing as a normal message
                return ResponseEntity.ok("ok");
            }

            Update update = webhookService.toUpdate(jsonObject);
            // store update_id as processed to avoid duplicate processing
            webhookService.markUpdateAsProcessed(updateId);

            // Extract message text and chat ID
            String messageText = update.getMessage_text();
            long chatId = update.getChat_id();

            logger.info("Processing /start command. Message text: {}", messageText);

            if (messageText.startsWith("/start")) {
                // extract the part after '/start'
                if (messageText.length() > 6) {
                    String token = messageText.substring(7).trim();
                    logger.info("Extracted token: {}", token);

                    if (token.length() == 6) {
                        // find the device id, account for when invalid linking code?
                        // here i want to link the telegram chat info (update) with user via token
                        if(webhookService.handleUserLinking(token, update)) {
                            logger.info("Successfully linked Telegram chat id with account");
                            String successMessage = "Account is linked.";
                            String chatIdString = String.valueOf(chatId);

                            try {
                                telegramNotificationService.sendTelegramNotification(chatIdString, successMessage);
                            } catch (Exception e) {
                                logger.error("Failed to send notification to chat ID {}: {}", chatId, e.getMessage(), e);
                            }
                        }
                    }
                }
            }
            
        }

        return ResponseEntity.ok("ok");
    }
    
}
