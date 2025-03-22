package com.example.maziyyah.light_touch.light_touch.models;

import java.time.Instant;

public class HugEvent {

    private String pairingId;
    private Instant timestamp;

    

    public HugEvent() {
    }


    public HugEvent(String pairingId, Instant timestamp) {
        this.pairingId = pairingId;
        this.timestamp = timestamp;
    }

    
    public String getPairingId() {
        return pairingId;
    }


    public void setPairingId(String pairingId) {
        this.pairingId = pairingId;
    }


    public Instant getTimestamp() {
        return timestamp;
    }


    public void setTimestamp(Instant timestamp) {
        this.timestamp = timestamp;
    }


   

    
    
}
