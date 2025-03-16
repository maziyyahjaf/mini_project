package com.example.maziyyah.light_touch.light_touch.repositories;

import java.time.Duration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.example.maziyyah.light_touch.light_touch.utils.Utils;

@Repository
public class WebhookRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Qualifier(Utils.template01)
    private final RedisTemplate<String, Object> template;

    private static final Logger logger = LoggerFactory.getLogger(WebhookRepository.class);
    public static final String PROCESSED_UPDATE_KEY_PREFIX = "processed_updateID:";

    public WebhookRepository(@Qualifier(Utils.template01) RedisTemplate<String, Object> template) {
        this.template = template;
    }

    public Boolean isDuplicateUpdate(Integer updateId) {
        String redisKey = PROCESSED_UPDATE_KEY_PREFIX + updateId; // each update Id has its own key
        return template.hasKey(redisKey);
    }

    public void markUpdateAsProcessed(Integer updateId) {
        String redisKey = PROCESSED_UPDATE_KEY_PREFIX + updateId;
        template.opsForValue().set(redisKey, "1", Duration.ofMinutes(5)); // key will expore in 5 minutes
    }

    public String findDeviceIDByLinkingCode(String linkingCode) {
        String sql = "SELECT device_id FROM users WHERE telegram_link_code = ?";
        try {
            return jdbcTemplate.queryForObject(sql, String.class, linkingCode);
        } catch (DataAccessException e) {
            logger.error("No matching code found");
            // throw exception?
            return null;
        } 
    }

    public void saveTelegramChatId(String deviceId, String chatIdString) {
        String sql = "UPDATE users SET telegram_chat_id = ?, telegram_link_code = NULL WHERE device_id = ?";
        jdbcTemplate.update(sql, chatIdString, deviceId);
    }

    public String findPairedDeviceIdBasedOnChatId(String chatIdString) {
        String sql = "SELECT paired_device_id FROM users WHERE telegram_chat_id = ?";

        try {
            return jdbcTemplate.queryForObject(sql, String.class, chatIdString);
        } catch (DataAccessException ex) {
            logger.error("SQL Error: {} - {}", ex.getMessage(), ex.getCause());
            return null;
        }
    }


    
}
