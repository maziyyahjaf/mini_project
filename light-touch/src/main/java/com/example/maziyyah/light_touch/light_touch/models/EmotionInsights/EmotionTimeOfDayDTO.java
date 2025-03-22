package com.example.maziyyah.light_touch.light_touch.models.EmotionInsights;

import java.sql.ResultSet;
import java.sql.SQLException;

public class EmotionTimeOfDayDTO {
    private String timeOfDay;
    private String emotion;
    private int frequency;


    
    public EmotionTimeOfDayDTO() {
    }
    public EmotionTimeOfDayDTO(String timeOfDay, String emotion, int frequency) {
        this.timeOfDay = timeOfDay;
        this.emotion = emotion;
        this.frequency = frequency;
    }
    public String getTimeOfDay() {
        return timeOfDay;
    }
    public void setTimeOfDay(String timeOfDay) {
        this.timeOfDay = timeOfDay;
    }
    public String getEmotion() {
        return emotion;
    }
    public void setEmotion(String emotion) {
        this.emotion = emotion;
    }
    public int getFrequency() {
        return frequency;
    }
    public void setFrequency(int frequency) {
        this.frequency = frequency;
    }

    public static EmotionTimeOfDayDTO populate(ResultSet rs) throws SQLException {
        EmotionTimeOfDayDTO dto = new EmotionTimeOfDayDTO();
        dto.setTimeOfDay(rs.getString("time_of_day"));
        dto.setEmotion(rs.getString("emotion"));
        dto.setFrequency(rs.getInt("frequency"));

        return dto;
        
    }

    
}
