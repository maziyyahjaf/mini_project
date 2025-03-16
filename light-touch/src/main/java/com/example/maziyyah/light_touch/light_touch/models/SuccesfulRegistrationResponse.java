package com.example.maziyyah.light_touch.light_touch.models;

public class SuccesfulRegistrationResponse {
    private String status;
    private String message;
    private String teleLinkingCode;
    
    public SuccesfulRegistrationResponse() {
    }

    public SuccesfulRegistrationResponse(String status, String message, String teleLinkingCode) {
        this.status = status;
        this.message = message;
        this.teleLinkingCode = teleLinkingCode;
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

    
}
