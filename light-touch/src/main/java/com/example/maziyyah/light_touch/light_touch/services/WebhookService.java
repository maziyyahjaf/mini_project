package com.example.maziyyah.light_touch.light_touch.services;

import org.springframework.stereotype.Service;

import com.example.maziyyah.light_touch.light_touch.models.CallbackQuery;
import com.example.maziyyah.light_touch.light_touch.models.Update;
import com.example.maziyyah.light_touch.light_touch.repositories.WebhookRepository;

import jakarta.json.Json;
import jakarta.json.JsonObject;

@Service
public class WebhookService {

    private final WebhookRepository webhookRepository;
    private final MQTTService mqttService;
    private final TelegramNotificationService telegramNotificationService;

    public WebhookService(WebhookRepository webhookRepository, MQTTService mqttService, TelegramNotificationService telegramNotificationService) {
        this.webhookRepository = webhookRepository;
        this.mqttService = mqttService;
        this.telegramNotificationService = telegramNotificationService;
    }

    public Boolean isDuplicateUpdate(Integer updateId) {
        return webhookRepository.isDuplicateUpdate(updateId);
    }

    public void markUpdateAsProcessed(Integer updateId) {
        webhookRepository.markUpdateAsProcessed(updateId);
    }

    public Update toUpdate(JsonObject jsonObject) {
        if (jsonObject == null || !jsonObject.containsKey("message")) {
            throw new IllegalArgumentException("Invalid JSON: Missing 'message' object");
        }

        JsonObject message = jsonObject.getJsonObject("message");
        
        // extract message text
        String message_text = message.containsKey("text") ? message.getString("text") : null;

        // extract sender's details
        JsonObject from = message.getJsonObject("from");
        Long from_id = from != null && from.containsKey("id") ? from.getJsonNumber("id").longValue() : null;

        // extract chat details
        JsonObject chat = message.getJsonObject("chat");
        Long chat_id = chat != null && chat.containsKey("id") ? chat.getJsonNumber("id").longValue() : null;

        if (chat_id == null || from_id == null || message_text == null) {
            throw new IllegalArgumentException("Invalid JSON: Missing required fields");
        }

        Update update = new Update(chat_id, from_id, message_text);
        return update;


    }

    public Boolean handleUserLinking(String linkingCode, Update update) {
        String deviceId = findDeviceIDByLinkingCode(linkingCode);
        String chatIdString = String.valueOf(update.getChat_id());
        if (deviceId != null) {
            saveTelegramChatId(deviceId, chatIdString);
            return true;
        }

        return false;
        
    }

    public String findDeviceIDByLinkingCode(String linkingCode) {
        return webhookRepository.findDeviceIDByLinkingCode(linkingCode);
    }

    public void saveTelegramChatId(String deviceId, String chatIdString) {
        webhookRepository.saveTelegramChatId(deviceId, chatIdString);
    }

    public CallbackQuery toCallbackQuery (JsonObject jsonObject) {
        if (jsonObject == null || !jsonObject.containsKey("callback_query")) {
            throw new IllegalArgumentException("Invalid JSON: Missing 'message' object");
        }

        JsonObject callbackQueryObj = jsonObject.getJsonObject("callback_query");
        String callbackQueryId = callbackQueryObj.getString("id");
        JsonObject from = callbackQueryObj.getJsonObject("from");
        Long from_id = from != null && from.containsKey("id") ? from.getJsonNumber("id").longValue() : null;
        JsonObject message = callbackQueryObj.getJsonObject("message");
        JsonObject chat = message.getJsonObject("chat");
        Long chat_id = chat != null && chat.containsKey("id") ? chat.getJsonNumber("id").longValue() : null;

        CallbackQuery callbackQuery = new CallbackQuery(callbackQueryId, from_id, chat_id);

        return callbackQuery;
    }

    public void processCallbackQuery(JsonObject jsonObject) {
        CallbackQuery callbackQuery = toCallbackQuery(jsonObject);

        // find paired device id based on chat_id
        String chatIdString = String.valueOf(callbackQuery.getChat_id());
        String pairedDeviceId = webhookRepository.findPairedDeviceIdBasedOnChatId(chatIdString);
        String topic = pairedDeviceId + "/tele_hug";
        String message = "Sent from telegram";
        mqttService.publishTelegramHug(topic, message);

        // answer to callback query
        String callbackQueryId = callbackQuery.getCallback_query_id();
        answerCallbackQuery(callbackQueryId, "Hug sent! ❤️");


    }

    public void answerCallbackQuery(String callbackQueryId, String message) {
        JsonObject jsonObject = Json.createObjectBuilder()
                                    .add("callback_query_id", callbackQueryId)
                                    .add("text", message)
                                    .add("show_alert", false)
                                    .build();

        telegramNotificationService.respondToCallbackQuery(jsonObject);
    }
    
    
}
