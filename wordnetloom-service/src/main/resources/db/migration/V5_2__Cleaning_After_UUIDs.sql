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
CHANGE COLUMN sense_fk sense_id BINARY(16);

ALTER TABLE sense_emotions
CHANGE COLUMN annotation_fk annotation_id BINARY(16);

ALTER TABLE sense_valuations
CHANGE COLUMN annotation_fk annotation_id BINARY(16);

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



