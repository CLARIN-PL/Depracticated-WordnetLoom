ALTER TABLE relation_tests
DROP COLUMN relation_type_id;

ALTER TABLE relation_type
DROP COLUMN parent_relation_type_id,
DROP COLUMN reverse_relation_type_id;

ALTER TABLE relation_type_allowed_lexicons
DROP PRIMARY KEY,
ADD PRIMARY KEY(lexicon_id, relation_type_fk),
DROP COLUMN relation_type_id;

ALTER TABLE relation_type_allowed_parts_of_speech
DROP PRIMARY KEY,
ADD PRIMARY KEY(part_of_speech_id, relation_type_fk),
DROP COLUMN relation_type_id;

ALTER TABLE sense
DROP COLUMN synset_id,
DROP COLUMN word_id;

ALTER TABLE sense_attributes
DROP COLUMN sense_id;

ALTER TABLE sense_examples
DROP COLUMN sense_attribute_id;

ALTER TABLE sense_relation
DROP COLUMN child_sense_id,
DROP COLUMN parent_sense_id,
DROP COLUMN relation_type_id;

ALTER TABLE synset
DROP COLUMN split;

ALTER TABLE synset_attributes
DROP COLUMN synset_id;

ALTER TABLE synset_examples
DROP COLUMN synset_attributes_id;

ALTER TABLE synset_relation
DROP COLUMN child_synset_id,
DROP COLUMN parent_synset_id,
DROP COLUMN synset_relation_type_id;

ALTER TABLE emotional_annotations
DROP COLUMN sense_id;

ALTER TABLE sense_emotions
DROP COLUMN annotation_id;

ALTER TABLE sense_valuations
DROP COLUMN annotation_id;

DROP TABLE temp_domains;
DROP TABLE temp_lexicon;
DROP TABLE temp_relation_type;
DROP TABLE temp_sense_word;

ALTER TABLE emotional_annotations DROP FOREIGN KEY FK_emotional_annotation_sense;
ALTER TABLE relation_tests DROP FOREIGN KEY FK_relation_test_relation_type;
ALTER TABLE relation_type DROP FOREIGN KEY FK_relation_type_parent;
ALTER TABLE relation_type DROP FOREIGN KEY FK_relation_type_reverse;
ALTER TABLE relation_type_allowed_lexicons DROP FOREIGN KEY FK_relation_type_allowed_lexicons_relation;
ALTER TABLE relation_type_allowed_parts_of_speech DROP FOREIGN KEY FK_relation_type_allowed_pos_relation;
ALTER TABLE sense DROP FOREIGN KEY FK_sense_synset;
ALTER TABLE sense DROP FOREIGN KEY FK_sense_word;
ALTER TABLE sense_attributes DROP FOREIGN KEY FK_sense_attributes_sense;
ALTER TABLE sense_examples DROP FOREIGN KEY FK_sense_examples_attribute;
ALTER TABLE sense_relation DROP FOREIGN KEY FK_sense_relation_child;
ALTER TABLE sense_relation DROP FOREIGN KEY FK_sense_relation_parent;
ALTER TABLE sense_relation DROP FOREIGN KEY FK_sense_relation_relation;
ALTER TABLE synset_attributes DROP FOREIGN KEY FK_synset_attributes_synset;
ALTER TABLE synset_examples DROP FOREIGN KEY FK_synset_examples_attribute;
ALTER TABLE synset_relation DROP FOREIGN KEY FK_synset_relation_child;
ALTER TABLE synset_relation DROP FOREIGN KEY FK_synset_relation_parent;
ALTER TABLE synset_relation DROP FOREIGN KEY FK_synset_relation_relation;
ALTER TABLE sense_emotions DROP FOREIGN KEY FK_sense_emotions_emotional_annotations;
ALTER TABLE sense_valuations DROP FOREIGN KEY FK_sense_valuations_emotional_annotations;

# TODO: zmienić nazwę i usunąć w relation_tests
ALTER TABLE relation_tests
CHANGE COLUMN relation_type_fk relation_type_id BINARY(16);

ALTER TABLE relation_type
CHANGE COLUMN id legacy_id BIGINT(20),
CHANGE COLUMN uuid id BINARY(16),
CHANGE COLUMN parent_relation_fk parent_relation_type_id BINARY(16),
CHANGE COLUMN reverse_relation_fk reverse_relation_type_id BINARY(16);

ALTER TABLE relation_type_allowed_lexicons
CHANGE COLUMN relation_type_fk relation_type_id BINARY(16);

ALTER TABLE relation_type_allowed_parts_of_speech
CHANGE COLUMN relation_type_fk relation_type_id BINARY(16);

ALTER TABLE sense
CHANGE COLUMN id legacy_id BIGINT(20),
CHANGE COLUMN uuid id BINARY(16),
CHANGE COLUMN synset_fk synset_id BINARY(16),
CHANGE COLUMN word_fk word_id BINARY(16);

ALTER TABLE sense_relation
CHANGE COLUMN child_sense_fk child_sense_id BINARY(16),
CHANGE COLUMN parent_sense_fk parent_sense_id BINARY(16),
CHANGE COLUMN relation_type_fk relation_type_id BINARY(16);

ALTER TABLE sense_attributes
CHANGE COLUMN sense_fk sense_id BINARY(16);

ALTER TABLE sense_examples
CHANGE COLUMN sense_attribute_fk sense_attribute_id BINARY(16);

