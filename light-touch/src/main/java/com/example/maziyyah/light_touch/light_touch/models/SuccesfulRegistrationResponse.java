package com.example.maziyyah.light_touch.light_touch.models;

public class SuccesfulRegistrationResponse {
    private String status;
    private String message;
    private String teleLinkingCode;
    private String pairedDeviceId;
    private String pairingId;
    
    public SuccesfulRegistrationResponse() {
    }

    public SuccesfulRegistrationResponse(String status, String message, String teleLinkingCode, String pairedDeviceId, String pairingId) {
        this.status = status;
        this.message = message;
        this.teleLinkingCode = teleLinkingCode;
        this.pairedDeviceId = pairedDeviceId;
        this.pairingId = pairingId;
    }

    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }
    public String getTeleLinkingCode() {
        return teleLinkingCode;
    }
    public void setTeleLinkingCode(String teleLinkingCode) {
        this.teleLinkingCode = teleLinkingCode;
    }

    public String getPairedDeviceId() {
        return pairedDeviceId;
    }

    public void setPairedDeviceId(String pairedDeviceId) {
        this.pairedDeviceId = pairedDeviceId;
    }

    public String getPairingId() {
        return pairingId;
    }

    public void setPairingId(String pairingId) {
        this.pairingId = pairingId;
    }

    

    
}
