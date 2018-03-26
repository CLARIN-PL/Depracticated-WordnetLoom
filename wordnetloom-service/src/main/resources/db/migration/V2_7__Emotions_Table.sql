CREATE TABLE sense_emotions (
  id       BIGINT       NOT NULL AUTO_INCREMENT,
  sense_id BIGINT       NOT NULL,
  has_emotional_characteristic BIT DEFAULT 0 NOT NULL,
  super_anotation       BIT DEFAULT 0 NOT NULL,
  emotions              VARCHAR(255),
  valuations            VARCHAR(255),
  markedness            VARCHAR(255),
  example1              VARCHAR(512),
  example2              VARCHAR(512),
  PRIMARY KEY (id)
);

ALTER TABLE sense_emotions
  ADD CONSTRAINT FKj3d2urv1wi643wzxcvefrewfdsvc
FOREIGN KEY (sense_id)
REFERENCES sense (id);

-- Clean up before import
DELETE e FROM wordnet_work.emotion e
  LEFT JOIN wordnet_work.lexicalunit l on l.ID = lexicalunit_id
WHERE l.ID is null;
