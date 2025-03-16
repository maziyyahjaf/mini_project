package com.example.maziyyah.light_touch.light_touch.models;

import java.time.LocalDateTime;

// import java.util.UUID;

public class EmotionLog {
    
    private int emotionLogId;
    private String userId;
    private String emotion;
    private int intensity;
    private String notes;
    private LocalDateTime createdAt;
    private boolean sentToDevice = false;

    
    public EmotionLog() {
    }


    public EmotionLog(String userId, String emotion, int intensity, String notes,
            LocalDateTime createdAt, boolean sentToDevice) {
        this.userId = userId;
        this.emotion = emotion;
        this.intensity = intensity;
        this.notes = notes;
        this.createdAt = LocalDateTime.now(); // automatically set timestamp
        this.sentToDevice = sentToDevice;
    }


    public int getEmotionLogId() {
        return emotionLogId;
    }


    public void setEmotionLogId(int emotionLogId) {
        this.emotionLogId = emotionLogId;
    }


    public String getUserId() {
        return userId;
    }


    public void setUserId(String userId) {
        this.userId = userId;
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


    public String getNotes() {
        return notes;
    }


    public void setNotes(String notes) {
        this.notes = notes;
    }


    public LocalDateTime getCreatedAt() {
        return createdAt;
    }


    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }


    public boolean isSentToDevice() {
        return sentToDevice;
    }


    public void setSentToDevice(boolean sentToDevice) {
        this.sentToDevice = sentToDevice;
    }

    

    

    


    

    

    




    
}
