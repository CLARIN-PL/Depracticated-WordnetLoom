INSERT INTO application_localised_string(value, language)
SELECT DISTINCT name, 'pl'
FROM `plwordnet3-prod`.yiddish_dictionaries
WHERE name != '' AND name NOT IN (SELECT value FROM application_localised_string)
UNION
SELECT DISTINCT description, 'pl'
FROM `plwordnet3-prod`.yiddish_dictionaries
WHERE description != '' AND description NOT IN (SELECT value FROM application_localised_string);

# przenoszenie słowników
ALTER TABLE dictionaries
ADD COLUMN temp_id BIGINT(20);

INSERT INTO dictionaries(dtype, temp_id, description_id, name_id)
SELECT D.dtype , D.id AS temp_id, A.id AS description_id, A2.id AS name_id
FROM `plwordnet3-prod`.yiddish_dictionaries D LEFT JOIN application_localised_string A ON D.description = A.value AND A.language = 'pl' AND A.value != ''
LEFT JOIN application_localised_string A2 ON D.name = A2.value AND A2.language = 'pl' AND A2.value != '';

CREATE TABLE temp_lexicon (
	oldID INT,
    newID INT
);

# dodanie nowego leksykonu
INSERT INTO lexicon(identifier, language_name, language_shortcut, name, lexicon_version, confidence_score, onlyToRead)
VALUES('J', 'Yiddish', 'y', 'Yiddish', 1, 1, 0);

INSERT INTO temp_lexicon(oldID, newID)
SELECT 3, LAST_INSERT_ID();

INSERT INTO lexicon(identifier, language_name, language_shortcut, name, lexicon_version, confidence_score, onlyToRead)
VALUES('G', 'German', 'ger', 'Germanet', 11, 1, 0);

INSERT INTO temp_lexicon(oldID, newID)
SELECT 4, LAST_INSERT_ID();

# przeniesienie synsetów yiidish i Germanet
INSERT INTO synset(id, split, lexicon_id, status_id, abstract, uuid)
SELECT SY.id, SY.split,
(SELECT newID FROM temp_lexicon WHERE oldID = id_lexicon),
12, 0, UUID_TO_BIN(UUID())
FROM `plwordnet3-prod`.synset SY
LEFT JOIN `plwordnet3-prod`.sense_to_synset SS ON SY.id = SS.id_synset AND SS.id_sense = (SELECT MIN(id_sense) FROM `plwordnet3-prod`.sense_to_synset WHERE id_synset = SY.id)
LEFT JOIN `plwordnet3-prod`.sense SE ON SS.id_sense = SE.id
WHERE id_lexicon > 2;

# TODO ustawić odpowiedni status i abstract

CREATE TABLE temp_sense_word(
	sense_id INT NOT NULL,
    old_word INT NOT NULL,
    new_word_uuid BINARY(16) NOT NULL,
    new_word INT NOT NULL
);

CREATE INDEX temp_sense_id_index ON temp_sense_word(sense_id);
CREATE INDEX temp_old_word_index ON temp_sense_word(old_word);
CREATE INDEX temp_new_word_index ON temp_sense_word(new_word);
CREATE INDEX temp_new_word_uuid_index ON temp_sense_word(new_word_uuid);


INSERT temp_sense_word(sense_id, old_word, new_word, new_word_uuid)
SELECT DISTINCT S.id, W.id, W2.id, W2.uuid
FROM word W2
JOIN `plwordnet3-prod`.word W ON W.word = W2.word
JOIN `plwordnet3-prod`.sense S ON S.lemma = W.id
WHERE S.id_lexicon > 2;

-- TODO sprawdzić, czy zapytanie zwróci wszystkie dane

INSERT INTO sense(id, synset_position, variant, word_id, word_fk, synset_id, uuid)
SELECT S.id,
SS.sense_index,
sense_number,
new_word,
new_word_uuid,
SS.id_synset,
UUID_TO_BIN(UUID())
FROM `plwordnet3-prod`.sense S
LEFT JOIN `plwordnet3-prod`.sense_to_synset SS ON S.id = SS.id_sense
LEFT JOIN temp_sense_word TSW ON TSW.sense_id = S.id
WHERE S.id_lexicon > 2;

