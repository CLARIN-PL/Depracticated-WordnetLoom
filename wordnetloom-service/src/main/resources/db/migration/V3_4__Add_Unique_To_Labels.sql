ALTER TABLE application_labels
ADD CONSTRAINT labels_unique UNIQUE (label_key, language)