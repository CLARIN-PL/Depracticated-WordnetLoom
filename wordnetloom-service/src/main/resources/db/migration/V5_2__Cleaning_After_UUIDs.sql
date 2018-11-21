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

DROP TABLE temp_domains;
DROP TABLE temp_lexicon;
DROP TABLE temp_relation_type;
DROP TABLE temp_sense_word;

