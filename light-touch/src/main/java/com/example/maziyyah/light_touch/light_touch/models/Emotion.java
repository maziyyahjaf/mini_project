package com.example.maziyyah.light_touch.light_touch.models;

import java.sql.ResultSet;
import java.sql.SQLException;

import jakarta.json.Json;
import jakarta.json.JsonObject;

public class Emotion {
    private String emotionName;
    private String emotionIconReference;
    private int displayOrder;
    
    public Emotion() {
    }

    public Emotion(String emotionName, String emotionIconReference, int displayOrder) {
        this.emotionName = emotionName;
        this.emotionIconReference = emotionIconReference;
        this.displayOrder = displayOrder;
    }
    
    public String getEmotionName() {
        return emotionName;
    }
    public void setEmotionName(String emotionName) {
        this.emotionName = emotionName;
    }
    public int getDisplayOrder() {
        return displayOrder;
    }
    public void setDisplayOrder(int displayOrder) {
        this.displayOrder = displayOrder;
    }
    public String getEmotionIconReference() {
        return emotionIconReference;
    }
    public void setEmotionIconReference(String emotionIconReference) {
        this.emotionIconReference = emotionIconReference;
    }

    public static Emotion populate(ResultSet rs) throws SQLException {
        Emotion emotion = new Emotion();
        emotion.setEmotionName(rs.getString("emotion_name"));
        emotion.setEmotionIconReference(rs.getString("emotion_icon_reference"));
        emotion.setDisplayOrder(rs.getInt("display_order"));

        return emotion;
    }

    public JsonObject toJsonObject() {
        return Json.createObjectBuilder()
                    .add("emotionName", getEmotionName())
                    .add("emotionIconReference", getEmotionIconReference())
                    .add("displayOrder", getDisplayOrder())
                    .build();
    }
    

    
}
