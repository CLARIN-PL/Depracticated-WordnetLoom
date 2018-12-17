ALTER TABLE tracker_relation_type
  ADD COLUMN `global_wordnet_relation_type` VARCHAR(255) NULL AFTER `relation_argument`;

ALTER TABLE relation_type
  ADD COLUMN `global_wordnet_relation_type` VARCHAR(255) NULL AFTER `relation_argument`;