UPDATE sense S JOIN `plwordnet3-prod`.sense SE ON S.id = SE.id JOIN  `plwordnet3-prod`.part_of_speech P ON SE.part_of_speech = P.id
JOIN application_localised_string A ON P.uby_lmf_type = A.value JOIN part_of_speech POS ON POS.name_id = A.id
SET part_of_speech_id = POS.id;

UPDATE sense S JOIN `plwordnet3-prod`.sense SE ON S.id = SE.id JOIN temp_domains TD ON TD.newId = SE.domain
SET domain_id = TD.oldId;

UPDATE sense S JOIN `plwordnet3-prod`.sense SE ON S.id = SE.id JOIN temp_lexicon TL ON TL.oldID = SE.id_lexicon
SET lexicon_id = TL.newID;

# uzupełnianie jednostek (uuidy)
UPDATE sense S JOIN synset SY ON S.synset_id = SY.id
SET S.synset_fk = SY.uuid
WHERE S.synset_fk IS NULL;

# wstawienie brakujących atrybutów
INSERT INTO sense_attributes(sense_id, sense_fk)
SELECT id, uuid FROM sense WHERE id NOT IN (SELECT sense_id FROM sense_attributes);

# dodanie do application_localised_string wartości z relation_type

INSERT INTO application_localised_string(value, language)
SELECT T.text, 'pl' FROM `plwordnet3-prod`.relation_type R LEFT JOIN `plwordnet3-prod`.text T ON R.description = T.id
WHERE T.text NOT IN (SELECT value FROM application_localised_string)
UNION
SELECT T.text, 'pl' FROM `plwordnet3-prod`.relation_type R LEFT JOIN `plwordnet3-prod`.text T ON R.display_text = T.id
WHERE T.text NOT IN (SELECT value FROM application_localised_string)
UNION
SELECT T.text, 'pl' FROM `plwordnet3-prod`.relation_type R LEFT JOIN `plwordnet3-prod`.text T ON R.name = T.id
WHERE T.text NOT IN (SELECT value FROM application_localised_string)
UNION
SELECT T.text, 'pl' FROM `plwordnet3-prod`.relation_type R LEFT JOIN `plwordnet3-prod`.text T ON R.short_display_text = T.id
WHERE T.text NOT IN (SELECT value FROM application_localised_string);

# przerzucenie relacji których nie ma
INSERT INTO relation_type (id, auto_reverse, multilingual, description_id, display_text_id, name_id, short_display_text_id, relation_argument, node_position, uuid)
SELECT
R.id,
auto_reverse,
multilingual,
(SELECT id FROM application_localised_string WHERE value = T1.text AND language = 'pl' LIMIT 1) AS description,
(SELECT id FROM application_localised_string WHERE value = T2.text AND language = 'pl' LIMIT 1) AS display_text,
(SELECT id FROM application_localised_string WHERE value = T3.text AND language = 'pl' LIMIT 1) AS name,
(SELECT id FROM application_localised_string WHERE value = T4.text AND language = 'pl' LIMIT 1) AS short_display_text,
CASE argument_type WHEN 1 THEN 'SYNSET_RELATION' WHEN 0 THEN 'SENSE_RELATION' ELSE NULL END as relation_argument,
'IGNORE' as node_position,
UUID_TO_BIN(UUID()) as uuid
FROM `plwordnet3-prod`.relation_type R
LEFT JOIN `plwordnet3-prod`.text T1 ON R.description = T1.id
LEFT JOIN `plwordnet3-prod`.text T2 ON R.display_text = T2.id
LEFT JOIN `plwordnet3-prod`.text T3 ON R.name = T3.id
LEFT JOIN `plwordnet3-prod`.text T4 ON R.short_display_text = T4.id
WHERE T3.text NOT IN (SELECT ALS.value FROM relation_type RT JOIN application_localised_string ALS ON RT.name_id = ALS.id)
HAVING relation_argument IS NOT NULL;

CREATE TABLE temp_relation_type (
	old_id INT NOT NULL,
    new_uuid BINARY(16) NOT NULL
);

