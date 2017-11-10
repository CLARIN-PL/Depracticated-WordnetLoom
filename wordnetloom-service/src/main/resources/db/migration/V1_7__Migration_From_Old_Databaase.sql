# utowrzenie indeksu na tabeli word
CREATE INDEX word_index ON wordnet.word(word);

#przeniesienie słów. m^2 oraz m^3 nalezy dodać odzielnie, ponieważ DISTINCT traktuje m^2 tak samo jak M2 i wstawia tylko jedną z tych wartości
INSERT INTO wordnet.word(word)
SELECT DISTINCT lemma COLLATE utf8_general_ci
FROM wordnet_work.lexicalunit
UNION ALL
SELECT 'm²'
UNION ALL
SELECT 'm³';

# dodanie kolumny status do jednostki
ALTER TABLE wordnet.sense
ADD COLUMN status INT NOT NULL DEFAULT 0;

#dodanie kolumny status do synsetu
ALTER TABLE wordnet.synset
ADD COLUMN status INT NOT NULL DEFAULT 0;

# dodanie kolumny error_comment do atrybutów jednostki
ALTER TABLE wordnet.sense_attributes
ADD COLUMN error_comment TEXT NULL;

# dodanie kolumny error_comment do atrybutów synsetyu
ALTER TABLE wordnet.synset_attributes
ADD COLUMN error_comment TEXT NULL;

# przerzucenie synsetu
# złączenia mają na celu pozbycie się synsetów pustych oraz połączeń synsetów z nieistniejącymi jednostkami
INSERT INTO wordnet.synset (id, split, lexicon_id, status)
SELECT DISTINCT S.id, S.split,
(SELECT CASE WHEN pos <=4 THEN 1 WHEN S.comment ='' AND S.owner = '' THEN 3 ELSE 2 END AS lexicon
FROM wordnet_work.lexicalunit LL LEFT JOIN wordnet_work.unitandsynset U ON LL.id = U.LEX_ID
LEFT JOIN wordnet_work.synset SS ON U.SYN_ID = SS.id WHERE SS.id = S.id LIMIT 1) AS lexicon,
S.status
FROM wordnet_work.synset S LEFT JOIN wordnet_work.unitandsynset U ON S.id = U.SYN_ID LEFT JOIN wordnet_work.lexicalunit L ON U.LEX_ID = L.id
WHERE U.SYN_ID IS NOT NULL AND L.id IS NOT NULL;

# przerzucenie jednostek
INSERT INTO wordnet.sense (id, synset_position, variant,domain_id, lexicon_id, part_of_speech_id, synset_id, word_id, status)
SELECT L.id,
U.unitindex,
L.variant,
L.domain,
CASE WHEN pos <= 4 THEN 1 WHEN S.comment='' AND S.owner = '' THEN 3 ELSE 2 END AS lexicon,
CASE WHEN pos <= 4 THEN pos ELSE pos - 4 END AS part_of_speech,
S.id AS synset_id,
(SELECT id FROM wordnet.word WHERE word = L.lemma LIMIT 1) AS word_id,
L.status
FROM wordnet_work.lexicalunit L LEFT JOIN wordnet_work.unitandsynset U ON L.id = U.LEX_ID
LEFT JOIN wordnet_work.synset S ON U.SYN_ID = S.id;

# PRZERZUCANIE UŻYTKOWNIKÓW
#podział na imię i nazwisko
SELECT DISTINCT SUBSTRING_INDEX(TRIM(owner), '.', 1),
SUBSTRING_INDEX(TRIM(owner), '.', -1)
FROM wordnet_work.lexicalunit;

# dodanie użytkowników. Użytkownicy wymagają czyszczenia
INSERT INTO wordnet.users(email,firstname, lastname, password)
SELECT DISTINCT '' AS email,
SUBSTRING_INDEX(TRIM(owner), '.', 1) COLLATE utf8_general_ci AS firstname,
SUBSTRING_INDEX(TRIM(owner), '.', -1) COLLATE utf8_general_ci AS lastname,
'' AS password
FROM wordnet_work.lexicalunit
HAVING firstname != '' AND lastname != ''
UNION DISTINCT
SELECT DISTINCT '' AS email,
SUBSTRING_INDEX(TRIM(owner), '.', 1) COLLATE utf8_general_ci AS firstname,
SUBSTRING_INDEX(TRIM(owner), '.',-1) COLLATE utf8_general_ci AS lastname,
'' AS password
FROM wordnet_work.synset
HAVING firstname != '' AND lastname != '';

# wstawianie atrybutów jednostek
INSERT INTO wordnet.sense_attributes (sense_id, comment, user_id, error_comment)
SELECT id, comment,
(SELECT id FROM wordnet.users WHERE
SUBSTRING_INDEX(TRIM(L.owner), '.', 1) = firstname AND
SUBSTRING_INDEX(TRIM(L.owner), '.', -1) = lastname) AS user,
error_comment
FROM wordnet_work.lexicalunit L
WHERE comment != '' AND comment IS NOT NULL;

