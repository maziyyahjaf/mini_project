DROP DATABASE IF EXISTS moods;

CREATE DATABASE moods;
USE moods;

CREATE TABLE devices(
    device_id VARCHAR(8) NOT NULL, --  unique device identifier
    is_available BOOLEAN DEFAULT TRUE,

    CONSTRAINT pk_device_id primary key(device_id)
);

-- track pre-linked devices
CREATE TABLE device_pairings(
    id INT NOT NULL AUTO_INCREMENT,
    device1_id VARCHAR(8) NOT NULL, -- First device
    device2_id VARCHAR(8) NOT NULL,
    
    CONSTRAINT pk_id PRIMARY KEY (id),
    CONSTRAINT fk_device1_id FOREIGN KEY (device1_id) REFERENCES devices(device_id),
    CONSTRAINT fk_device2_id FOREIGN KEY (device2_id) REFERENCES devices(device_id),
    CONSTRAINT unique_device_pair UNIQUE (device1_id, device2_id) -- Ensures no duplicate pairs

);

CREATE TABLE users (
    firebase_user_id VARCHAR(200) NOT NULL, -- this will be firebase uuid instead?
    name VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    device_id VARCHAR(8) NOT NULL,
    paired_device_id VARCHAR(8) NOT NULL,
    is_paired BOOLEAN DEFAULT FALSE,
    telegram_chat_id VARCHAR(50) UNIQUE,
    telegram_link_code VARCHAR(10) UNIQUE,
    timezone VARCHAR(100),
    pairing_id VARCHAR(30),

    CONSTRAINT pk_firebase_user_id primary key(firebase_user_id),
    CONSTRAINT fk_device FOREIGN KEY (device_id) REFERENCES devices(device_id) ON DELETE RESTRICT,
    CONSTRAINT fk_paired_device FOREIGN KEY (paired_device_id) REFERENCES devices(device_id) ON DELETE RESTRICT,
    CONSTRAINT unique_user_device UNIQUE (device_id)

    
);

CREATE TABLE emotion_logs(
    log_id INT NOT NULL auto_increment,
    firebase_user_id VARCHAR(200) NOT NULL, -- user who logs the emotion
    emotion VARCHAR(50) NOT NULL,
    intensity INT DEFAULT 3 CHECK (intensity BETWEEN 1 AND 5), -- intensity affects brightness/vibration
    timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    send_to_device BOOLEAN DEFAULT FALSE,
    notes TEXT NULL,

    CONSTRAINT pk_log_id primary key(log_id),
    CONSTRAINT fk_firebase_user_id foreign key(firebase_user_id) REFERENCES users(firebase_user_id) ON DELETE CASCADE
);

CREATE INDEX idx_emotion_logs_user_id ON emotion_logs(firebase_user_id); -- Fast lookup by user_id
CREATE INDEX idx_users_device_id ON users(device_id);


-- Add Foreign Key for Paired User AFTER Creating Users Table
-- ALTER TABLE users ADD CONSTRAINT fk_paired_user FOREIGN KEY (paired_user_id) REFERENCES users(user_id) ON DELETE SET NULL;

grant all privileges on moods.* to 'fred'@'%';

