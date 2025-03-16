package com.example.maziyyah.light_touch.light_touch.models;

public class ApiResponse {

    private String status;
    private String message;

    
    public ApiResponse(String status, String message) {
        this.status = status;
        this.message = message;
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

    
    
}
