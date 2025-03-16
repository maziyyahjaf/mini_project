package com.example.maziyyah.light_touch.light_touch.models;

public class LoginRequest {

    private String username;
    private String password; // shouldnt this be hashed first?

    
    public LoginRequest() {
    }


    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }

    
    
}
