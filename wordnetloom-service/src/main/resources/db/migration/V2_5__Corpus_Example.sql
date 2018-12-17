INSERT INTO corpus_example(word, text)
  SELECT lemma, text  FROM wordnet_work.usage_examples;

CREATE INDEX word_index
  ON corpus_example (word);