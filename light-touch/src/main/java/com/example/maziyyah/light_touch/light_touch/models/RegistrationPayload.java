package com.example.maziyyah.light_touch.light_touch.models;

public class RegistrationPayload {
    
    private String firebaseUid;
    private String name;
    private String email;
    private String deviceId;
    private String timezone;

    public RegistrationPayload() {
    }
    
    public RegistrationPayload(String firebaseUid, String name, String email, String deviceId, String timezone) {
        this.firebaseUid = firebaseUid;
        this.name = name;
        this.email = email;
        this.deviceId = deviceId;
        this.timezone = timezone;
    }
    public String getFirebaseUid() {
        return firebaseUid;
    }
    public void setFirebaseUid(String firebaseUid) {
        this.firebaseUid = firebaseUid;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getDeviceId() {
        return deviceId;
    }
    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }
    public String getTimezone() {
        return timezone;
    }
    public void setTimezone(String timezone) {
        this.timezone = timezone;
    }

    @Override
    public String toString() {
        return "RegistrationPayload [firebaseUid=" + firebaseUid + ", name=" + name + ", email=" + email + ", deviceId="
                + deviceId + ", timezone=" + timezone + "]";
    }

    
    

 

    


    
}