INSERT INTO temp_relation_type(old_id, new_uuid)
SELECT R1.id, R2.uuid
FROM `plwordnet3-prod`.relation_type R1 JOIN relation_type R2 ON R1.id = R2.id JOIN `plwordnet3-prod`.text T1 ON R1.name = T1.id JOIN application_localised_string A ON R2.name_id = A.id
WHERE T1.text = A.value AND A.language = 'pl';

# ustawienie rodziców i relacji odwrotnych
# TODO sprawdzić to jeszcze

UPDATE `plwordnet3-prod`.relation_type R1 LEFT JOIN temp_relation_type T1 ON R1.id = T1.old_id
LEFT JOIN relation_type R2 ON T1.new_uuid = R2.uuid
LEFT JOIN temp_relation_type T2 ON R1.parent = T2.old_id
SET R2.parent_relation_fk = T2.new_uuid,
R2.parent_relation_type_id = T2.old_id
WHERE R2.parent_relation_fk IS NULL;

UPDATE `plwordnet3-prod`.relation_type R1 LEFT JOIN temp_relation_type T1 ON R1.id = T1.old_id
LEFT JOIN relation_type R2 ON T1.new_uuid = R2.uuid
LEFT JOIN temp_relation_type T2 ON R1.reverse = T2.old_id
SET R2.reverse_relation_fk = T2.new_uuid,
R2.reverse_relation_type_id = T2.old_id
WHERE R2.reverse_relation_fk IS NULL;

# wstawienie brakujących atrybutów synsetów
INSERT INTO synset_attributes(synset_id, synset_fk)
SELECT id, uuid FROM synset WHERE id NOT IN (SELECT synset_id FROM synset_attributes);

# przeniesienie relacji jednostek

INSERT INTO sense_relation(child_sense_id, parent_sense_id, relation_type_id)
SELECT sense_from, sense_to, relation
FROM `plwordnet3-prod`.sense_relation SR
WHERE (sense_to, sense_from, relation) NOT IN (SELECT child_sense_id, parent_sense_id, relation_type_id FROM sense_relation)
AND sense_to IN (SELECT id FROM sense)
AND sense_from IN (SELECT id FROM sense)
AND relation IN (SELECT id FROM relation_type);

# aktualizacja uuidów relacji jednostek
UPDATE sense_relation SR JOIN sense S ON SR.child_sense_id = S.id
SET SR.child_sense_fk = S.uuid
WHERE SR.child_sense_fk IS NULL;

UPDATE sense_relation SR JOIN sense S ON SR.parent_sense_id = S.id
SET SR.parent_sense_fk = S.uuid
WHERE SR.parent_sense_fk IS NULL;

UPDATE sense_relation SR JOIN temp_relation_type T ON SR.relation_type_id = T.old_id
SET SR.relation_type_fk = T.new_uuid
WHERE SR.relation_type_fk IS NULL;

#przenoszenie relacji synsetów
INSERT INTO synset_relation(child_synset_id, parent_synset_id, synset_relation_type_id)
SELECT synset_from, synset_to, relation
FROM `plwordnet3-prod`.synset_relation SR
WHERE (synset_to, synset_from, relation) NOT IN (SELECT child_synset_id, parent_synset_id, synset_relation_type_id FROM synset_relation)
AND synset_to IN (SELECT id FROM synset)
AND synset_from IN (SELECT id FROM synset)
AND relation IN (SELECT id FROM relation_type);

# aktualizacja uuidów relacji synsetów
UPDATE synset_relation SR JOIN synset S ON SR.child_synset_id = S.id
SET SR.child_synset_fk = S.uuid
WHERE SR.child_synset_fk IS NULL;

UPDATE synset_relation SR JOIN synset S ON SR.parent_synset_id = S.id
SET SR.parent_synset_fk = S.uuid
WHERE SR.parent_synset_fk IS NULL;

UPDATE synset_relation SR JOIN temp_relation_type T ON SR.synset_relation_type_id = T.old_id
SET SR.relation_type_fk = T.new_uuid
WHERE SR.relation_type_fk IS NULL;

# tworzenie i przenoszenie tabel yiddish

