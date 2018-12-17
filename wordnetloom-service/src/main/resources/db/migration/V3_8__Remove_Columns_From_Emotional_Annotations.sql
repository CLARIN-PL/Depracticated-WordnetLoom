START TRANSACTION;

ALTER TABLE emotional_annotations
DROP COLUMN emotions,
DROP COLUMN valuations,
DROP COLUMN markedness;

COMMIT;