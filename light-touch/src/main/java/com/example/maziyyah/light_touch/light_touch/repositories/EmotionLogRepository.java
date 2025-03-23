package com.example.maziyyah.light_touch.light_touch.repositories;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.example.maziyyah.light_touch.light_touch.models.Emotion;
import com.example.maziyyah.light_touch.light_touch.models.EmotionLog;

@Repository
public class EmotionLogRepository {

    private static final Logger logger = LoggerFactory.getLogger(EmotionLogRepository.class);
    private final JdbcTemplate jdbcTemplate;
    
    public EmotionLogRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


    private static final String SELECT_ALL_EMOTIONS = "SELECT emotion_name, emotion_icon_reference, display_order FROM emotions ORDER BY display_order";

    public List<Emotion> getAllEmotions() {
        return jdbcTemplate.query(SELECT_ALL_EMOTIONS, (rs, rowNum) -> {
            return Emotion.populate(rs);
        });
    }
    
    // save emotion log to mySQL
    public void saveEmotionLog(EmotionLog log) {
        String sql = "INSERT INTO emotion_logs (firebase_user_id, emotion, intensity, timestamp, send_to_device, notes) VALUES (?, ?, ?, ?, ?, ?)";
        try {
            logger.info("timestamp from payload {}", log.getTimestamp());

            Timestamp sqlTimestamp = Timestamp.from(Instant.parse(log.getTimestamp()));
;
            logger.info("sql timestamp {}", sqlTimestamp.toString());

            //save to the db
            jdbcTemplate.update(sql, log.getFirebaseUid(), log.getEmotion(), log.getIntensity(), sqlTimestamp,log.isSendToDevice(), log.getNotes());

        } catch (DataAccessException ex) {
            logger.error("SQL Error: {} - {}", ex.getMessage(), ex.getCause());
            logger.error("Error saving emotion log to MySQL for user: {}, device id: {}", log.getFirebaseUid(), log.getDeviceId(), ex);
            throw new RuntimeException("Database error: Unable to save emotion log.");
        }
    }


    public void updateEmotionLog(EmotionLog log) {
        String sql = "UPDATE emotion_logs SET emotion = ?, intensity = ?, timestamp = ?, send_to_device = ?, notes = ? WHERE log_id = ?";

        try {

            Timestamp sqlTimestamp = Timestamp.from(Instant.parse(log.getTimestamp()));
            jdbcTemplate.update(sql, log.getEmotion(), log.getIntensity(), sqlTimestamp, log.isSendToDevice(), log.getNotes(), log.getEmotionLogId());

        } catch (DataAccessException ex) {
            logger.error("SQL Error: {} - {}", ex.getMessage(), ex.getCause());
            logger.error("Error saving updated emotion log to MySQL for user: {}, device id: {}", log.getFirebaseUid(), log.getDeviceId(), ex);
            throw new RuntimeException("Database error: Unable to save updated emotion log.");

        }
    }


    // fetch device id based on firebase user id
    public String fetchDeviceIdBasedOnFirebaseUid(String firebaseUid) {
        String sql = "SELECT device_id FROM users WHERE firebase_user_id = ? ";

        try {
            return jdbcTemplate.queryForObject(sql, String.class, firebaseUid);
        } catch (DataAccessException ex) {
            logger.error("SQL Error: {} - {}", ex.getMessage(), ex.getCause());
            logger.error("Error retrieveing device id from MySQL for user: {}", firebaseUid, ex);
            throw new RuntimeException("Database error: Unable to retrieve device id.");

        }
    }
    
    // Retrieve emotion logs for a specific user
    public List<EmotionLog> findByUserId(String userId) {
        String sql = "SELECT * FROM emotion_logs WHERE firebase_user_id = ?";
        return jdbcTemplate.query(sql, new EmotionLogRowMapper(), userId);
    }

     // RowMapper to map SQL result to EmotionLog object
    private static class EmotionLogRowMapper implements RowMapper<EmotionLog> {
        @Override
        public EmotionLog mapRow(ResultSet rs, int rowNum) throws SQLException {
            EmotionLog log = new EmotionLog();
            // log.setEmotionLogId(rs.getInt("log_id"));
            // log.setUserId(rs.getString("user_id"));
            // log.setEmotion(rs.getString("emotion"));
            // log.setIntensity(rs.getInt("intensity"));
            // log.setNotes(rs.getString("notes"));

            // Convert TIMESTAMP from MySQL to LocalDateTime
            // Timestamp timestamp = rs.getTimestamp("created_at");
            // if (timestamp != null) {
            //     log.setCreatedAt(timestamp.toLocalDateTime());
            // }

            // log.setSentToDevice(rs.getBoolean("sent_to_device"));
            return log;
        }
    }

    public String fetchUserTimeZone(String firebaseUid) {
        String sql = "SELECT timezone FROM users WHERE firebase_user_id = ?";
        return jdbcTemplate.queryForObject(sql, String.class, firebaseUid);
    }
}


