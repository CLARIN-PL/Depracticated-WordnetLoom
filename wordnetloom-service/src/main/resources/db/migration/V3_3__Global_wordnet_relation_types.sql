ALTER TABLE `wordnet`.`tracker_relation_type`
  ADD COLUMN `global_wordnet_relation_type` VARCHAR(255) NULL AFTER `relation_argument`;

ALTER TABLE `wordnet`.`relation_type`
  ADD COLUMN `global_wordnet_relation_type` VARCHAR(255) NULL AFTER `relation_argument`;
