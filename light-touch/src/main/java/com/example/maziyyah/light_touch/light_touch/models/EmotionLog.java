package com.example.maziyyah.light_touch.light_touch.models;

// import java.time.LocalDateTime;

// import java.util.UUID;

public class EmotionLog {

    private String firebaseUid;
    // private int emotionLogId;
    private String deviceId;
    private String emotion;
    private int intensity;
    // private String notes;
    // private LocalDateTime createdAt;
    private String timestamp;
    private boolean sendToDevice = false;

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

}
