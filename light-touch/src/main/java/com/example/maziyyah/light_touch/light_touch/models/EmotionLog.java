package com.example.maziyyah.light_touch.light_touch.models;

import java.sql.ResultSet;
import java.sql.SQLException;

// import java.time.LocalDateTime;

// import java.util.UUID;

public class EmotionLog {

    private String firebaseUid;
    private String emotion;
    private int intensity;
    private String timestamp;
    private boolean sendToDevice = false;
    private String deviceId; // yes you need this.. (but currently not in db)
    //private int emotionLogId; // new -> in db
    private String notes; // new -> currently not in db

    public EmotionLog() {
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
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

    public String getFirebaseUid() {
        return firebaseUid;
    }

    public void setFirebaseUid(String firebaseUid) {
        this.firebaseUid = firebaseUid;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public boolean isSendToDevice() {
        return sendToDevice;
    }

    public void setSendToDevice(boolean sendToDevice) {
        this.sendToDevice = sendToDevice;
    }

    // public int getEmotionLogId() {
    //     return emotionLogId;
    // }

    // public void setEmotionLogId(int emotionLogId) {
    //     this.emotionLogId = emotionLogId;
    // }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

   

    

}
