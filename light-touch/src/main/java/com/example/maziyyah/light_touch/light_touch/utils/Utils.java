package com.example.maziyyah.light_touch.light_touch.utils;

import java.io.StringReader;

import com.example.maziyyah.light_touch.light_touch.models.EmotionLog;
import com.example.maziyyah.light_touch.light_touch.models.RegistrationPayload;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;

public class Utils {

    public static final String template01 = "template01";
    public static final String template02 = "template02";

    public static final JsonObject sendTextToNLPService(String text) {
        JsonObject textJsonObj = Json.createObjectBuilder()
                                    .add("text", text)
                                    .build();
        return textJsonObj;
    }

    public static RegistrationPayload toRegistrationPayload(String payload) {
        JsonReader jsonReader = Json.createReader(new StringReader(payload));
        JsonObject jsonObject = jsonReader.readObject();

        String firebaseUid = jsonObject.getString("firebaseUid");
        String name = jsonObject.getString("name");
        String email = jsonObject.getString("email");
        String deviceId = jsonObject.getString("deviceId");
        String timezone = jsonObject.getString("timezone");

        RegistrationPayload registrationPayload = new RegistrationPayload(firebaseUid, name, email, deviceId, timezone);
        return registrationPayload;
    }

    public static EmotionLog toEmotionLog(String payload, String firebaseUid) {
        JsonReader jsonReader = Json.createReader(new StringReader(payload));
        JsonObject jsonObject = jsonReader.readObject();

        int intensity = jsonObject.getInt("intensity");
        String deviceId =  jsonObject.getString("deviceId");
        String emotion = jsonObject.getString("emotion");
        String timestamp = jsonObject.getString("timestamp");
        boolean sendToDevice = jsonObject.getBoolean("sendToDevice");
        String notes = jsonObject.getString("notes");

        EmotionLog emotionLog = new EmotionLog();
        emotionLog.setFirebaseUid(firebaseUid);
        emotionLog.setDeviceId(deviceId);
        emotionLog.setEmotion(emotion);
        emotionLog.setIntensity(intensity);
        emotionLog.setTimestamp(timestamp);
        emotionLog.setSendToDevice(sendToDevice);
        emotionLog.setNotes(notes);

        return emotionLog;

    }

    public static EmotionLog toUpdatedEmotionLog(String payload) {
        JsonReader jsonReader = Json.createReader(new StringReader(payload));
        JsonObject jsonObject = jsonReader.readObject();

        String deviceId =  jsonObject.getString("deviceId");
        int emotionLogId = jsonObject.getJsonNumber("emotionLogId").intValue();
        String emotion = jsonObject.getString("emotion");
        int intensity = jsonObject.getInt("intensity");
        String timestamp = jsonObject.getString("timestamp");
        boolean sendToDevice = jsonObject.getBoolean("sendToDevice");
        String notes = jsonObject.getString("notes");

        EmotionLog emotionLog = new EmotionLog();
        emotionLog.setDeviceId(deviceId);
        emotionLog.setEmotionLogId(emotionLogId);
        emotionLog.setEmotion(emotion);
        emotionLog.setIntensity(intensity);
        emotionLog.setTimestamp(timestamp);
        emotionLog.setSendToDevice(sendToDevice);
        emotionLog.setNotes(notes);

        return emotionLog;
    }


    
}
