ALTER TABLE wordnet_work.lexicalunit
  CONVERT TO CHARACTER SET utf8
  COLLATE utf8_polish_ci;


ALTER TABLE wordnet_work.synset
  CONVERT TO CHARACTER SET utf8
  COLLATE utf8_polish_ci;


ALTER TABLE wordnet_work.relationtype
  CONVERT TO CHARACTER SET utf8
  COLLATE utf8_polish_ci;


#przeniesienie słów. m^2 oraz m^3 nalezy dodać odzielnie, ponieważ DISTINCT traktuje m^2 tak samo jak M2 i wstawia tylko jedną z tych wartości
INSERT INTO word (word)
  SELECT DISTINCT CAST(BINARY lemma AS CHAR CHARACTER SET utf8) COLLATE utf8_bin
  FROM wordnet_work.lexicalunit
  UNION ALL
  SELECT 'm²'
  UNION ALL
  SELECT 'm³';

CREATE TABLE temp_dictionaries(
    id BIGINT NOT NULL PRIMARY KEY AUTO_INCREMENT,
    old_value INT NOT NULL,
    name_id BIGINT NOT NULL,
    dtype VARCHAR(31) NOT NULL,
    tag VARCHAR(20),
    markednessValue BIGINT
);

# dodawanie słowników
DELIMITER $$
DROP PROCEDURE IF EXISTS insert_dictionaries $$
CREATE PROCEDURE insert_dictionaries(IN valuesList VARCHAR(1000), IN typeName VARCHAR(40))
    BEGIN
        SET @counter = 0;
        SET @valuesList = valuesList;
        SET @lastInsertedStringId = 0;
        SET @lastInsertedValueId = null;
        WHILE(LOCATE(',', @valuesList) > 0) DO
            SET @value = SUBSTRING(@valuesList, 1, LOCATE(',', @valuesList) -1);
            SET @optionalValue = null;
            SET @tag = null;
            IF(LOCATE(';', @value) > 0) THEN
                SET @optionalValue = SUBSTRING(@value,LOCATE(';', @value)+1);
                SET @value = SUBSTRING(@value, 1, LOCATE(';', @value) -1);

                SELECT @optionalValue;
            END IF;
            INSERT INTO application_localised_string (value, language)
            VALUES (@value, 'pl');
            SET @lastInsertedStringId = LAST_INSERT_ID();
            INSERT INTO application_localised_string(id, value, language)
            VALUES (@lastInsertedStringId, @value, 'en');

            IF(@optionalValue IS NOT NULL) THEN
                IF(typeName = 'Markedness') THEN
                    INSERT INTO application_localised_string(value, language)
                    VALUES (@optionalValue, 'pl');
                    SET @lastInsertedValueId = LAST_INSERT_ID();
                    INSERT INTO application_localised_string(id, value, language)
                    VALUES (@lastInsertedValueId, @optionalValue, 'en');
                END IF;
                IF (typeName = 'Aspect') THEN
                    SET @tag = @optionalValue;
				END IF;
			END IF;
            INSERT INTO temp_dictionaries (old_value, name_id, dtype, tag, markednessValue)
            VALUES(@counter, @lastInsertedStringId, typeName, @tag, @lastInsertedValueId);

            SET @valuesList = SUBSTRING(@valuesList, LOCATE(',', @valuesList)+1);
            SET @counter = @counter +1;
        END WHILE;
    END $$
DELIMITER ;
CALL insert_dictionaries('og.,daw.,książk.,nienorm.,posp.,pot.,reg.,specj.,środ.,urz.,wulg.,', 'Register');
CALL insert_dictionaries('Nieprzetworzony,Nowy,Błąd,Sprawdzony,Znaczenie,Częściowo przetworzony,', 'Status');
CALL insert_dictionaries('radość,zaufanie,cieszenie się na coś oczekiwanego, zaskoczenie czymś nieprzewidywanym, smutek, złość, strach, wstręt,', 'Emotion');
CALL insert_dictionaries('użyteczność, dobro, prawda, wiedza, piękno, szczęście, nieużyteczność, krzywda, niewiedza, błąd, brzydota, nieszczęście,', 'Valuation');
CALL insert_dictionaries('Wybierz:, amb (niejednoznaczność pod względem nacechowania emocjonalnego;amb,+ m (mocne nacechowanie pozytywne jednostki);+ m, - m (mocne nacechowanie negatywne jednostki);- m,+ s (słabe nacechowanie pozytywne jednostki);+ s,- s (słabe nacechowanie negatywne jednostki);- s,', 'Markedness');
CALL insert_dictionaries('jednostka nie jest czasownikiem;no,aspect dokonany;:perf:,aspekt niedokonany;:imperf:,predykatyw;:pred:,czasownik dwuaspektowy;:imperf.perf;,', 'Aspect');

