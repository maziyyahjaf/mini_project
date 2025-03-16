package com.example.maziyyah.light_touch.light_touch.models;

public class DevicePairing {

    private String device1Id;
    private String device2Id;

    public DevicePairing() {
    }

    public DevicePairing(String device1Id, String device2Id) {
        this.device1Id = device1Id;
        this.device2Id = device2Id;
    }

    public String getDevice1Id() {
        return device1Id;
    }
    public void setDevice1Id(String device1Id) {
        this.device1Id = device1Id;
    }
    public String getDevice2Id() {
        return device2Id;
    }
    public void setDevice2Id(String device2Id) {
        this.device2Id = device2Id;
    }



    
    
}
