USE moods;

CREATE TABLE emotion_logs(
    log_id INT NOT NULL auto_increment,
    firebase_user_id VARCHAR(200) NOT NULL, -- user who logs the emotion
    emotion VARCHAR(50) NOT NULL,
    intensity INT DEFAULT 3 CHECK (intensity BETWEEN 1 AND 5), -- intensity affects brightness/vibration
    timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    send_to_device BOOLEAN DEFAULT FALSE,

    CONSTRAINT pk_log_id primary key(log_id),
    CONSTRAINT fk_firebase_user_id foreign key(firebase_user_id) REFERENCES users(firebase_user_id) ON DELETE CASCADE
);

CREATE INDEX idx_emotion_logs_user_id ON emotion_logs(firebase_user_id); 