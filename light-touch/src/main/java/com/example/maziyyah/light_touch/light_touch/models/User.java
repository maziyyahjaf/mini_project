package com.example.maziyyah.light_touch.light_touch.models;

import java.sql.ResultSet;
import java.sql.SQLException;

public class User {

    private String name;
    private String email;
    private String deviceId; // pre-generated 
    private String pairedDeviceId; // reference to another user
    private Boolean isPaired;
    private String telegramChatId;
    private String telegramLinkCode;
    private String timezone;
    private String pairing_id;


    public User() {
    }


    public User(String name, String email, String deviceId, String pairedDeviceId, Boolean isPaired,
            String telegramChatId, String telegramLinkCode, String timezone, String pairing_id) {
        this.name = name;
        this.email = email;
        this.deviceId = deviceId;
        this.pairedDeviceId = pairedDeviceId;
        this.isPaired = isPaired;
        this.telegramChatId = telegramChatId;
        this.telegramLinkCode = telegramLinkCode;
        this.timezone = timezone;
        this.pairing_id = pairing_id;
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


    public String getPairedDeviceId() {
        return pairedDeviceId;
    }


    public void setPairedDeviceId(String pairedDeviceId) {
        this.pairedDeviceId = pairedDeviceId;
    }


    public Boolean getIsPaired() {
        return isPaired;
    }


    public void setIsPaired(Boolean isPaired) {
        this.isPaired = isPaired;
    }


    public String getTelegramChatId() {
        return telegramChatId;
    }


    public void setTelegramChatId(String telegramChatId) {
        this.telegramChatId = telegramChatId;
    }


    public String getTelegramLinkCode() {
        return telegramLinkCode;
    }


    public void setTelegramLinkCode(String telegramLinkCode) {
        this.telegramLinkCode = telegramLinkCode;
    }


    public String getTimezone() {
        return timezone;
    }


    public void setTimezone(String timezone) {
        this.timezone = timezone;
    }


    public String getPairing_id() {
        return pairing_id;
    }


    public void setPairing_id(String pairing_id) {
        this.pairing_id = pairing_id;
    }

    public static User populate(ResultSet rs) throws SQLException {
       
        String name = rs.getString("name");
        String email = rs.getString("email");
        String deviceId = rs.getString("device_id");
        String pairedDeviceId = rs.getString("paired_device_id");
        Boolean isPaired = rs.getBoolean("is_paired");
        String telegramChatId = rs.getString("telegram_chat_id");
        String telegramLinkCode = rs.getString("telegram_link_code");
        String timezone = rs.getString("timezone");
        String pairingId = rs.getString("pairing_id");

        User user = new User(name, email, deviceId, pairedDeviceId, isPaired, telegramChatId, telegramLinkCode, timezone, pairingId);



        return user;
        
    }

    

    



  

    

    

    
    

    

    

}
