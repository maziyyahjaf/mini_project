package com.example.maziyyah.light_touch.light_touch.repositories;

import java.sql.ResultSet;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.example.maziyyah.light_touch.light_touch.models.Device;

@Repository
public class DeviceRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private static final String FIND_AVAILABLE_DEVICE_BY_ID = 
                "SELECT device_id, is_available FROM devices WHERE device_id = ? AND is_available = TRUE";

    public Optional<Device> findAvailableDeviceById(String deviceId) {
        return jdbcTemplate.query(FIND_AVAILABLE_DEVICE_BY_ID, (ResultSet rs) -> {
            if (rs.next()) {
                return Optional.of(Device.populate(rs));
            } else {
                return Optional.empty();
            }
        }, deviceId);
    }
    
}
