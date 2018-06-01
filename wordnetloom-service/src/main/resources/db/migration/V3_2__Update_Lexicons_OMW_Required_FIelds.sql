ALTER TABLE `wordnet`.`lexicon` DROP COLUMN icon;

ALTER TABLE `wordnet`.`tracker_lexicon`
  ADD COLUMN `language_shortcut` VARCHAR(5) NULL AFTER `language_name`;

ALTER TABLE `wordnet`.`tracker_lexicon`
  ADD COLUMN `license` VARCHAR(255) NULL AFTER `lexicon_version`;

ALTER TABLE `wordnet`.`tracker_lexicon`
  ADD COLUMN `email` VARCHAR(255) NULL AFTER `license`;

ALTER TABLE `wordnet`.`tracker_lexicon`
  ADD COLUMN `reference_url` VARCHAR(255) NULL AFTER `email`;

ALTER TABLE `wordnet`.`tracker_lexicon`
  ADD COLUMN `citation` TEXT NULL AFTER `reference_url`;

ALTER TABLE `wordnet`.`tracker_lexicon`
  ADD COLUMN `confidence_score` VARCHAR(255) NULL AFTER `citation`;

ALTER TABLE `wordnet`.`lexicon`
  ADD COLUMN `language_shortcut` VARCHAR(5) NULL AFTER `language_name`;

ALTER TABLE `wordnet`.`lexicon`
  ADD COLUMN `license` VARCHAR(255) NULL AFTER `lexicon_version`;

ALTER TABLE `wordnet`.`lexicon`
  ADD COLUMN `email` VARCHAR(255) NULL AFTER `license`;

ALTER TABLE `wordnet`.`lexicon`
  ADD COLUMN `reference_url` VARCHAR(255) NULL AFTER `email`;

ALTER TABLE `wordnet`.`lexicon`
  ADD COLUMN `citation` TEXT NULL AFTER `reference_url`;

ALTER TABLE `wordnet`.`lexicon`
  ADD COLUMN `confidence_score` VARCHAR(255) NULL AFTER `citation`;


UPDATE lexicon set language_shortcut = 'pl',
  license='wordnet',
  email='maciej.piasecki@pwr.edu.pl',
  reference_url='http://plwordnet.pwr.edu.pl/wordnet/',
  citation='Maziarz, M., Piasecki, M., Rudnicka, E., Kędzia, P. (2016). “plWordnet 3.0 - a Comprehensive-Lexical Semantic Resource.“ Proceedings of COLING 2016, the 26th International Conference on Computational Linguistics: Technical Papers, Osaka, Japan. Retrieved from http://coling2016.anlp.jp/',
  confidence_score='1' WHERE id = 1;


UPDATE lexicon set language_shortcut = 'en',
  license='wordnet',
  email='plwordnet.pwr.wroc.pl@gmail.com',
  reference_url='http://plwordnet.pwr.edu.pl/wordnet/',
  citation='',
  confidence_score='1' WHERE id = 2;

UPDATE lexicon set language_shortcut = 'en',
  license='wordnet',
  email='ewa.rudnicka@pwr.edu.pl',
  reference_url='http://plwordnet.pwr.edu.pl/wordnet/',
  citation='Rudnicka, E., Witkowski, W., Kaliński M. (2015). “Towards the Extension of Princeton WordNet”. Cognitive Studies 15, 335-351. Retrieved from: https://ispan.waw.pl/journals/index.php/cs-ec/article/download/cs.2015.023/1774',
  confidence_score='1' WHERE id = 3;
