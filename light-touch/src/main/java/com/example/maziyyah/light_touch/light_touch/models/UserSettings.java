package com.example.maziyyah.light_touch.light_touch.models;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UserSettings {

    private boolean pairingStatus;
    private String telegramChatId;
    private String telegramLinkCode;

    public boolean isPairingStatus() {
        return pairingStatus;
    }
    public void setPairingStatus(boolean pairingStatus) {
        this.pairingStatus = pairingStatus;
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
    


    public static UserSettings populate(ResultSet rs) throws SQLException {
        UserSettings userSettings = new UserSettings();
        boolean pairingStatus = rs.getBoolean("is_paired");
        String telegramChatId = rs.getString("telegram_chat_id");
        String telegramLinkCode = rs.getString("telegram_link_code");
        userSettings.setPairingStatus(pairingStatus);
        userSettings.setTelegramChatId(telegramChatId);
        userSettings.setTelegramLinkCode(telegramLinkCode);

        return userSettings;

    }
   

    
    
}
