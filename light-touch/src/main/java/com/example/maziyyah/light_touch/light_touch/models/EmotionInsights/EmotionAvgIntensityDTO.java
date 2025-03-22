package com.example.maziyyah.light_touch.light_touch.models.EmotionInsights;

import java.sql.ResultSet;
import java.sql.SQLException;

public class EmotionAvgIntensityDTO {
    private String emotionName;
    private double avgIntensity;

    public String getEmotionName() {
        return emotionName;
    }
    public void setEmotionName(String emotionName) {
        this.emotionName = emotionName;
    }
    public double getAvgIntensity() {
        return avgIntensity;
    }
    public void setAvgIntensity(double avgIntensity) {
        this.avgIntensity = avgIntensity;
    }

    public static EmotionAvgIntensityDTO populate(ResultSet rs) throws SQLException {
        EmotionAvgIntensityDTO dto = new EmotionAvgIntensityDTO();
        dto.setEmotionName(rs.getString("emotion_name"));
        dto.setAvgIntensity(rs.getDouble("avg_intensity"));

        return dto;
    }

    
    
}
