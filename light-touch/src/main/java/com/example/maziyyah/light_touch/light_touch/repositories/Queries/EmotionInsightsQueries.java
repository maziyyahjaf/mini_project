package com.example.maziyyah.light_touch.light_touch.repositories.Queries;

public class EmotionInsightsQueries {
    
    public static final String MOST_FREQUENT_EMOTIONS = 
            """
                SELECT el.emotion
                COUNT(el.log_id) AS frequency
                FROM emotion_logs el
                WHERE el.firebase_user_id = ?
                GROUP BY el.emotion
                ORDER BY frequency DESC            
            """;


    public static final String AVG_INTENSITY_EMOTIONS = 
            """
                SELECT el.emotion,
                AVG(el.intensity) AS avg_intensity
                FROM emotion_logs el
                WHERE el.firebase_user_id = ?
                GROUP BY el.emotion
                ORDER BY avg_intensity DESC
            """;

    public static final String GET_MOOD_TRENDS_OVERALL = 
            """
                SELECT DATE(CONVERT_TZ(el.timestamp, @@session.time_zone, ?)) AS log_date, 
                            e.emotion_name, COUNT(el.log_id) AS frequency
                FROM emotion_logs el 
                WHERE el.firebase_user_id = ?
                GROUP BY log_date, el.emotion
                ORDER BY log_date ASC, frequency DESC
            """;

    // Analyze which emotions are most common at different times of the day (morning, afternoon, evening, night). // collective        
    public static final String TIME_OF_DAY_ANALYSIS = 
            """
                SELECT CASE
                        WHEN HOUR(CONVERT_TZ(el.timestamp, @@session.time_zone, ?)) BETWEEN 0 AND 4 THEN 'late night'
                        WHEN HOUR(CONVERT_TZ(el.timestamp, @@session.time_zone, ?)) BETWEEN 5 AND 7 THEN 'early morning'
                        WHEN HOUR(CONVERT_TZ(el.timestamp, @@session.time_zone, ?)) BETWEEN 8 AND 11 THEN 'morning'
                        WHEN HOUR(CONVERT_TZ(el.timestamp, @@session.time_zone, ?)) BETWEEN 12 AND 16 THEN 'afternoon'
                        WHEN HOUR(CONVERT_TZ(el.timestamp, @@session.time_zone, ?)) BETWEEN 17 AND 20 THEN 'evening'
                        ELSE 'night'
                    END AS time_of_day,
                    el.emotion,
                    COUNT(el.log_id) AS frequency
                FROM emotion_logs el
                WHERE el.firebase_user_id = ?
                GROUP BY time_of_day, el.emotion
            """;

    // Analyze how often the user sends emotions to the device and which emotions are most commonly sent.
    public static final String DEVICE_INTERACTION_ANALYSIS = 
            """
                SELECT el.emotion, COUNT(el.log_id) AS sent_count
                FROM emotion_logs el
                WHERE el.firebase_user_id = ? AND el.send_to_device = TRUE
                GROUP BY el.emotion
                ORDER BY sent_count DESC
            """;
    
    public static final String WEEKLY_PATTERN = 
            """
                SELECT DAYNAME(CONVERT_TZ(el.timestamp, @@session.time_zone, ?)) AS day_of_week,
                el.emotion,
                COUNT(el.log_id) AS frequency
                FROM emotion_logs el
                WHERE el.firebase_user_id = ?
                GROUP BY day_of_week, el.emotion
                ORDER BY FIELD(day_of_week, 'Monday', 'Tuesday', 'Wednesday', 'Thursday', 'Friday', 'Saturday', 'Sunday'),
                frequency DESC
         """;

    public static final String GET_PAST_LOGS = 
        """
            SELECT DATE(CONVERT_TZ(el.timestamp, @@session.time_zone, ?)) AS log_date, 
               el.emotion, COUNT(el.log_id) AS frequency
            FROM emotion_logs el 
            WHERE el.firebase_user_id = ?
		    AND CONVERT_TZ(el.timestamp, @@session.time_zone, ?) >= CURDATE() - INTERVAL ? DAY
            GROUP BY log_date, el.emotion
            ORDER BY log_date ASC, frequency DESC
            
        """;

    
    public static final String WEEKLY_PATTERN_WITH_LOG_ID = 
        """
            SELECT DAYNAME(CONVERT_TZ(el.timestamp, @@session.time_zone, ?)) AS day_of_week,
            el.emotion,
            COUNT(el.log_id) AS frequency,
            JSON_ARRAYAGG(el.log_id) AS log_ids
            FROM emotion_logs el
            WHERE el.firebase_user_id = ?
            AND CONVERT_TZ(el.timestamp, @@session.time_zone, ?) >= CURDATE() - INTERVAL ? DAY
            GROUP BY day_of_week, el.emotion
            ORDER BY FIELD(day_of_week, 'Monday', 'Tuesday', 'Wednesday', 'Thursday', 'Friday', 'Saturday', 'Sunday'),
            frequency DESC
     """;

     public static final String WEEKLY_TIME_OF_DAY_ANALYSIS_WITH_LOG_ID = 
            """
                SELECT CASE
                        WHEN HOUR(CONVERT_TZ(el.timestamp, @@session.time_zone, ?)) BETWEEN 0 AND 4 THEN 'late night'
                        WHEN HOUR(CONVERT_TZ(el.timestamp, @@session.time_zone, ?)) BETWEEN 5 AND 7 THEN 'early morning'
                        WHEN HOUR(CONVERT_TZ(el.timestamp, @@session.time_zone, ?)) BETWEEN 8 AND 11 THEN 'morning'
                        WHEN HOUR(CONVERT_TZ(el.timestamp, @@session.time_zone, ?)) BETWEEN 12 AND 16 THEN 'afternoon'
                        WHEN HOUR(CONVERT_TZ(el.timestamp, @@session.time_zone, ?)) BETWEEN 17 AND 20 THEN 'evening'
                        ELSE 'night'
                    END AS time_of_day,
                    el.emotion,
                    COUNT(el.log_id) AS frequency,
                    JSON_ARRAYAGG(el.log_id) AS log_ids
                FROM emotion_logs el
                WHERE el.firebase_user_id = ?
                AND CONVERT_TZ(el.timestamp, @@session.time_zone, ?) >= CURDATE() - INTERVAL ? DAY
                GROUP BY time_of_day, el.emotion
            """;

    public static final String GET_LOG_BY_ID =
        """
            SELECT log_id, 
                    firebase_user_id,
                    emotion,
                    intensity,
                    CONVERT_TZ(timestamp, @@session.time_zone, ?) AS local_timestamp,
                    send_to_device
             FROM emotion_logs WHERE log_id = ?
             AND firebase_user_id = ?
                
        """;
}
