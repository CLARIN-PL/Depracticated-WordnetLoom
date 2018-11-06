DROP PROCEDURE IF EXISTS add_uuid_column;
DROP PROCEDURE IF EXISTS add_foreign_key;
DROP PROCEDURE IF EXISTS add_uuid_index;
DROP PROCEDURE IF EXISTS generate_uuid_key;
DROP PROCEDURE IF EXISTS move_data;
DROP FUNCTION IF EXISTS UUID_TO_BIN;
DROP FUNCTION IF EXISTS BIN_TO_UUID;

CREATE FUNCTION UUID_TO_BIN(_uuid BINARY(36))
RETURNS BINARY(16)
LANGUAGE SQL DETERMINISTIC CONTAINS SQL SQL SECURITY INVOKER
RETURN
UNHEX(CONCAT(
SUBSTR(_uuid, 15, 4),
SUBSTR(_uuid, 10, 4),
SUBSTR(_uuid, 1, 8),
SUBSTR(_uuid, 20, 4),
SUBSTR(_uuid, 25) ));

CREATE FUNCTION BIN_TO_UUID(_bin BINARY(16))
RETURNS BINARY(36)
LANGUAGE SQL DETERMINISTIC CONTAINS SQL SQL SECURITY INVOKER
RETURN
LCASE(CONCAT_WS('-',
HEX(SUBSTR(_bin, 5, 4)),
HEX(SUBSTR(_bin, 3, 2)),
HEX(SUBSTR(_bin, 1, 2)),
HEX(SUBSTR(_bin, 9, 2)),
HEX(SUBSTR(_bin, 11))
));

DELIMITER $$

CREATE PROCEDURE add_uuid_column(IN t VARCHAR(255),IN n VARCHAR(255))
BEGIN
	SET @q = CONCAT("ALTER TABLE ", t, " ADD COLUMN ", n, " BINARY(16)");
    PREPARE stmt FROM @q;
    EXECUTE stmt;
    DEALLOCATE PREPARE stmt;
END;
$$

CREATE PROCEDURE add_uuid_index(IN tab VARCHAR(255))
BEGIN
    SET @q = CONCAT("CREATE INDEX ", tab, "_uuid_index ON ", tab, "(uuid)");
    PREPARE stmt FROM @q;
    EXECUTE stmt;
    DEALLOCATE PREPARE stmt;
END;
$$

CREATE PROCEDURE add_foreign_key(IN tab VARCHAR(255),IN field VARCHAR(255), IN reference_tab VARCHAR(255), IN reference_field VARCHAR(255), IN con VARCHAR(255))
BEGIN
    SET @q = CONCAT("ALTER TABLE ", tab, " ADD CONSTRAINT ", con, " FOREIGN KEY (", field, ") REFERENCES ", reference_tab, "(", reference_field, ")");
    PREPARE stmt FROM @q;
    EXECUTE stmt;
    DEALLOCATE PREPARE stmt;
END;
$$

CREATE PROCEDURE generate_uuid_key(IN tab VARCHAR(255))
BEGIN
    SET @q = CONCAT("UPDATE ", tab, " SET uuid = UUID_TO_BIN(UUID())");
    PREPARE stmt FROM @q;
    EXECUTE stmt;
    DEALLOCATE PREPARE stmt;
END;
$$

CREATE PROCEDURE move_data(IN tab1 VARCHAR(255), IN tab2 VARCHAR(255), IN old_field VARCHAR(255), IN new_field VARCHAR(255))
BEGIN
    SET @q = CONCAT("UPDATE ", tab1, " A JOIN ", tab2, " B ON A.", old_field, "=B.id SET A.", new_field, "=B.uuid");
    PREPARE stmt FROM @q;
    EXECUTE stmt;
    DEALLOCATE PREPARE stmt;
END;
$$

DELIMITER ;