DROP PROCEDURE IF EXISTS insert_localised_description;

INSERT INTO dictionaries (dtype, id, name_id, tag, value)
SELECT dtype, id, name_id, tag, markednessValue
FROM temp_dictionaries;

# przerzucenie synsetu
# złączenia mają na celu pozbycie się synsetów pustych oraz połączeń synsetów z nieistniejącymi jednostkami
INSERT INTO synset (id, split,abstract, lexicon_id, status_id)
  SELECT DISTINCT
    S.id,
    S.split,
    S.isabstract,
    (SELECT CASE WHEN pos <= 4
      THEN 1
            WHEN LL.project=-1
              THEN 2
            ELSE 3 END AS lexicon
     FROM wordnet_work.lexicalunit LL LEFT JOIN wordnet_work.unitandsynset U ON LL.id = U.LEX_ID
       LEFT JOIN wordnet_work.synset SS ON U.SYN_ID = SS.id
     WHERE SS.id = S.id
     LIMIT 1) AS lexicon,
     (SELECT id FROM temp_dictionaries WHERE old_value = S.status AND dtype = 'Status')
  FROM wordnet_work.synset S LEFT JOIN wordnet_work.unitandsynset U ON S.id = U.SYN_ID
    LEFT JOIN wordnet_work.lexicalunit L ON U.LEX_ID = L.id
  WHERE U.SYN_ID IS NOT NULL AND L.id IS NOT NULL;

CREATE TABLE tempWord(
	id INT PRIMARY KEY,
    bin BLOB, INDEX(bin(255)),
    word TEXT
);

INSERT INTO tempWord(id, bin, word)
SELECT DISTINCT id, BINARY TRIM(TRAILING FROM word), word
FROM word;


# przerzucenie jednostek
INSERT INTO sense (id, synset_position, variant, domain_id, lexicon_id, part_of_speech_id, synset_id, word_id, status_id)
  SELECT
    L.id,
    U.unitindex,
    L.variant,
    L.domain,
    CASE WHEN pos <= 4
      THEN 1
    WHEN L.project = -1
      THEN 2
    ELSE 3 END       AS lexicon,
    CASE WHEN pos <= 4
      THEN pos
    ELSE pos - 4 END AS part_of_speech,
    S.id             AS synset_id,
    (SELECT W.id
     FROM tempWord W
      WHERE W.bin = BINARY (TRIM(TRAILING FROM L.lemma))
     LIMIT 1)        AS word_id,
      (SELECT id FROM temp_dictionaries WHERE old_value = L.status AND dtype = 'Status') AS status
  FROM wordnet_work.lexicalunit L LEFT JOIN wordnet_work.unitandsynset U ON L.id = U.LEX_ID
    LEFT JOIN wordnet_work.synset S ON U.SYN_ID = S.id;

DROP TABLE tempWord;

# changing synset position in english lexicons
UPDATE sense
SET synset_position = synset_position -1
WHERE lexicon_id = 2;

UPDATE sense
SET synset_position = 0
WHERE synset_position < 0;

CREATE TABLE tempSynsetPos(
  id INT PRIMARY KEY,
  pos INT
);

INSERT INTO tempSynsetPos(id, pos)
  SELECT s.synset_id, min(s.synset_position) FROM sense s
  GROUP BY s.synset_id
  HAVING min(s.synset_position) <> 0;

UPDATE sense s
  LEFT JOIN tempSynsetPos tmp on s.synset_id = tmp.id
SET s.synset_position = s.synset_position - tmp.pos
WHERE s.synset_id = tmp.id;

DROP TABLE tempSynsetPos;
# ------------------------------------------

# PRZERZUCANIE UŻYTKOWNIKÓW
#podział na imię i nazwisko
SELECT DISTINCT
  SUBSTRING_INDEX(TRIM(LOWER(owner)), '.', 1),
  SUBSTRING_INDEX(TRIM(LOWER(owner)), '.', -1)
FROM wordnet_work.lexicalunit;