# wstawianie atrybótów synsetów
# złączenia z synsetem dokonujemy aby wyeliminować atrybuty synsetów pustych, które nie zostały przeniesione do nowej bazy
INSERT INTO wordnet.synset_attributes(synset_id, comment,abstract, owner_id, error_comment)
SELECT S.id, comment,
isabstract,
(SELECT id FROM wordnet.users WHERE
SUBSTRING_INDEX(TRIM(S.owner),'.', 1) = firstname AND
SUBSTRING_INDEX(TRIM(S.owner),'.', -1) = lastname LIMIT 1) AS user,
error_comment
FROM wordnet_work.synset S JOIN wordnet.synset SY ON S.id = SY.id
WHERE comment != '' AND comment IS NOT NULL;


# dodawanie tekstów opisu
DELIMITER $$
DROP PROCEDURE IF EXISTS insert_localised_description$$
CREATE PROCEDURE insert_localised_description(IN columnName VARCHAR(50))
BEGIN
#DECLARE i INT DEFAULT 0;
SET @s1 = CONCAT('SELECT COUNT(DISTINCT ', columnName, ') FROM wordnet_work.relationtype INTO @n');
PREPARE statement1 FROM @s1;
EXECUTE statement1;
DEALLOCATE PREPARE statement1;
SET @i = 0;
WHILE @i<@n DO
	SET @s2 = CONCAT(' SELECT DISTINCT ', columnName, ' INTO @t FROM wordnet_work.relationtype LIMIT ?, 1');
    PREPARE statement2 FROM @s2;
	EXECUTE statement2 USING @i;
    DEALLOCATE PREPARE statement2;

    INSERT INTO wordnet.application_localised_string(value, language)
    VALUES (@t, 'pl');
    INSERT INTO wordnet.application_localised_string(id, value, language)
    VALUES (last_insert_id, @t, 'en');
    # w przypadku pojawienia sie nowych języków wstawic w tym miejscu odpowiednią wartość
	SET @i = @i +1;
END WHILE;
END $$

DELIMITER ;

CALL insert_localised_description('description');
CALL insert_localised_description('display');
CALL insert_localised_description('name');
CALL insert_localised_description('shortcut');

DROP PROCEDURE IF EXISTS insert_localised_description;

#wstawienie typów relacji
INSERT INTO wordnet.relation_type(id, auto_reverse, multilingual,description_id, display_text_id, name_id, parent_relation_type_id, relation_argument, short_display_text_id, node_position)
SELECT ID,
autoreverse,
0 AS multilingual,
(SELECT id FROM wordnet.application_localised_string WHERE value = R.description LIMIT 1) AS description,
(SELECT id FROM wordnet.application_localised_string WHERE value = R.display LIMIT 1) AS display,
(SELECT id FROM wordnet.application_localised_string WHERE value = R.name LIMIT 1) AS name,
PARENT_ID,
CASE WHEN
CASE WHEN PARENT_ID IS NOT NULL THEN (SELECT objecttype FROM wordnet_work.relationtype WHERE ID = R.PARENT_ID) ELSE objecttype END = 0
THEN 'SYNSET_RELATION' ELSE 'SENSE_RELATION' END AS relation_argument,
(SELECT id FROM wordnet.application_localised_string WHERE value = R.shortcut LIMIT 1) AS short,
'IGNORE'
FROM wordnet_work.relationtype R
WHERE objecttype != 2
ORDER BY id;

# wstawienie relacji odwrotnych. Relacje odwrotne trzeba wstawić odzielnie, ponieważ klucze obce nie pozwalają tego zrobić podczas wstawiania typów relacji
SET SQL_SAFE_UPDATES = 0;
UPDATE wordnet_work.relationtype RS INNER JOIN wordnet.relation_type RN ON RS.id = RN.id
SET RN.reverse_relation_type_id = RS.REVERSE_ID;
SET SQL_SAFE_UPDATES =1;

# aktualizacja tabeli przykładów, dodanie do niej kolumny z id oraz kolumny z typem przykładu
ALTER TABLE wordnet.sense_examples
ADD COLUMN id INT PRIMARY KEY AUTO_INCREMENT;

ALTER TABLE wordnet.sense_examples
ADD COLUMN type VARCHAR(30) NOT NULL;

# dodawanie testów , sprawdzić, czy numery id się zgadzają
# TODO będzie trzeba wyrzucić dtype
INSERT INTO wordnet.relation_tests(dtype,test, element_A_part_of_speech_id, element_B_part_of_speech_id, relation_type_id, position)
SELECT '',`text`,
CASE WHEN posA <= 4 THEN posA ELSE posA - 4 END AS partA,
CASE WHEN posB = 0 THEN NULL ELSE CASE WHEN posB <= 4 THEN posB ELSE posB - 4 END END AS partB,
REL_ID, `order`
FROM wordnet_work.test
WHERE REL_ID IN (SELECT id FROM wordnet.relation_type);

