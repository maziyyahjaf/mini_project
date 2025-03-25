package com.example.maziyyah.light_touch.light_touch.repositories;

import java.sql.ResultSet;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.example.maziyyah.light_touch.light_touch.exceptions.Devices.ErrorSavingRegistrationDetailsException;
import com.example.maziyyah.light_touch.light_touch.models.RegistrationPayload;
import com.example.maziyyah.light_touch.light_touch.models.User;

@Repository
public class UserRepository {
    private static final Logger logger = LoggerFactory.getLogger(UserRepository.class);
    // save to sql database?
    // need to have the table of device ids ready
    // add users?
    // save users?

    @Autowired
    private JdbcTemplate jdbcTemplate;


    // flag the newly registered user's device id is_available from TRUE to FALSE;
    private static final String UPDATE_DEVICE_ID_AVAILABILITY = "UPDATE devices SET is_available = FALSE WHERE device_id = ?";
    // find the pre-linked paired device id
    private static final String FIND_PAIRED_DEVICE_ID_FOR_NEWLY_REGISTERED_DEVICE = 
            """
                  SELECT CASE 
                            WHEN device1_id = ? THEN device2_id
                            WHEN device2_id = ? THEN device1_id
                        END AS paired_device_id
                FROM device_pairings
                WHERE device1_id = ? OR device2_id = ?
            """;
    // check the status of the paired device id
    private static final String CHECK_PAIRED_DEVICE_ID_AVAILABILITY_STATUS = "SELECT is_available FROM devices WHERE device_id = ?";


    private static final String GET_DEVICE_ID = 
    "SELECT device_id FROM users WHERE firebase_user_id = ?";

    private static final String SAVE_NEWLY_REGISTERED_USER = 
            """
                INSERT INTO users(firebase_user_id, name, email, device_id, paired_device_id, is_paired, telegram_link_code, timezone, pairing_id)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)
            """;

    // when both user and partner have registered
    private static final String UPDATE_PAIRING_STATUS = 
            """
                UPDATE users SET is_paired = TRUE WHERE device_id IN (?, ?)
            """;
    
    private static final String FETCH_USER_DETAILS_AFTER_LOGIN = 
            """
                SELECT name, email, device_id, paired_device_id, is_paired, 
                telegram_chat_id, telegram_link_code, timezone, pairing_id
                FROM users
                WHERE firebase_user_id = ?
            
            """;

    public void updateDeviceIdAvailability(String deviceId) {
        jdbcTemplate.update(UPDATE_DEVICE_ID_AVAILABILITY, deviceId);
    }

    public Optional<String> findPairedDeviceIdForNewlyRegisteredDevice(String deviceId) {
        try {
            String pairedDeviceId = jdbcTemplate.queryForObject(
                FIND_PAIRED_DEVICE_ID_FOR_NEWLY_REGISTERED_DEVICE,
                String.class,
                deviceId, deviceId, deviceId, deviceId
            );
            return Optional.ofNullable(pairedDeviceId);
        } catch (EmptyResultDataAccessException ex) {
            return Optional.empty(); 
        }
    }

    public Optional<Boolean> checkPairedDeviceIdAvailabilityStatus(String pairedDeviceId) {
        try {
            Boolean isAvailable = jdbcTemplate.queryForObject(
                CHECK_PAIRED_DEVICE_ID_AVAILABILITY_STATUS, 
                boolean.class, 
                pairedDeviceId);
            return Optional.ofNullable(isAvailable);
        } catch (EmptyResultDataAccessException ex) {
            return Optional.empty(); // device does not exist - throw an exception in Service? (but unlikely..)
        }
        
    }

    public void updatePairingStatus(String deviceId, String pairedDeviceId) {
        jdbcTemplate.update(UPDATE_PAIRING_STATUS, deviceId, pairedDeviceId);
    }


    public String getDeviceIdForUserId(String userId) {
        return jdbcTemplate.queryForObject(GET_DEVICE_ID, String.class, userId);
    }

    public void saveLinkingCode(String deviceId, String linkingCode) {
        String sql = "UPDATE users SET telegram_link_code = ? WHERE device_id = ?";
        jdbcTemplate.update(sql, linkingCode, deviceId);
    }

    public void saveNewlyRegisteredUser(RegistrationPayload payload, String pairedDeviceId, Boolean isPairedStatus, String telegramLinkingCode, String pairingId) {
        try {
            jdbcTemplate.update(SAVE_NEWLY_REGISTERED_USER,
                                payload.getFirebaseUid(),
                                payload.getName(),
                                payload.getEmail(),
                                payload.getDeviceId(),
                                pairedDeviceId,
                                isPairedStatus,
                                telegramLinkingCode,
                                payload.getTimezone(),
                                pairingId);
        } catch (DataAccessException ex) {
            logger.error("SQL Error: {} - {}", ex.getMessage(), ex.getCause());

            throw new ErrorSavingRegistrationDetailsException("Error saving registration details.");
        }
    }

    public Optional<User> getUserDetailsByFirebaseUid(String firebaseUid) {
        return jdbcTemplate.query(FETCH_USER_DETAILS_AFTER_LOGIN, (ResultSet rs) -> {
            if (rs.next()) {
                return Optional.of(User.populate(rs));
            } else {
                return Optional.empty();
            }
        }, firebaseUid);
    }

    public Optional<String> getPartnerFirebaseUid(String pairingId, String currentUserId) {
        String sql = 
                """
                    SELECT firebase_user_id
                    FROM users
                    WHERE pairing_id = ?
                    AND firebase_user_id != ?
                    LIMIT 1
                
                """;
        try {
            String partnerId = jdbcTemplate.queryForObject(sql, String.class, pairingId, currentUserId);
            return Optional.of(partnerId);
        } catch (EmptyResultDataAccessException ex) {
            logger.error("SQL Error: {} - {}", ex.getMessage(), ex.getCause());
            return Optional.empty();
        }
    }

    public Optional<String> fetchEmailForEarlierRegisteredUser(String pairedDeviceId) {
        String sql =
                    """
                        SELECT email
                        FROM users
                        WHERE device_id = ?         
                    """;
        try {
            String earlierRegisteredUserEmail = jdbcTemplate.queryForObject(sql, String.class, pairedDeviceId);
            return Optional.of(earlierRegisteredUserEmail);
        } catch (EmptyResultDataAccessException ex) {
            logger.error("SQL Error: {} - {}", ex.getMessage(), ex.getCause());
            return Optional.empty();
        }
    }

    


}
