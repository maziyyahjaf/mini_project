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

import com.example.maziyyah.light_touch.light_touch.models.EmotionInsights.EmotionFrequencyDTO;
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
                firebaseUid
            );

            // return Optional based on whether the list is empty or not
            return emotionFrequencies.isEmpty() ? Optional.empty() : Optional.of(emotionFrequencies);
            
        } catch (DataAccessException ex) {
            logger.error("SQL Error: {} - {}", ex.getMessage(), ex.getCause());
            throw new RuntimeException(String.format("Database error: Unable to retrieve most frequent emotions for %s.", firebaseUid));


        } 
    }
    
}
