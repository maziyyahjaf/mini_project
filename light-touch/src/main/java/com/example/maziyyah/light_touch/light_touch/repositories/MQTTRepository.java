package com.example.maziyyah.light_touch.light_touch.repositories;

import java.lang.reflect.GenericArrayType;
import java.util.Optional;

import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class MQTTRepository {

    private final JdbcTemplate jdbcTemplate;

    public MQTTRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private static final String GET_PAIRED_DEVICE_ID = "SELECT paired_device_id FROM users WHERE device_id = ?";
    private static final String GET_TELEGRAM_CHAT_ID = "SELECT telegram_chat_id FROM users WHERE device_id = ?";
    private static final String GET_NAME = "SELECT name FROM users WHERE device_id = ?";

    public Optional<String> getPairedDeviceForDeviceId(String deviceId) {
        try {
            String pairedDeviceId = jdbcTemplate.queryForObject(GET_PAIRED_DEVICE_ID, String.class, deviceId);
            return Optional.ofNullable(pairedDeviceId);
        } catch (DataAccessException ex) {
            return Optional.empty();
        }
    }

    public Optional<String> getTelegramChatIdForDeviceId(String deviceId) {
        try {
            String telegramChatId = jdbcTemplate.queryForObject(GET_TELEGRAM_CHAT_ID, String.class, deviceId);
            return Optional.ofNullable(telegramChatId);
        } catch (EmptyResultDataAccessException ex) {
            return Optional.empty();
        }
    }

    // get partner info
    public String getName(String deviceId) {
        return jdbcTemplate.queryForObject(GET_NAME, String.class, deviceId);
    }


    
    
}
