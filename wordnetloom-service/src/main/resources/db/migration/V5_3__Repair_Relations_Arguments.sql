SET SQL_SAFE_UPDATES = 0;

UPDATE relation_type C JOIN relation_type P ON C.parent_relation_type_id = P.id
	AND C.relation_argument != P.relation_argument
SET C.relation_argument = P.relation_argument;

SET SQL_SAFE_UPDATES = 1;