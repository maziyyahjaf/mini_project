package com.example.maziyyah.light_touch.light_touch.repositories.Queries;

public class EmotionInsightsQueries {
    
    public static final String MOST_FREQUENT_EMOTIONS = 
            """
                SELECT e.emotion_name,
                COUNT(el.log_id) AS frequency
                FROM emotion_logs el
                JOIN emotions e
                ON el.emotion = e.emotion_name
                WHERE el.firebase_user_id = ?
                GROUP BY e.emotion_name
                ORDER BY frequency DESC            
            """;


    public static final String AVG_INTENSITY_EMOTIONS = 
            """
                SELECT e.emotion_name,
                AVG(el.intensity) AS avg_intensity
                FROM emotions_logs el
                JOIN emotions e
                ON el.emotion = e.emotion_name
                WHERE el.firebase_user_id = ?
                GROUP BY e.emotion_name
                ORDER BY avg_intensity DESC
             
            """;

    public static final String DAILY_MOOD_TRENDS = 
            """
                SELECT DATE(el.timestamp) AS log_date, e.emotion_name, COUNT(el.log_id) AS frequency
                FROM emotions_logs el 
                JOIN emotions e
                ON el.emotion = e.emotion_name
                WHERE el.firebase_user_id = ?
                GROUP BY log_date, e.emotion_name
                ORDER BY log_date ASC, frequency DESC
            
            """;

    public static final String TIME_OF_DAY_ANALYSIS = 
            """
                SELECT CASE
                        WHEN HOUR(CONVERT_TZ())
                
            """;
}
