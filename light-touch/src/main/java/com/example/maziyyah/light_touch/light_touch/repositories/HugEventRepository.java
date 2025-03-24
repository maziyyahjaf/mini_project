package com.example.maziyyah.light_touch.light_touch.repositories;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
// import org.springframework.transaction.annotation.Transactional;

import com.example.maziyyah.light_touch.light_touch.utils.Utils;
import com.example.maziyyah.light_touch.light_touch.exceptions.HugEvents.ErrorRetrievingHugCount;
import com.example.maziyyah.light_touch.light_touch.exceptions.HugEvents.ErrorSavingHugEvent;
import com.example.maziyyah.light_touch.light_touch.models.HugEvent;

@Repository
public class HugEventRepository {

    private static final long SIMULTANEOUS_HUG_WINDOW_SEC = 10; 
    private static final Logger logger = LoggerFactory.getLogger(HugEventRepository.class);

    @Qualifier(Utils.template01)
    private final RedisTemplate<String, Object> template;

    private final JdbcTemplate jdbcTemplate;

    public HugEventRepository(@Qualifier(Utils.template01) RedisTemplate<String, Object> template,
                                JdbcTemplate jdbcTemplate) {
        this.template = template;
        this.jdbcTemplate = jdbcTemplate;
    }

    public void logSpontaneousHug(String deviceId) {
        String hugKey = "hug:" + deviceId;
        template.opsForValue().set(hugKey, "hug_detected",  SIMULTANEOUS_HUG_WINDOW_SEC, TimeUnit.SECONDS);
    }

    public boolean checkIfPairedDeviceSentHug(String pairedDeviceId) {
        String hugKey = "hug:" + pairedDeviceId;
        return template.hasKey(hugKey);
    }

    // save the hug count?

    // @Transactional
    public void saveHugEvent(HugEvent event) {
        // check if you need to explicitly set the timestamp to UTC 
        String sql = "INSERT INTO hugEvents(pairing_id, timestamp) VALUES (?, ?)";
        try {
            jdbcTemplate.update(sql, event.getPairingId(), event.getTimestamp());
            updateRedisCount(event.getPairingId());

        } catch (DataAccessException ex) {
            logger.error("SQL Error: {} - {}", ex.getMessage(), ex.getCause());
            throw new ErrorSavingHugEvent(String.format("Error saving hug event for %s detected at %s", event.getPairingId(), event.getTimestamp()));
        }
    }

    public void updateRedisCount(String pairingId) {
        // get current UTC date
        LocalDate utcToday = LocalDate.now(ZoneOffset.UTC);
        String redisKey = "hug:" + pairingId + ":" + utcToday;

        // increment the count
        Long count = template.opsForValue().increment(redisKey);
        if (count != null && count == 1) {
            // set ttl only if the key is new
            template.expire(redisKey, Duration.ofHours(48));
        }
    }

    public Object getRedisHugCount(String pairingId, LocalDate date) {
        String redisKey = "hug:" + pairingId + ":" + date;
        Object redisCountObj = template.opsForValue().get(redisKey);
        return redisCountObj;
    }

    public Integer getSQLHugCount(String pairingId, Instant startOfDay, Instant startOfNextDay) {
        String sql = "SELECT COUNT(*) FROM hugEvents WHERE pairing_id = ? AND timestamp BETWEEN ? AND ?";
        try {
            Integer count = jdbcTemplate.queryForObject(sql, Integer.class, pairingId, startOfDay, startOfNextDay);
            return count != null ? count : 0; // safe guard..
        } catch (DataAccessException ex) {
            logger.error("SQL Error: {} - {}", ex.getMessage(), ex.getCause());
            throw new ErrorRetrievingHugCount(String.format("Error retrieving hug count from SQL for pairing id %s", pairingId));
        }

    }

    public Optional<LocalDateTime> getLastSimultaneousHug(String pairingId) {
        String sql =
                """
                    SELECT timestamp
                    FROM hugEvents
                    WHERE pairing_id = ?
                    ORDER BY timestamp DESC
                    LIMIT 1
                """;

        try {
            LocalDateTime result = jdbcTemplate.queryForObject(sql,
                                        (rs,rowNum) -> rs.getTimestamp("timestamp").toLocalDateTime(),
                                        pairingId);
            return Optional.ofNullable(result);
        } catch (EmptyResultDataAccessException ex) {
            return Optional.empty(); // no hug found
        }
        
    }

  




    

    
}
