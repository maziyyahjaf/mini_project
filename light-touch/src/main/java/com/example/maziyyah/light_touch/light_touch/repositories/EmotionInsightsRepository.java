package com.example.maziyyah.light_touch.light_touch.repositories;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.example.maziyyah.light_touch.light_touch.models.EmotionInsights.EmotionAvgIntensityDTO;
import com.example.maziyyah.light_touch.light_touch.models.EmotionInsights.EmotionDeviceDTO;
import com.example.maziyyah.light_touch.light_touch.models.EmotionInsights.EmotionFrequencyDTO;
import com.example.maziyyah.light_touch.light_touch.models.EmotionInsights.EmotionTimeOfDayDTO;
import com.example.maziyyah.light_touch.light_touch.repositories.Queries.EmotionInsightsQueries;

@Repository
public class EmotionInsightsRepository {

    private static final Logger logger = LoggerFactory.getLogger(EmotionInsightsRepository.class);

    @Autowired
    private JdbcTemplate jdbcTemplate;



    public Optional<List<EmotionFrequencyDTO>> getMostFrequentEmotions(String firebaseUid) {
        List<EmotionFrequencyDTO> emotionFrequencies = new ArrayList<>();

        try {
            // use jdbcTemplate.query with a RowMapper to process the result set
            emotionFrequencies = jdbcTemplate.query(
                EmotionInsightsQueries.MOST_FREQUENT_EMOTIONS,
                (rs, rowNum) -> EmotionFrequencyDTO.populate(rs),
                firebaseUid);

            // return Optional based on whether the list is empty or not
            return emotionFrequencies.isEmpty() ? Optional.empty() : Optional.of(emotionFrequencies);
            
        } catch (DataAccessException ex) {
            logger.error("SQL Error: {} - {}", ex.getMessage(), ex.getCause());
            throw new RuntimeException(String.format("Database error: Unable to retrieve most frequent emotions for %s.", firebaseUid));


        } 
    }

    // avg intensity of emotions (collectively)
    public Optional<List<EmotionAvgIntensityDTO>> getAvgEmotionIntensity(String firebaseUid) {
        List<EmotionAvgIntensityDTO> emotionAvgList = new ArrayList<>();
        try {
            emotionAvgList = jdbcTemplate.query(
                            EmotionInsightsQueries.AVG_INTENSITY_EMOTIONS,
                            (rs, rowNum) -> EmotionAvgIntensityDTO.populate(rs),
                            firebaseUid);
            return emotionAvgList.isEmpty() ? Optional.empty() : Optional.of(emotionAvgList);

        } catch (DataAccessException ex) {
            logger.error("SQL Error: {} - {}", ex.getMessage(), ex.getCause());
            throw new RuntimeException(String.format("Database error: Unable to retrieve avg emotions intensities for %s.", firebaseUid));


        }
    }
    //collective
    public Optional<List<EmotionTimeOfDayDTO>> getTimeOfDayAnalysis(String timezoneOffset, String firebaseUid) {
        List<EmotionTimeOfDayDTO> emotionTimeOfDayList = new ArrayList<>();

        try {
            emotionTimeOfDayList = jdbcTemplate.query(
                                    EmotionInsightsQueries.TIME_OF_DAY_ANALYSIS,
                                    (rs, rowNum) -> EmotionTimeOfDayDTO.populate(rs),
                                    timezoneOffset,
                                    timezoneOffset,
                                    timezoneOffset,
                                    timezoneOffset,
                                    timezoneOffset,
                                    firebaseUid);
            return emotionTimeOfDayList.isEmpty() ? Optional.empty() : Optional.of(emotionTimeOfDayList);

        } catch (DataAccessException ex) {
            logger.error("SQL Error: {} - {}", ex.getMessage(), ex.getCause());
            throw new RuntimeException(String.format("Database error: Unable to retrieve time of day emotion analysis for %s.", firebaseUid));

        }
    }

    public Optional<List<EmotionDeviceDTO>> getDeviceInteractionAnalysis(String firebaseUid) {
        List<EmotionDeviceDTO> emotionDeviceList = new ArrayList<>();

        try {
            emotionDeviceList = jdbcTemplate.query(
                                    EmotionInsightsQueries.DEVICE_INTERACTION_ANALYSIS,
                                    (rs, rowNum) -> EmotionDeviceDTO.populate(rs),
                                    firebaseUid
            );

            return emotionDeviceList.isEmpty() ? Optional.empty() : Optional.of(emotionDeviceList);

        } catch (DataAccessException ex) {
            logger.error("SQL Error: {} - {}", ex.getMessage(), ex.getCause());
            throw new RuntimeException(String.format("Database error: Unable to retrieve device interfaction analysis for %s.", firebaseUid));

        }
    }

    public String getUserTimeZone(String firebaseUid) {
        String sql = "SELECT timezone FROM users WHERE firebase_user_id = ?";

        try {
            return jdbcTemplate.queryForObject(sql, String.class, firebaseUid);
        } catch (DataAccessException ex) {
            logger.error("SQL Error: {} - {}", ex.getMessage(), ex.getCause());
            logger.error("Error retrieveing timezone from MySQL for user: {}", firebaseUid, ex);
            throw new RuntimeException("Database error: Unable to retrieve timezone info.");

        }
    }
    
}
