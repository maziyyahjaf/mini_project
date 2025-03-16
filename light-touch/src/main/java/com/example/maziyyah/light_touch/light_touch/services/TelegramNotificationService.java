package com.example.maziyyah.light_touch.light_touch.services;


import java.net.URI;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonObject;

@Service
public class TelegramNotificationService {

    @Value("${telegram.bot.token}")
    private String telegramBotToken;

    @Value("${telegram.bot.message.url}")
    private String telegramBotMessageUrl;

    RestTemplate restTemplate = new RestTemplate();

    private static final Logger logger = LoggerFactory.getLogger(TelegramNotificationService.class);

    // this is for sending notifications to loved ones when they missed toy interactions
    public void sendTelegramNotification(String telegramChatId, String message) {
        logger.info("telegramchatid: {}, message: {}", telegramChatId, message);
        JsonObject jsonObject = Json.createObjectBuilder()
                                    .add("chat_id", telegramChatId)
                                    .add("text", message)
                                    .build();

        sendTelegramNotificationRequest(jsonObject);
    }

    public void sendTelegramNotificationWithButton(String telegramChatId, String message) {
        logger.info("telegramchatid: {}, message: {}", telegramChatId, message);                      
   
        
        // Create button
        JsonObject sendHugJsonObject = Json.createObjectBuilder()
                                            .add("text", "Send a Hug ❤️")
                                            .add("callback_data", "send_hug")
                                            .build();

        // Wrap the button inside a nested array
        JsonArray inlineKeyboardRow = Json.createArrayBuilder().add(sendHugJsonObject).build();
        JsonArray inlineKeyboardArray = Json.createArrayBuilder().add(inlineKeyboardRow).build();
      
        // create reply_markup JSON object
        JsonObject inlineKeyboardJsonObject = Json.createObjectBuilder()
                                                    .add("inline_keyboard", inlineKeyboardArray)
                                                    .build();

        // Create main JSON payload
        JsonObject jsonObject = Json.createObjectBuilder()
                                .add("chat_id", telegramChatId)
                                .add("text", message)
                                .add("reply_markup", inlineKeyboardJsonObject)
                                .build();
        
        sendTelegramNotificationRequest(jsonObject);
    
    }

    public void sendTelegramNotificationRequest(JsonObject jsonObject) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-type", "application/json");

        RequestEntity<String> req = RequestEntity
                                        .post(URI.create(buildSendMessageUrl()))
                                        .headers(headers)
                                        .body(jsonObject.toString());

        try {
            ResponseEntity<String> response = restTemplate.exchange(req, String.class);
            logger.info("Response: Status={}, Body={}", response.getStatusCode(), response.getBody());

        } catch (Exception e) {
            logger.error("Failed to send Telegram message. Error: {}", e.getMessage(), e);
        }

    }

    public void respondToCallbackQuery(JsonObject jsonObject) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-type", "application/json");

        RequestEntity<String> req = RequestEntity
                                        .post(URI.create(buildAnswerCallbackQueryUrl()))
                                        .headers(headers)
                                        .body(jsonObject.toString());
         try {
            ResponseEntity<String> response = restTemplate.exchange(req, String.class);
            logger.info("Response: Status={}, Body={}", response.getStatusCode(), response.getBody());

        } catch (Exception e) {
            logger.error("Failed to answer Callback Query. Error: {}", e.getMessage(), e);
        }
        
    }

    public String buildSendMessageUrl() {
        return UriComponentsBuilder.fromUriString(telegramBotMessageUrl)
                .pathSegment("bot" + telegramBotToken)
                .pathSegment("sendMessage")
                .toUriString();
    }

    public String buildAnswerCallbackQueryUrl() {
        return UriComponentsBuilder.fromUriString(telegramBotMessageUrl)
                .pathSegment("bot" + telegramBotToken)
                .pathSegment("answerCallbackQuery")
                .toUriString();
    }
}
