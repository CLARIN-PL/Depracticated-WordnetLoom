ALTER TABLE `wordnet`.`application_labels`
ADD CONSTRAINT labels_unique UNIQUE (label_key, language)