package com.example.maziyyah.light_touch.light_touch.models;

public class HugStatus {

    private String deviceId; 
    private int simultaneousHugs;

    
    public HugStatus() {
    }

    public HugStatus(String deviceId, int simultaneousHugs) {
        this.deviceId = deviceId;
        this.simultaneousHugs = simultaneousHugs;
    }
    
    public String getDeviceId() {
        return deviceId;
    }
    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }
    public int getSimultaneousHugs() {
        return simultaneousHugs;
    }
    public void setSimultaneousHugs(int simultaneousHugs) {
        this.simultaneousHugs = simultaneousHugs;
    }

    


    
}