CREATE TABLE yiddish_sense_extension (
	id BIGINT(20) PRIMARY KEY AUTO_INCREMENT,
    sense_fk BINARY(16), # TODO dodać NOT NULL
    comment TEXT,
    context TEXT,
    etymology TEXT,
    spelling_latin VARCHAR(255),
    meaning TEXT,
    variant VARCHAR(255),
    etymological_root VARCHAR(255),
    spelling_yiddish VARCHAR(255),
    spelling_yivo VARCHAR(255),
    attested_id BIGINT(20),
    grammatical_gender_id BIGINT(20),
    lexical_characteristic_id BIGINT(20),
    source_id BIGINT(20),
    status_id BIGINT(20),
    style_id BIGINT(20),
    FOREIGN KEY (sense_fk) REFERENCES sense(uuid),
    FOREIGN KEY (attested_id) REFERENCES dictionaries(id),
    FOREIGN KEY (grammatical_gender_id) REFERENCES dictionaries(id),
    FOREIGN KEY (lexical_characteristic_id) REFERENCES dictionaries(id),
    FOREIGN KEY (source_id) REFERENCES dictionaries(id),
    FOREIGN KEY (status_id) REFERENCES dictionaries(id),
    FOREIGN KEY (style_id) REFERENCES dictionaries(id)
);

INSERT INTO yiddish_sense_extension(id, comment, context, etymology, spelling_latin, meaning, variant, etymological_root, spelling_yiddish, spelling_yivo,
attested_id, grammatical_gender_id, lexical_characteristic_id, source_id, status_id, style_id)
SELECT
id,
CASE comment WHEN '' THEN NULL ELSE comment END,
CASE context WHEN '' THEN NULL ELSE context END,
CASE etymology WHEN '' THEN NULL ELSE etymology END,
CASE spelling_latin WHEN '' THEN NULL ELSE spelling_latin END,
CASE meaning WHEN '' THEN NULL ELSE meaning END,
CASE variant WHEN '' THEN NULL ELSE variant END,
CASE etymological_root WHEN '' THEN NULL ELSE etymological_root END,
CASE spelling_yiddish WHEN '' THEN NULL ELSE spelling_yiddish END,
CASE spelling_yivo WHEN '' THEN NULL ELSE spelling_yivo END,
(SELECT id FROM dictionaries WHERE temp_id = attested_id LIMIT 1) as attested,
(SELECT id FROM dictionaries WHERE temp_id = grammatical_gender_id LIMIT 1) as gramatical_gender,
(SELECT id FROM dictionaries WHERE temp_id = lexical_characteristic_id LIMIT 1) as lexical_characteristic,
(SELECT id FROM dictionaries WHERE temp_id = source_id LIMIT 1) as source,
(SELECT id FROM dictionaries WHERE temp_id = status_id LIMIT 1) as status,
(SELECT id FROM dictionaries WHERE temp_id = style_id LIMIT 1) as style
FROM `plwordnet3-prod`.yiddish_sense_extension
WHERE sense IN (SELECT id FROM `plwordnet3-prod`.sense);

UPDATE yiddish_sense_extension Y1 JOIN `plwordnet3-prod`.yiddish_sense_extension Y2 ON Y1.id = Y2.id JOIN sense S ON Y2.sense = S.id
SET Y1.sense_fk = S.uuid;

# TODO prawdopodobnie będzie trzeba usunać wpisy, gdzie sense_fk IS NULL

CREATE TABLE yiddish_domain(
    id BIGINT(20) PRIMARY KEY AUTO_INCREMENT,
    domain_id BIGINT(20) NOT NULL,
    modifier_id BIGINT(20),
    extension_id BIGINT(20),
    FOREIGN KEY (domain_id) REFERENCES dictionaries(id),
    FOREIGN KEY (modifier_id) REFERENCES dictionaries(id),
    FOREIGN KEY (extension_id) REFERENCES yiddish_sense_extension(id)
);