# dodawanie dozwolonych leksykonów dla poszczególnych typów relacji. Wstawione wartości powinny być przejrzane i poprawione ręcznie
DELIMITER $$
DROP PROCEDURE IF EXISTS insertAllowedLexicons $$
CREATE PROCEDURE insertAllowedLexicons()
BEGIN
DECLARE n INT;
DECLARE i INT;
DECLARE relID INT;
SELECT COUNT(1) FROM wordnet.relation_type INTO n;
SET i = 0;
SET relID = 0;
START TRANSACTION;
WHILE i < n DO
	SELECT id FROM wordnet.relation_type ORDER BY id LIMIT i, 1  INTO relID;
    SET i = i + 1;
    IF((relID > 170 AND relID <= 190) OR (relID >3000 AND relID <= 3002)) THEN # angielskie
		INSERT INTO wordnet.relation_type_allowed_lexicons (relation_type_id, lexicon_id)
        VALUES (relID, 2);

	ELSEIF ((relID > 197 AND relID <= 223) OR (relID > 3004 AND relID <= 3016)) THEN # miedzyjęzykowe
		INSERT INTO wordnet.relation_type_allowed_lexicons(relation_type_id, lexicon_id)
        VALUES(relID, 1),(relID, 3);
	ELSE
		INSERT INTO wordnet.relation_type_allowed_lexicons(relation_type_id, lexicon_id)
        VALUES(relID, 1);
	END IF;
END WHILE;
COMMIT;
END $$

DELIMITER ;

CALL insertAllowedLexicons();

DROP PROCEDURE IF EXISTS insertAllowedLexicons;

# dodanie i wypełnienie tabeli register_types. Tabela będzie potrzebna do parsowania komentarzy
CREATE TABLE wordnet.register_types
(
    id INT PRIMARY KEY AUTO_INCREMENT,
    name_id BIGINT NOT NULL UNIQUE
);

ALTER TABLE wordnet.register_types
ADD CONSTRAINT fk_register_types_localised
FOREIGN KEY (name_id) REFERENCES wordnet.application_localised_string(id);

DELIMITER $$
DROP PROCEDURE IF EXISTS insert_register_types$$
CREATE PROCEDURE insert_register_types()
BEGIN
SET @registers = 'og.,daw.,książk.,nienorm.,posp.,pot.,reg.,specj.,środ.,urz.,wulg.,';
WHILE(LOCATE(',', @registers) > 0) DO
	SET @value = SUBSTRING(@registers, 1, LOCATE(',', @registers) -1);
    SET @last_insert_id = LAST_INSERT_ID();
    INSERT INTO wordnet.application_localised_string(value, language)
    VALUES(@value, 'pl');
    SET @last_insert_id = LAST_INSERT_ID();
    INSERT INTO wordnet.application_localised_string(id, value, language)
    VALUES(@last_insert_id, @value, 'en');
    INSERT INTO wordnet.register_types(name_id)
    VALUES(@last_insert_id);
    SET @registers = SUBSTRING(@registers, LOCATE(',', @registers) +1);
END WHILE;
END $$

DELIMITER ;

CALL insert_register_types;

DROP PROCEDURE insert_register_types;

# dodanie kolumny proper_name, do atrybutów jednostek
ALTER TABLE wordnet.sense_attributes
ADD COLUMN proper_name BIT DEFAULT 0 NOT NULL;

# wstawienie relacji jednostek i synsetów
# wstawianie relacji jednostek. Sprawdzamy parent oraz child, ponieważ w bazie przechowywane sa relacje do nieistniejących jednostek
# chyba będzie do poprawy. Za pomocą tego wstawiane sa tylko te relacje tych typów, które zostały wcześniej
# dodane do nowej tabeli. Być moze bedzie trzeba to zrobić inaczej
INSERT INTO wordnet.sense_relation(child_sense_id, parent_sense_id, relation_type_id)
SELECT CHILD_ID, PARENT_ID, REL_ID
FROM wordnet_work.lexicalrelation
WHERE REL_ID IN (SELECT id FROM wordnet.relation_type)
AND PARENT_ID IN (SELECT id FROM wordnet_work.lexicalunit)
AND CHILD_ID IN (SELECT id FROM wordnet_work.lexicalunit);

# wstawianie relacji synsetów
INSERT INTO wordnet.synset_relation(child_synset_id, parent_synset_id, synset_relation_type_id)
SELECT CHILD_ID, PARENT_ID, REL_ID
FROM wordnet_work.synsetrelation
WHERE PARENT_ID IN (SELECT id FROM wordnet.synset)
AND CHILD_ID IN (SELECT id FROM wordnet.synset);