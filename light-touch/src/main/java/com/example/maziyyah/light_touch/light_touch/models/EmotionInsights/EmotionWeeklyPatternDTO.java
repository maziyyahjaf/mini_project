package com.example.maziyyah.light_touch.light_touch.models.EmotionInsights;

import java.io.StringReader;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;



import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonNumber;
import jakarta.json.JsonReader;
import jakarta.json.JsonValue;

public class EmotionWeeklyPatternDTO {
    private String dayOfWeek;
    private String emotion;
    private int frequency;
    private List<Integer> logIds;

    public String getDayOfWeek() {
        return dayOfWeek;
    }
    public void setDayOfWeek(String dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
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

    public List<Integer> getLogIds() {
        return logIds;
    }
    public void setLogIds(List<Integer> logIds) {
        this.logIds = logIds;
    }
   

    public static EmotionWeeklyPatternDTO populate(ResultSet rs) throws SQLException {
        EmotionWeeklyPatternDTO dto = new EmotionWeeklyPatternDTO();
        dto.setDayOfWeek(rs.getString("day_of_week"));
        dto.setEmotion(rs.getString("emotion"));
        dto.setFrequency(rs.getInt("frequency"));
        String logIdsJson = rs.getString("log_ids");
        List<Integer> logIdList = parseLogIds(logIdsJson);
        dto.setLogIds(logIdList);

        return dto;
    }

    private static List<Integer> parseLogIds(String logIdsJson) {
        List<Integer> logIdList = new ArrayList<>();
        JsonReader jsonReader = Json.createReader(new StringReader(logIdsJson));
        JsonArray jsonArray = jsonReader.readArray();
        for (JsonValue value : jsonArray) {
            // Check if the value is a JsonNumber
            if (value instanceof JsonNumber) {
                JsonNumber jsonNumber = (JsonNumber) value;
                logIdList.add(jsonNumber.intValue());
            }
        }
        return logIdList;
    }
   

    
    
}
