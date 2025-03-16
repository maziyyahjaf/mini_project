package com.example.maziyyah.light_touch.light_touch.models;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Device {

    private String deviceId;
    private Boolean isAvailable;


    public String getDeviceId() {
        return deviceId;
    }
    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public Boolean getIsAvailable() {
        return isAvailable;
    }
    public void setIsAvailable(Boolean isAvailable) {
        this.isAvailable = isAvailable;
    }


    public static Device populate(ResultSet rs) throws SQLException {
        Device device = new Device();
        device.setDeviceId(rs.getString("device_id"));
        device.setIsAvailable(rs.getBoolean("is_available"));

        return device;
    }
    

    
    
}