CALL add_uuid_column('emotional_annotations', 'sense_fk');
CALL add_uuid_column('relation_tests', 'relation_type_fk');
CALL add_uuid_column('relation_type', 'uuid');
CALL add_uuid_column('relation_type', 'parent_relation_fk');
CALL add_uuid_column('relation_type', 'reverse_relation_fk');
CALL add_uuid_column('relation_type_allowed_lexicons', 'relation_type_fk');
CALL add_uuid_column('relation_type_allowed_parts_of_speech', 'relation_type_fk');
CALL add_uuid_column('sense', 'uuid');
CALL add_uuid_column('sense', 'synset_fk');
CALL add_uuid_column('sense', 'word_fk');
CALL add_uuid_column('sense_attributes', 'sense_fk');
CALL add_uuid_column('sense_examples', 'sense_attribute_fk');
CALL add_uuid_column('sense_relation', 'child_sense_fk');
CALL add_uuid_column('sense_relation', 'parent_sense_fk');
CALL add_uuid_column('sense_relation', 'relation_type_fk');
CALL add_uuid_column('synset', 'uuid');
CALL add_uuid_column('synset_attributes', 'synset_fk');
CALL add_uuid_column('synset_examples', 'synset_attributes_fk');
CALL add_uuid_column('synset_relation', 'child_synset_fk');
CALL add_uuid_column('synset_relation', 'parent_synset_fk');
CALL add_uuid_column('synset_relation', 'relation_type_fk');
CALL add_uuid_column('word', 'uuid');



CALL add_uuid_column('tracker_relation_type', 'uuid');
CALL add_uuid_column('tracker_relation_type', 'parent_relation_fk');
CALL add_uuid_column('tracker_relation_type', 'reverse_relation_fk');
CALL add_uuid_column('tracker_relation_type_allowed_lexicons', 'relation_type_fk');
CALL add_uuid_column('tracker_relation_type_allowed_parts_of_speech', 'relation_type_fk');
CALL add_uuid_column('tracker_sense', 'uuid');
CALL add_uuid_column('tracker_sense', 'synset_fk');
CALL add_uuid_column('tracker_sense', 'word_fk');;
CALL add_uuid_column('tracker_sense_attributes', 'sense_fk');
CALL add_uuid_column('tracker_sense_examples', 'sense_attribute_fk');
CALL add_uuid_column('tracker_sense_relation', 'child_sense_fk');
CALL add_uuid_column('tracker_sense_relation', 'parent_sense_fk');
CALL add_uuid_column('tracker_sense_relation', 'relation_type_fk');
CALL add_uuid_column('tracker_synset', 'uuid');
CALL add_uuid_column('tracker_synset_attributes', 'synset_fk');
CALL add_uuid_column('tracker_synset_examples', 'uuid');
CALL add_uuid_column('tracker_synset_examples', 'synset_attributes_fk');
CALL add_uuid_column('tracker_synset_relation', 'child_synset_fk');
CALL add_uuid_column('tracker_synset_relation', 'parent_synset_fk');
CALL add_uuid_column('tracker_synset_relation', 'relation_type_fk');
CALL add_uuid_column('tracker_word', 'uuid');


CALL add_uuid_index('relation_type');
CALL add_uuid_index('sense');
CALL add_uuid_index('synset');
CALL add_uuid_index('word');


