ALTER TABLE dictionaries
ADD is_default BIT DEFAULT 0;

ALTER TABLE tracker_dictionaries
Add is_default BIT;


update dictionaries set is_default = 1 WHERE id = 12;