ALTER TABLE synset
CHANGE COLUMN id legacy_id BIGINT(20),
CHANGE COLUMN uuid id BINARY(16);

ALTER TABLE synset_relation
CHANGE COLUMN child_synset_fk child_synset_id BINARY(16),
CHANGE COLUMN parent_synset_fk parent_synset_id BINARY(16),
CHANGE COLUMN relation_type_fk synset_relation_type_id BINARY(16);

ALTER TABLE synset_attributes
CHANGE COLUMN synset_fk synset_id BINARY(16);

ALTER TABLE synset_examples
CHANGE COLUMN synset_attributes_fk synset_attribute_id BINARY(16);

ALTER TABLE yiddish_sense_extension
CHANGE COLUMN sense_fk sense_id BINARY(16);

ALTER TABLE word
CHANGE COLUMN id legacy_id BIGINT(20),
CHANGE COLUMN uuid id BINARY(16);

ALTER TABLE emotional_annotations
CHANGE COLUMN id legacy_id BIGINT(20),
CHANGE COLUMN uuid id BINARY(16),
CHANGE COLUMN sense_fk sense_id BINARY(16);

ALTER TABLE sense_emotions
CHANGE COLUMN annotation_fk annotation_id BINARY(16);

ALTER TABLE sense_valuations
CHANGE COLUMN annotation_fk annotation_id BINARY(16);

DELIMITER $$
CREATE PROCEDURE add_foreign_key(IN tab VARCHAR(255),IN field VARCHAR(255), IN reference_tab VARCHAR(255), IN reference_field VARCHAR(255), IN con VARCHAR(255))
BEGIN
    SET @q = CONCAT("ALTER TABLE ", tab, " ADD CONSTRAINT ", con, " FOREIGN KEY (", field, ") REFERENCES ", reference_tab, "(", reference_field, ")");
    PREPARE stmt FROM @q;
    EXECUTE stmt;
    DEALLOCATE PREPARE stmt;
END;
$$
DELIMITER ;

ALTER TABLE relation_tests DROP INDEX FK_relation_test_relation_type;

CALL add_foreign_key('emotional_annotations', 'sense_id', 'sense', 'id', 'FK_emotional_annotation_sense');
CALL add_foreign_key('relation_tests', 'relation_type_id', 'relation_type', 'id', 'FK_relation_test_relation_type');
CALL add_foreign_key('relation_type', 'parent_relation_type_id', 'relation_type', 'id', 'FK_relation_type_parent');
CALL add_foreign_key('relation_type', 'reverse_relation_type_id', 'relation_type', 'id', 'FK_relation_type_reverse');
CALL add_foreign_key('relation_type_allowed_lexicons', 'relation_type_id', 'relation_type', 'id', 'FK_relation_type_allowed_lexicons_relation');
CALL add_foreign_key('relation_type_allowed_parts_of_speech', 'relation_type_id', 'relation_type', 'id', 'FK_relation_type_allowed_pos_relation');
CALL add_foreign_key('sense', 'synset_id', 'synset', 'id', 'FK_sense_synset');
CALL add_foreign_key('sense', 'word_id', 'word', 'id', 'FK_sense_word');
CALL add_foreign_key('sense_attributes', 'sense_id', 'sense', 'id', 'FK_sense_attributes_sense');
CALL add_foreign_key('sense_examples', 'sense_attribute_id', 'sense', 'id', 'FK_sense_examples_attribute');
CALL add_foreign_key('sense_relation', 'child_sense_id', 'sense', 'id', 'FK_sense_relation_child');
CALL add_foreign_key('sense_relation', 'parent_sense_id', 'sense', 'id', 'FK_sense_relation_parent');
CALL add_foreign_key('sense_relation', 'relation_type_id', 'relation_type', 'id', 'FK_sense_relation_relation');
CALL add_foreign_key('synset_attributes', 'synset_id', 'synset', 'id', 'FK_synset_attributes_synset');
CALL add_foreign_key('synset_examples', 'synset_attribute_id', 'synset', 'id', 'FK_synset_examples_attribute');
CALL add_foreign_key('synset_relation', 'child_synset_id', 'synset', 'id', 'FK_synset_relation_child');
CALL add_foreign_key('synset_relation', 'parent_synset_id', 'synset', 'id', 'FK_synset_relation_parent');
CALL add_foreign_key('synset_relation', 'synset_relation_type_id', 'relation_type', 'id', 'FK_synset_relation_relation');
CALL add_foreign_key('sense_emotions', 'annotation_id', 'emotional_annotations', 'id', 'FK_sense_emotions_emotional_annotations');
CALL add_foreign_key('sense_valuations', 'annotation_id', 'emotional_annotations', 'id', 'FK_sense_valuations_emotional_annotations');

DROP PROCEDURE add_foreign_key;
--ALTER TABLE sense_relation
--CHANGE COLUMN id id BIGINT(20),
--DROP PRIMARY KEY,
--DROP COLUMN id,
--ADD PRIMARY KEY (child_sense_id, parent_sense_id, relation_type_id);

--ALTER TABLE synset_relation
--CHANGE COLUMn synset_relation_type_fk relation_type_id BINARY(16);

--ALTER TABLE synset_relation
--CHANGE COLUMN id id BIGINT(20),
--DROP PRIMARY KEY,
--DROP COLUMN id,
--ADD PRIMARY KEY (child_synset_id, parent_synset_id, synset_relation_type_id)



