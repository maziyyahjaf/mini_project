package com.example.maziyyah.light_touch.light_touch.repositories;

import java.util.Optional;
import java.sql.ResultSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.example.maziyyah.light_touch.light_touch.models.UserSettings;

@Repository
public class UserSettingsRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public static final String FETCH_USER_SETTINGS = 
            """
                 SELECT is_paired,
                            telegram_chat_id,
                            telegram_link_code
                    FROM users
                    WHERE firebase_user_id = ?
            """;

    public Optional<UserSettings> fetchUserSettingsByFirebaseUid(String firebaseUid) {
        return jdbcTemplate.query(FETCH_USER_SETTINGS, (ResultSet rs) -> {
            if (rs.next()) {
                return Optional.of(UserSettings.populate(rs));
            } else {
                return Optional.empty();
            }
        }, firebaseUid);

        
    }
    
}
