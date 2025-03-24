package com.example.maziyyah.light_touch.light_touch.models;

import java.time.LocalDateTime;

public class PartnerEmotionDTO {
    private String emotion;
    private int intensity;
    private LocalDateTime timestamp;
    
    public String getEmotion() {
        return emotion;
    }
    public void setEmotion(String emotion) {
        this.emotion = emotion;
    }
    public LocalDateTime getTimestamp() {
        return timestamp;
    }
    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
    public int getIntensity() {
        return intensity;
    }
    public void setIntensity(int intensity) {
        this.intensity = intensity;
    }

    
    
    
}