# dodanie użytkowników. Użytkownicy wymagają czyszczenia
INSERT INTO users (email, firstname, lastname, password)
  SELECT DISTINCT
    ''                                                            AS email,
    SUBSTRING_INDEX(LOWER(TRIM(owner)), '.', 1) COLLATE utf8_general_ci  AS firstname,
    SUBSTRING_INDEX(LOWER(TRIM(owner)), '.', -1) COLLATE utf8_general_ci AS lastname,
    ''                                                            AS password
  FROM wordnet_work.lexicalunit
  HAVING firstname != '' AND lastname != ''
  UNION DISTINCT
  SELECT DISTINCT
    ''                                                            AS email,
    SUBSTRING_INDEX(LOWER(TRIM(owner)), '.', 1) COLLATE utf8_general_ci  AS firstname,
    SUBSTRING_INDEX(LOWER(TRIM(owner)), '.', -1) COLLATE utf8_general_ci AS lastname,
    ''                                                            AS password
  FROM wordnet_work.synset
  HAVING firstname != '' AND lastname != '';

# wstawianie atrybutów jednostek
INSERT INTO sense_attributes (sense_id, comment, user_id, error_comment)
  SELECT
    id,
    comment,
    (SELECT id
     FROM users
     WHERE
       SUBSTRING_INDEX(TRIM(LOWER(L.owner)), '.', 1) = firstname AND
       SUBSTRING_INDEX(TRIM(LOWER(L.owner)), '.', -1) = lastname) AS user,
    error_comment
  FROM wordnet_work.lexicalunit L
  WHERE comment != '' AND comment IS NOT NULL;

# wstawianie atrybótów synsetów
# złączenia z synsetem dokonujemy aby wyeliminować atrybuty synsetów pustych, które nie zostały przeniesione do nowej bazy
INSERT INTO synset_attributes (synset_id, comment, definition, owner_id, error_comment)
  SELECT
    S.id,
    S.comment,
    S.definition,
    (SELECT id
     FROM users
     WHERE
       SUBSTRING_INDEX(TRIM(LOWER(S.owner)), '.', 1) = firstname AND
       SUBSTRING_INDEX(TRIM(LOWER(S.owner)), '.', -1) = lastname
     LIMIT 1) AS user,
    error_comment
  FROM wordnet_work.synset S
  JOIN synset SY ON S.id = SY.id;


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
    WHILE @i < @n DO
      SET @s2 = CONCAT(' SELECT DISTINCT ', columnName, ' INTO @t FROM wordnet_work.relationtype LIMIT ?, 1');
      PREPARE statement2 FROM @s2;
      EXECUTE statement2
      USING @i;
      DEALLOCATE PREPARE statement2;

      INSERT INTO application_localised_string (value, language)
      VALUES (@t, 'pl');
      INSERT INTO application_localised_string (id, value, language)
      VALUES (last_insert_id(), @t, 'en');
      # w przypadku pojawienia sie nowych języków wstawic w tym miejscu odpowiednią wartość
      SET @i = @i + 1;
    END WHILE;
  END $$

DELIMITER ;

CALL insert_localised_description('description');
CALL insert_localised_description('display');
CALL insert_localised_description('name');
CALL insert_localised_description('shortcut');

DROP PROCEDURE IF EXISTS insert_localised_description;

#wstawienie typów relacji
INSERT INTO relation_type (id, auto_reverse, multilingual, description_id, display_text_id, name_id, parent_relation_type_id, relation_argument, short_display_text_id, node_position)
  SELECT
    ID,
    autoreverse,
    0                          AS multilingual,
    (SELECT id
     FROM application_localised_string
     WHERE value = R.description
     LIMIT 1)                  AS description,
    (SELECT id
     FROM application_localised_string
     WHERE value = R.display
     LIMIT 1)                  AS display,
    (SELECT id
     FROM application_localised_string
     WHERE value = R.name
     LIMIT 1)                  AS name,
    PARENT_ID,
    CASE WHEN
      CASE WHEN PARENT_ID IS NOT NULL
        THEN (SELECT objecttype
              FROM wordnet_work.relationtype
              WHERE ID = R.PARENT_ID)
      ELSE objecttype END = 0
      THEN 'SENSE_RELATION'
    ELSE 'SYNSET_RELATION' END AS relation_argument,
    (SELECT id
     FROM application_localised_string
     WHERE value = R.shortcut
     LIMIT 1)                  AS short,
    'IGNORE'
  FROM wordnet_work.relationtype R
  WHERE objecttype != 2
  ORDER BY id;

