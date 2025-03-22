USE moods;

CREATE TABLE hugEvents (
    hug_event_id INT NOT NULL auto_increment,
    pairing_id VARCHAR(30) NOT NULL,
    timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT pk_hug_event_id PRIMARY KEY (hug_event_id),
    INDEX idx_pairing (pairing_id),
    INDEX idx_timestamp (timestamp),
    INDEX idx_pairing_timestamp (pairing_id, timestamp)

);