CALL add_foreign_key('emotional_annotations', 'sense_fk', 'sense', 'uuid', 'FK_emotional_annotation_sense');
CALL add_foreign_key('relation_tests', 'relation_type_fk', 'relation_type', 'uuid', 'FK_relation_test_relation_type');
CALL add_foreign_key('relation_type', 'parent_relation_fk', 'relation_type', 'uuid', 'FK_relation_type_parent');
CALL add_foreign_key('relation_type', 'reverse_relation_fk', 'relation_type', 'uuid', 'FK_relation_type_reverse');
CALL add_foreign_key('relation_type_allowed_lexicons', 'relation_type_fk', 'relation_type', 'uuid', 'FK_relation_type_allowed_lexicons_relation');
CALL add_foreign_key('relation_type_allowed_parts_of_speech', 'relation_type_fk', 'relation_type', 'uuid', 'FK_relation_type_allowed_pos_relation');
CALL add_foreign_key('sense', 'synset_fk', 'synset', 'uuid', 'FK_sense_synset');
CALL add_foreign_key('sense', 'word_fk', 'word', 'uuid', 'FK_sense_word');
CALL add_foreign_key('sense_attributes', 'sense_fk', 'sense', 'uuid', 'FK_sense_attributes_sense');
CALL add_foreign_key('sense_examples', 'sense_attribute_fk', 'sense', 'uuid', 'FK_sense_examples_attribute');
CALL add_foreign_key('sense_relation', 'child_sense_fk', 'sense', 'uuid', 'FK_sense_relation_child');
CALL add_foreign_key('sense_relation', 'parent_sense_fk', 'sense', 'uuid', 'FK_sense_relation_parent');
CALL add_foreign_key('sense_relation', 'relation_type_fk', 'relation_type', 'uuid', 'FK_sense_relation_relation');
CALL add_foreign_key('synset_attributes', 'synset_fk', 'synset', 'uuid', 'FK_synset_attributes_synset');
CALL add_foreign_key('synset_examples', 'synset_attributes_fk', 'synset', 'uuid', 'FK_synset_examples_attribute');
CALL add_foreign_key('synset_relation', 'child_synset_fk', 'synset', 'uuid', 'FK_synset_relation_child');
CALL add_foreign_key('synset_relation', 'parent_synset_fk', 'synset', 'uuid', 'FK_synset_relation_parent');
CALL add_foreign_key('synset_relation', 'relation_type_fk', 'relation_type', 'uuid', 'FK_synset_relation_relation');

SET SQL_SAFE_UPDATES = 0;

CALL generate_uuid_key('relation_type');
CALL generate_uuid_key('sense');
CALL generate_uuid_key('synset');
CALL generate_uuid_key('word');


SET SQL_SAFE_UPDATES = 0;
SET FOREIGN_KEY_CHECKS = 0;

CALL move_data('emotional_annotations','sense', 'sense_id', 'sense_fk');
CALL move_data('relation_tests','relation_type', 'relation_type_id', 'relation_type_fk');
CALL move_data('relation_type','relation_type', 'parent_relation_type_id', 'parent_relation_fk');
CALL move_data('relation_type','relation_type', 'reverse_relation_type_id', 'reverse_relation_fk');
CALL move_data('relation_type_allowed_lexicons','relation_type', 'relation_type_id', 'relation_type_fk');
CALL move_data('relation_type_allowed_parts_of_speech','relation_type', 'relation_type_id', 'relation_type_fk');
CALL move_data('sense','synset', 'synset_id', 'synset_fk');
CALL move_data('sense','word', 'word_id', 'word_fk');
CALL move_data('sense_attributes','sense', 'sense_id', 'sense_fk');
CALL move_data('sense_examples','sense', 'sense_attribute_id', 'sense_attribute_fk');
CALL move_data('sense_relation','sense', 'child_sense_id', 'child_sense_fk');
CALL move_data('sense_relation','sense', 'parent_sense_id', 'parent_sense_fk');
CALL move_data('sense_relation','relation_type', 'relation_type_id', 'relation_type_fk');
CALL move_data('synset_attributes','synset', 'synset_id', 'synset_fk');
-- CALL move_data('synset_relation','synset', 'child_synset_id', 'child_synset_fk');
-- CALL move_data('synset_relation','synset', 'parent_synset_id', 'parent_synset_fk');
CALL move_data('synset_relation','relation_type', 'synset_relation_type_id', 'relation_type_fk');
UPDATE synset_relation S
SET child_synset_fk = (SELECT uuid FROM synset WHERE id = S.child_synset_id);

UPDATE synset_relation S
SET parent_synset_fk = (SELECT uuid FROM synset WHERE id = S.parent_synset_id);


