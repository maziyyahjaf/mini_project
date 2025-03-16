package com.example.maziyyah.light_touch.light_touch.repositories;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.example.maziyyah.light_touch.light_touch.models.DevicePairing;

@Repository
public class DevicePairingRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private static final String FIND_PAIRING = "SELECT device1_id, device2_id FROM device_pairings " +
            "WHERE device1_id = ? OR device2_id = ?";

    public Optional<DevicePairing> findPairingByDeviceId(String deviceId) {
        try {
            return Optional.ofNullable(
                    jdbcTemplate.queryForObject(FIND_PAIRING,
                            (rs, rowNum) -> new DevicePairing(rs.getString("device1_id"), rs.getString("device2_id")),
                            deviceId, deviceId));

        } catch (EmptyResultDataAccessException ex) {
            return Optional.empty(); // No result found, return empty Optional (might be invalid device id?)
        }

    }

}
