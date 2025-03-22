USE moods;

CREATE TABLE emotions (
    emotion_id INT AUTO_INCREMENT,
    emotion_name VARCHAR(125) NOT NULL,
    emotion_icon_reference VARCHAR(500),
    display_order INT,

    CONSTRAINT pk_emotion_id PRIMARY KEY (emotion_id)

);