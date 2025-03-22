package com.example.maziyyah.light_touch.light_touch.models.EmotionInsights;

import java.sql.ResultSet;
import java.sql.SQLException;

public class EmotionFrequencyDTO {
    private String emotionName; // name of emotion
    private int frequency; // frequency of emotion

    
    public EmotionFrequencyDTO() {
    }

    public EmotionFrequencyDTO(String emotionName, int frequency) {
        this.emotionName = emotionName;
        this.frequency = frequency;
    }

    public String getEmotionName() {
        return emotionName;
    }
    public void setEmotionName(String emotionName) {
        this.emotionName = emotionName;
    }
    public int getFrequency() {
        return frequency;
    }
    public void setFrequency(int frequency) {
        this.frequency = frequency;
    }

    public static EmotionFrequencyDTO populate(ResultSet rs) throws SQLException {
        EmotionFrequencyDTO dto = new EmotionFrequencyDTO();
        dto.setEmotionName(rs.getString("emotion_name"));
        dto.setFrequency(rs.getInt("frequency"));

        return dto;
    }

    
}
