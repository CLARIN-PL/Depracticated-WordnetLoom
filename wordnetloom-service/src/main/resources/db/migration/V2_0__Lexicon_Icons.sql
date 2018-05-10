ALTER TABLE `wordnet`.`lexicon` ADD COLUMN `icon` VARCHAR(45) NULL AFTER `lexicon_version`;

UPDATE `wordnet`.`lexicon` SET icon ="en.png"  WHERE id = 1;
