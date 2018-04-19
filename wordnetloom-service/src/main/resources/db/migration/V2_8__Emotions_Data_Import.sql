INSERT INTO wordnet.emotional_annotations (id, sense_id, has_emotional_characteristic,super_anotation,emotions,valuations, markedness, example1, example2)
  SELECT id, lexicalunit_id, unitStatus, super_anotation, emotions, valuations, markedness, example1, example2
  FROM wordnet_work.emotion;