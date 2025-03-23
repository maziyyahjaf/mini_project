package com.example.maziyyah.light_touch.light_touch.models;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;

import jakarta.json.Json;
import jakarta.json.JsonObject;

public class EmotionLogDTO {

    private int emotionLogId;
    private String firebaseUid;
    private String emotion;
    private int intensity;
    private LocalDateTime timestamp; // This would be the converted timestamp
    private boolean sendToDevice;
    private String notes;


    public int getEmotionLogId() {
        return emotionLogId;
    }
    public void setEmotionLogId(int emotionLogId) {
        this.emotionLogId = emotionLogId;
    }
    public String getFirebaseUid() {
        return firebaseUid;
    }
    public void setFirebaseUid(String firebaseUid) {
        this.firebaseUid = firebaseUid;
    }
    public String getEmotion() {
        return emotion;
    }
    public void setEmotion(String emotion) {
        this.emotion = emotion;
    }
    public int getIntensity() {
        return intensity;
    }
    public void setIntensity(int intensity) {
        this.intensity = intensity;
    }
    public LocalDateTime getTimestamp() {
        return timestamp;
    }
    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
    public boolean isSendToDevice() {
        return sendToDevice;
    }
    public void setSendToDevice(boolean sendToDevice) {
        this.sendToDevice = sendToDevice;
    }

    public String getNotes() {
        return notes;
    }
    public void setNotes(String notes) {
        this.notes = notes;
    }

    

    public static EmotionLogDTO populate(ResultSet rs) throws SQLException {
        EmotionLogDTO dto = new EmotionLogDTO();
        dto.setEmotionLogId(rs.getInt("log_id"));
        dto.setFirebaseUid(rs.getString("firebase_user_id"));
        dto.setEmotion(rs.getString("emotion"));
        dto.setIntensity(rs.getInt("intensity"));
        Timestamp sqlTimestamp = rs.getTimestamp("local_timestamp");
        LocalDateTime timestamp = sqlTimestamp.toLocalDateTime();
        dto.setTimestamp(timestamp);
        dto.setSendToDevice(rs.getBoolean("send_to_device"));
        dto.setNotes(rs.getString("notes"));

        return dto;

    }

    public JsonObject toJSON() {
        JsonObject json = Json.createObjectBuilder()
                            .add("emotionLogId", getEmotionLogId())
                            .add("emotion", getEmotion())
                            .add("intensity", getIntensity())
                            .add("timestamp", getTimestamp().toString())
                            .add("sendToDevice", isSendToDevice())
                            .add("notes", getNotes())
                            .build();
        return json;
    }
    


    


    
}
