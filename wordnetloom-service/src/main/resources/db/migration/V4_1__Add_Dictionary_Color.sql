START TRANSACTION;

ALTER TABLE dictionaries
ADD color VARCHAR(7);

SET SQL_SAFE_UPDATES = 0;

UPDATE dictionaries D JOIN application_localised_string A ON D.name_id = A.id
SET D.color = '#000000'
WHERE A.value = 'Nieprzetworzony';

UPDATE dictionaries D JOIN application_localised_string A ON D.name_id = A.id
SET D.color = '#008080'
WHERE A.value = 'Nowy';

UPDATE dictionaries D JOIN application_localised_string A ON D.name_id = A.id
SET D.color = '#800080'
WHERE A.value = 'Błąd';

UPDATE dictionaries D JOIN application_localised_string A ON D.name_id = A.id
SET D.color = '#008000'
WHERE A.value = 'Sprawdzony';

UPDATE dictionaries D JOIN application_localised_string A ON D.name_id = A.id
SET D.color = '#808080'
WHERE A.value = 'Znaczenie';

UPDATE dictionaries D JOIN application_localised_string A ON D.name_id = A.id
SET D.color = '#808000'
WHERE A.value = 'Częściowo przetworzony';

SET SQL_SAFE_UPDATES = 1;

COMMIT;