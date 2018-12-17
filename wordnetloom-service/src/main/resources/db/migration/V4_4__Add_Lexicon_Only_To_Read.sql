ALTER TABLE wordnet.lexicon
ADD onlyToRead BIT DEFAULT 0;

ALTER TABLE wordnet.tracker_lexicon
Add onlyToRead BIT;