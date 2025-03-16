package com.example.maziyyah.light_touch.light_touch.repositories;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.example.maziyyah.light_touch.light_touch.models.EmotionLog;

@Repository
public class EmotionLogRepository {

    private static final Logger logger = LoggerFactory.getLogger(EmotionLogRepository.class);
    private final JdbcTemplate jdbcTemplate;
    
    public EmotionLogRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
    
    // save emotion log to mySQL
    public void saveEmotionLog(EmotionLog log) {
        String sql = "INSERT INTO emotion_logs (user_id, emotion, intensity, notes, sent_to_device) VALUES (?, ?, ?, ?, ?)";
        try {
            jdbcTemplate.update(sql, log.getUserId(), log.getEmotion(), log.getIntensity(), log.getNotes(), log.isSentToDevice());

        } catch (DataAccessException ex) {
            logger.error("Error saving emotion log to MySQL for user: {}", log.getUserId(), ex);
            throw new RuntimeException("Database error: Unable to save emotion log.");
        }
    }
    
    // Retrieve emotion logs for a specific user
    public List<EmotionLog> findByUserId(String userId) {
        String sql = "SELECT * FROM emotion_logs WHERE user_id = ?";
        return jdbcTemplate.query(sql, new EmotionLogRowMapper(), userId);
    }

     // RowMapper to map SQL result to EmotionLog object
    private static class EmotionLogRowMapper implements RowMapper<EmotionLog> {
        @Override
        public EmotionLog mapRow(ResultSet rs, int rowNum) throws SQLException {
            EmotionLog log = new EmotionLog();
            log.setEmotionLogId(rs.getInt("log_id"));
            log.setUserId(rs.getString("user_id"));
            log.setEmotion(rs.getString("emotion"));
            log.setIntensity(rs.getInt("intensity"));
            log.setNotes(rs.getString("notes"));

            // Convert TIMESTAMP from MySQL to LocalDateTime
            Timestamp timestamp = rs.getTimestamp("created_at");
            if (timestamp != null) {
                log.setCreatedAt(timestamp.toLocalDateTime());
            }

            log.setSentToDevice(rs.getBoolean("sent_to_device"));
            return log;
        }
    }
}