INSERT INTO yiddish_domain (id, domain_id, modifier_id, extension_id)
SELECT id,
(SELECT id FROM dictionaries WHERE temp_id = domain_id) AS domain,
(SELECT id FROM dictionaries WHERE temp_id = modifier_id) AS modifier,
extension_id
FROM `plwordnet3-prod`.yiddish_domain
WHERE extension_id IN (SELECT id FROM yiddish_sense_extension)
AND domain_id IN (SELECT temp_id FROM dictionaries);


CREATE TABLE yiddish_extension_source (
	id BIGINT(20) PRIMARY KEY AUTO_INCREMENT,
    sense_extension_id BIGINT(20) NOT NULL,
    source_dictionary_id BIGINT(20) NOT NULL,
    FOREIGN KEY (sense_extension_id) REFERENCES yiddish_sense_extension(id),
    FOREIGN KEY (source_dictionary_id) REFERENCES dictionaries(id)
);

INSERT INTO yiddish_extension_source(id, sense_extension_id, source_dictionary_id)
SELECT id,
sense_extension_id,
(SELECT id FROM dictionaries WHERE temp_id = source_dictionary_id)
FROM `plwordnet3-prod`.yiddish_extension_source
WHERE sense_extension_id IN (SELECT id FROM yiddish_sense_extension);

CREATE TABLE yiddish_transcriptions (
	id BIGINT(20) PRIMARY KEY AUTO_INCREMENT,
    phonography VARCHAR(255) NOT NULL,
    transcription_id BIGINT(20) NOT NULL,
    extension_id BIGINT(20),
    FOREIGN KEY (transcription_id) REFERENCES dictionaries(id),
    FOREIGN KEY (extension_id) REFERENCES yiddish_sense_extension(id)
);

# TODO sprawdzić, brakujące sense_extension
INSERT INTO yiddish_transcriptions (id, phonography, transcription_id, extension_id)
SELECT id, phonography,
(SELECT id FROM dictionaries WHERE temp_id = transcription_id),
extension_id
FROM `plwordnet3-prod`.yiddish_transcriptions
WHERE extension_id IN (SELECT id FROM yiddish_sense_extension);

CREATE TABLE yiddish_inflection(
	id BIGINT(20) PRIMARY KEY AUTO_INCREMENT,
    text VARCHAR(255) NOT NULL,
    prefix_id BIGINT(20) NOT NULL,
    extension_id BIGINT(20),
    FOREIGN KEY (prefix_id) REFERENCES dictionaries(id),
    FOREIGN KEY (extension_id) references yiddish_sense_extension(id)
);

INSERT INTO yiddish_inflection(id, text, prefix_id, extension_id)
SELECT id, text,
(SELECT id FROM dictionaries WHERE temp_id = prefix_id LIMIT 1),
extension_id
FROM `plwordnet3-prod`.yiddish_inflection
WHERE extension_id IN (SELECT id FROM yiddish_sense_extension);

CREATE TABLE yiddish_particles(
	id BIGINT(20) PRIMARY KEY AUTO_INCREMENT,
	dtype VARCHAR(31) NOT NULL,
    position INT DEFAULT 0,
    root VARCHAR(255),
    constituent VARCHAR(255),
    extension_id BIGINT(20),
    particle_id BIGINT(20),
    FOREIGN KEY (extension_id) REFERENCES yiddish_sense_extension(id),
    FOREIGN KEY (particle_id) REFERENCES dictionaries(id)
);

INSERT INTO yiddish_particles(id, dtype, position, root, constituent, extension_id, particle_id)
SELECT id, dtype, position, root, constituent, extension_id,
CASE dtype WHEN 'SuffixParticle' THEN (SELECT id FROM dictionaries WHERE temp_id = suffix_id)
	WHEN 'PrefixParticle' THEN (SELECT id FROM dictionaries WHERE temp_id = prefix_id)
    WHEN 'InterfixParticle' THEN (SELECT id FROM dictionaries WHERE temp_id = interfix_id) END
FROM `plwordnet3-prod`.yiddish_particles
WHERE extension_id IN (SELECT id FROM yiddish_sense_extension);

INSERT INTO application_localised_string(id, value, language)
SELECT id, value, 'en'
FROM application_localised_string
WHERE id NOT IN (SELECT id FROM application_localised_string WHERE language= 'en');