# wstawienie relacji odwrotnych. Relacje odwrotne trzeba wstawić odzielnie, ponieważ klucze obce nie pozwalają tego zrobić podczas wstawiania typów relacji
SET SQL_SAFE_UPDATES = 0;
UPDATE wordnet_work.relationtype RS INNER JOIN relation_type RN ON RS.id = RN.id
SET RN.reverse_relation_type_id = RS.REVERSE_ID;
SET SQL_SAFE_UPDATES = 1;

# dodawanie testów , sprawdzić, czy numery id się zgadzają
# TODO będzie trzeba wyrzucić dtype
INSERT INTO relation_tests (test, element_A_part_of_speech_id, element_B_part_of_speech_id, relation_type_id, position)
  SELECT
    `text`,
    CASE WHEN posA <= 4
      THEN posA
    ELSE posA - 4 END          AS partA,
    CASE WHEN posB = 0
      THEN NULL
    ELSE CASE WHEN posB <= 4
      THEN posB
         ELSE posB - 4 END END AS partB,
    REL_ID,
    `order`
  FROM wordnet_work.test
  WHERE REL_ID IN (SELECT id
                   FROM relation_type);

# dodawanie dozwolonych leksykonów dla poszczególnych typów relacji. Wstawione wartości powinny być przejrzane i poprawione ręcznie
DELIMITER $$
DROP PROCEDURE IF EXISTS insertAllowedLexicons $$
CREATE PROCEDURE insertAllowedLexicons()
  BEGIN
    DECLARE n INT;
    DECLARE i INT;
    DECLARE relID INT;
    SELECT COUNT(1)
    FROM relation_type
    INTO n;
    SET i = 0;
    SET relID = 0;
    START TRANSACTION;
    WHILE i < n DO
      SELECT id
      FROM relation_type
      ORDER BY id
      LIMIT i, 1
      INTO relID;
      SET i = i + 1;
      IF ((relID > 170 AND relID <= 190) OR (relID > 3000 AND relID <= 3002))
      THEN # angielskie
        INSERT INTO relation_type_allowed_lexicons (relation_type_id, lexicon_id)
        VALUES (relID, 2);

      ELSEIF ((relID > 197 AND relID <= 223) OR (relID > 3004 AND relID <= 3016))
        THEN # miedzyjęzykowe
          INSERT INTO relation_type_allowed_lexicons (relation_type_id, lexicon_id)
          VALUES (relID, 1), (relID, 3);
      ELSE
        INSERT INTO relation_type_allowed_lexicons (relation_type_id, lexicon_id)
        VALUES (relID, 1);
      END IF;
    END WHILE;
    COMMIT;
  END $$

DELIMITER ;

CALL insertAllowedLexicons();

DROP PROCEDURE IF EXISTS insertAllowedLexicons;

# dodanie kolumny proper_name, do atrybutów jednostek
ALTER TABLE sense_attributes
ADD COLUMN proper_name BIT DEFAULT 0 NOT NULL;

# wstawienie relacji jednostek i synsetów
# wstawianie relacji jednostek. Sprawdzamy parent oraz child, ponieważ w bazie przechowywane sa relacje do nieistniejących jednostek
# chyba będzie do poprawy. Za pomocą tego wstawiane sa tylko te relacje tych typów, które zostały wcześniej
# dodane do nowej tabeli. Być moze bedzie trzeba to zrobić inaczej
INSERT INTO sense_relation (child_sense_id, parent_sense_id, relation_type_id)
  SELECT
    CHILD_ID,
    PARENT_ID,
    REL_ID
  FROM wordnet_work.lexicalrelation
  WHERE REL_ID IN (SELECT id
                   FROM relation_type)
        AND PARENT_ID IN (SELECT id
                          FROM wordnet_work.lexicalunit)
        AND CHILD_ID IN (SELECT id
                         FROM wordnet_work.lexicalunit);

# wstawianie relacji synsetów
INSERT INTO synset_relation (child_synset_id, parent_synset_id, synset_relation_type_id)
  SELECT
    CHILD_ID,
    PARENT_ID,
    REL_ID
  FROM wordnet_work.synsetrelation
  WHERE PARENT_ID IN (SELECT id
                      FROM synset)
        AND CHILD_ID IN (SELECT id
                         FROM synset);

DROP TABLE temp_dictionaries;
DROP PROCEDURE IF EXISTS insert_dictionaries;