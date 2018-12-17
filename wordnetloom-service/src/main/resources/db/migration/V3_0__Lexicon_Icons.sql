ALTER TABLE lexicon ADD COLUMN `icon` VARCHAR(45) NULL AFTER `lexicon_version`;

UPDATE lexicon SET icon ="pl.png"  WHERE id = 1;
UPDATE lexicon SET icon ="en.png"  WHERE id = 2;
UPDATE lexicon SET icon ="en.png"  WHERE id = 3;