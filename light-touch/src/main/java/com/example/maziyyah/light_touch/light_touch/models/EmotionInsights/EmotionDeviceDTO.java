package com.example.maziyyah.light_touch.light_touch.models.EmotionInsights;

import java.sql.ResultSet;
import java.sql.SQLException;

public class EmotionDeviceDTO {
    private String emotion;
    private int sentCount;


    public String getEmotion() {
        return emotion;
    }
    public void setEmotion(String emotion) {
        this.emotion = emotion;
    }
    public int getSentCount() {
        return sentCount;
    }
    public void setSentCount(int sentCount) {
        this.sentCount = sentCount;
    }

    public static EmotionDeviceDTO populate(ResultSet rs) throws SQLException {
        EmotionDeviceDTO dto = new EmotionDeviceDTO();
        dto.setEmotion(rs.getString("emotion"));
        dto.setSentCount(rs.getInt("sent_count"));

        return dto;

    }

    
    
}
