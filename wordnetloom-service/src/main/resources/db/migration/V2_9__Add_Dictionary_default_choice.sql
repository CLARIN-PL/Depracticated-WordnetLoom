ALTER TABLE wordnet.dictionaries
ADD is_default BIT DEFAULT 0;

ALTER TABLE wordnet.tracker_dictionaries
Add is_default BIT;