SET FOREIGN_KEY_CHECKS = 1;
SET SQL_SAFE_UPDATES = 1;


ALTER TABLE emotional_annotations DROP FOREIGN KEY FKj3d2urv1wi643wzxcvefrewfdsvc;
ALTER TABLE relation_type_allowed_lexicons DROP FOREIGN KEY FK1te1f64fg0gdrsp8whnxsk5ux;
ALTER TABLE relation_type_allowed_parts_of_speech DROP FOREIGN KEY FK5ynuaw5d0qyhywfxj0u8vxuylzxd;
ALTER TABLE relation_tests DROP FOREIGN KEY FK404q0976re9p6m0h1oyanxhnd;
ALTER TABLE relation_type DROP FOREIGN KEY FK6bdgdngxm2rl0vium1q98i9c1;
ALTER TABLE relation_type DROP FOREIGN KEY FK8k2lma1x3l6nm7rm7rjxjx3a9;
ALTER TABLE word DROP FOREIGN KEY FK98i2qhqmrcfki79ul7ua8tup7;
ALTER TABLE sense DROP FOREIGN KEY FKk1w1bikgc6pcqsm4v5jbnahdq;
ALTER TABLE sense DROP FOREIGN KEY FK98i2qhqmrcfki79ul7ua8tup7;
ALTER TABLE sense_attributes DROP FOREIGN KEY FKjevbefuvttet3sb4u1h8h4gys;
ALTER TABLE sense_examples DROP FOREIGN KEY FK8vf5o4pb6dmm3jmy1npt7snxe;
ALTER TABLE sense_relation DROP FOREIGN KEY FKprx8p7wb6h19eavxc1wjvnbhf;
ALTER TABLE sense_relation DROP FOREIGN KEY FKk682ashm51g6a7u4unytrt1ic;
ALTER TABLE sense_relation DROP FOREIGN KEY FKddrqi5c2vnofp8wdrbsgcw3ct;
ALTER TABLE synset_attributes DROP FOREIGN KEY FKlru0bqxvyea356fr15w2wdu7i;
ALTER TABLE synset_examples DROP FOREIGN KEY FK3po12pm1bqwwgq9ejvlrvg4sx;
ALTER TABLE synset_relation DROP FOREIGN KEY FKhcndh5xtn9k4pcrjb8ur9e1oy;
ALTER TABLE synset_relation DROP FOREIGN KEY FK4q4yini7xmac0dojilalv3l6j;
ALTER TABLE synset_relation DROP FOREIGN KEY FKj3d2urv1wi643w7y6ovlei5q;


ALTER TABLE relation_type CHANGE id id BIGINT(20) NOT NULL;
ALTER TABLE sense CHANGE id id BIGINT(20) NOT NULL;
ALTER TABLE synset CHANGE id id BIGINT(20) NOT NULL;
ALTER TABLE word CHANGE id id BIGINT(20) NOT NULL;

ALTER TABLE relation_type DROP PRIMARY KEY, ADD PRIMARY KEY (uuid);
ALTER TABLE sense DROP PRIMARY KEY, ADD PRIMARY KEY (uuid);
ALTER TABLE sense_attributes DROP PRIMARY KEY, ADD PRIMARY KEY (sense_fk);
ALTER TABLE synset DROP PRIMARY KEY, ADD PRIMARY KEY (uuid);
ALTER TABLE synset_attributes DROP PRIMARY KEY, ADD PRIMARY KEY (synset_fk);
ALTER TABLE word DROP PRIMARY KEY, ADD PRIMARY KEY (uuid);

DROP PROCEDURE IF EXISTS add_uuid_column;
DROP PROCEDURE IF EXISTS add_foreign_key;
DROP PROCEDURE IF EXISTS add_uuid_index;
DROP PROCEDURE IF EXISTS generate_uuid_key;
DROP PROCEDURE IF EXISTS move_data;




