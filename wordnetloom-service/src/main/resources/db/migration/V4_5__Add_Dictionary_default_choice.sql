ALTER TABLE wordnet.dictionaries
ADD is_default BIT DEFAULT 0;

ALTER TABLE wordnet.tracker_dictionaries
Add is_default BIT;


update wordnet.dictionaries set is_default = 1 WHERE id = 12;