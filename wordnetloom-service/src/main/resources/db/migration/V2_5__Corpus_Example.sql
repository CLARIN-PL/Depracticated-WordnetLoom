INSERT INTO wordnet.corpus_example(word, text)
  SELECT lemma, text  FROM wordnet_work.usage_examples;

CREATE INDEX word_index
  ON wordnet.corpus_example (word);