START TRANSACTION;

CREATE TABLE unit_emotions (
	annotation_id BIGINT,
    emotion BIGINT,
    PRIMARY KEY(annotation_id, emotion),
    FOREIGN KEY (annotation_id) REFERENCES emotional_annotations(id),
    FOREIGN KEY (emotion) REFERENCES dictionaries(id)
);

CREATE TABLE unit_valuations (
	annotation_id BIGINT,
    valuation BIGINT,
    PRIMARY KEY (annotation_id, valuation),
    FOREIGN KEY (annotation_id) REFERENCES emotional_annotations(id),
    FOREIGN KEY (valuation) REFERENCES dictionaries(id)
);

DELIMITER $$

CREATE TRIGGER unit_emotions_before_insert_trigger
	BEFORE INSERT ON unit_emotions FOR EACH ROW
	BEGIN
		IF (SELECT dtype FROM dictionaries WHERE id = NEW.emotion) <> 'Emotion'
        THEN SIGNAL SQLSTATE '45000'
			SET MESSAGE_TEXT = 'Cannot add or update row: emotion id is incorrect type';
		END IF;
    END;
$$

CREATE TRIGGER unit_valuation_beore_insert_trigger
	BEFORE INSERT ON unit_valuations FOR EACH ROW
    BEGIN
		IF (SELECT dtype FROM dictionaries WHERE id=NEW.valuation) <> 'Valuation'
        THEN SIGNAL SQLSTATE '45000'
			SET MESSAGE_TEXT = 'Cannot add or update row: valuation id is incorrect type';
		END IF;
	END;

$$

DELIMITER ;

ALTER TABLE emotional_annotations
ADD owner BIGINT;

ALTER TABLE emotional_annotations
ADD CONSTRAINT FK_emotional_annotations_users
FOREIGN KEY (owner) REFERENCES users(id);

COMMIT;

