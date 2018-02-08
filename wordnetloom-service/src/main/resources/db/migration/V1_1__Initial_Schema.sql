CREATE TABLE application_localised_string (
  id       BIGINT       NOT NULL AUTO_INCREMENT,
  value    TEXT,
  language VARCHAR(255) NOT NULL,
  PRIMARY KEY (id, language)
);

CREATE TABLE lexicon (
  id              BIGINT       NOT NULL AUTO_INCREMENT,
  identifier      VARCHAR(255) NOT NULL
  COMMENT 'Short identification string representing lexicon',
  language_name   VARCHAR(255) NOT NULL
  COMMENT 'Language of lexicon',
  name            VARCHAR(255) NOT NULL,
  lexicon_version VARCHAR(255) NOT NULL
  COMMENT 'Lexicon name',
  PRIMARY KEY (id)
);

CREATE TABLE domain (
  id             BIGINT NOT NULL AUTO_INCREMENT,
  description_id BIGINT,
  name_id        BIGINT,
  PRIMARY KEY (id)
)
  COMMENT 'Table describes domain';

CREATE TABLE part_of_speech (
  id      BIGINT NOT NULL AUTO_INCREMENT,
  name_id BIGINT COMMENT 'Name of part of speech',
  color   VARCHAR(255) COMMENT 'Color displayed on visualisation',
  PRIMARY KEY (id)
)
  COMMENT 'Table describes parts of speech';

CREATE TABLE dictionaries (
  dtype          VARCHAR(31) NOT NULL,
  id             BIGINT      NOT NULL AUTO_INCREMENT,
  description_id BIGINT COMMENT 'Dictionary description',
  name_id        BIGINT COMMENT 'Dictionary name',
  tag           VARCHAR(20),
  value         BIGINT,
  PRIMARY KEY (id)
);

CREATE TABLE corpus_example (
  id   BIGINT NOT NULL AUTO_INCREMENT,
  text TEXT,
  word VARCHAR(255),
  PRIMARY KEY (id)
);

CREATE TABLE users (
  id        BIGINT       NOT NULL AUTO_INCREMENT,
  email     VARCHAR(255) NOT NULL,
  firstname VARCHAR(255) NOT NULL,
  lastname  VARCHAR(255) NOT NULL,
  password  VARCHAR(64)  NOT NULL,
  role      VARCHAR(255),
  PRIMARY KEY (id)
);

CREATE TABLE relation_tests (
  dtype                       VARCHAR(31)   NOT NULL,
  id                          BIGINT        NOT NULL AUTO_INCREMENT,
  position                    INT DEFAULT 0 NOT NULL,
  test                        TEXT,
  element_A_part_of_speech_id BIGINT,
  element_B_part_of_speech_id BIGINT,
  relation_type_id            BIGINT        NOT NULL,
  PRIMARY KEY (id)
);

CREATE TABLE sense (
  id                BIGINT        NOT NULL AUTO_INCREMENT,
  synset_position   INTEGER COMMENT 'Position order in synset',
  variant           INT DEFAULT 1 NOT NULL
  COMMENT 'Sense variant number',
  domain_id         BIGINT        NOT NULL
  COMMENT 'Domain Id',
  lexicon_id        BIGINT        NOT NULL
  COMMENT 'Lexicon Id',
  part_of_speech_id BIGINT        NOT NULL
  COMMENT 'Part of speech Id',
  synset_id         BIGINT COMMENT 'Synset Id',
  word_id           BIGINT        NOT NULL,
  status_id         BIGINT,
  PRIMARY KEY (id)
);

CREATE TABLE sense_attributes (
  sense_id      BIGINT NOT NULL,
  comment       TEXT,
  definition    TEXT,
  link          VARCHAR(255),
  register_id   BIGINT,
  aspect_id     BIGINT,
  user_id       BIGINT,
  error_comment TEXT,
  PRIMARY KEY (sense_id)
);

CREATE TABLE sense_examples (
  id       BIGINT      NOT NULL AUTO_INCREMENT,
  sense_attribute_id   BIGINT  NOT NULL,
  example  TEXT,
  type     VARCHAR(30) NOT NULL,
  PRIMARY KEY (id)
);

CREATE TABLE sense_relation (
  id               BIGINT NOT NULL AUTO_INCREMENT,
  child_sense_id   BIGINT NOT NULL,
  parent_sense_id  BIGINT NOT NULL,
  relation_type_id BIGINT NOT NULL,
  PRIMARY KEY (id)
);

CREATE TABLE relation_type (
  id                       BIGINT        NOT NULL AUTO_INCREMENT,
  auto_reverse             BIT DEFAULT 0 NOT NULL
  COMMENT 'On true application will create automatically reversed relation',
  multilingual             BIT DEFAULT 0 NOT NULL
  COMMENT 'Relation between two lexicons',
  description_id           BIGINT,
  display_text_id          BIGINT,
  name_id                  BIGINT,
  parent_relation_type_id  BIGINT,
  relation_argument        VARCHAR(255) COMMENT 'Describes type of relation',
  reverse_relation_type_id BIGINT,
  short_display_text_id    BIGINT COMMENT 'Text displayed on visualisation',
  color                    VARCHAR(255) COMMENT 'Color of displayed relation',
  node_position            VARCHAR(255) COMMENT 'Position in node LEFT,TOP,RIGHT,BOTTOM',
  PRIMARY KEY (id)
);

CREATE TABLE relation_type_allowed_lexicons (
  relation_type_id BIGINT NOT NULL,
  lexicon_id       BIGINT NOT NULL,
  PRIMARY KEY (relation_type_id, lexicon_id)
);

CREATE TABLE relation_type_allowed_parts_of_speech (
  relation_type_id  BIGINT NOT NULL,
  part_of_speech_id BIGINT NOT NULL,
  PRIMARY KEY (relation_type_id, part_of_speech_id)
);

CREATE TABLE synset (
  id         BIGINT NOT NULL AUTO_INCREMENT,
  split      INTEGER COMMENT 'Position of line splitting synset head',
  lexicon_id BIGINT NOT NULL,
  status_id  BIGINT,
  abstract BOOLEAN COMMENT 'is synset abstract',
  PRIMARY KEY (id)
);

CREATE TABLE synset_attributes (
  synset_id     BIGINT NOT NULL,
  comment       TEXT,
  definition    TEXT,
  princeton_id  VARCHAR(255) COMMENT 'External original Princeton Id',
  owner_id      BIGINT COMMENT 'Synset owner',
  error_comment TEXT,
  ili_id        VARCHAR(255) COMMENT 'OMW id',
  PRIMARY KEY (synset_id)
);

CREATE TABLE synset_examples (
  id       BIGINT      NOT NULL AUTO_INCREMENT,
  synset_attributes_id BIGINT NOT NULL,
  example              TEXT,
  PRIMARY KEY (id)
);

CREATE TABLE synset_relation (
  id                      BIGINT NOT NULL AUTO_INCREMENT,
  child_synset_id         BIGINT NOT NULL,
  parent_synset_id        BIGINT NOT NULL,
  synset_relation_type_id BIGINT NOT NULL,
  PRIMARY KEY (id)
);

CREATE TABLE word (
  id   BIGINT       NOT NULL AUTO_INCREMENT,
  word VARCHAR(255) NOT NULL,
  PRIMARY KEY (id)
);

CREATE TABLE word_form (
  id   BIGINT NOT NULL AUTO_INCREMENT,
  form VARCHAR(255),
  tag  VARCHAR(255),
  word VARCHAR(255),
  PRIMARY KEY (id)
);

#ALTER TABLE relation_type_allowed_lexicons
#ADD CONSTRAINT UK_7p9ttfi403pkoiua29qff7vwi UNIQUE (lexicon_id);

ALTER TABLE dictionaries
  ADD CONSTRAINT FKflyxm5y0r293f9s1sv4q7weix
FOREIGN KEY (description_id)
REFERENCES application_localised_string (id);

ALTER TABLE dictionaries
  ADD CONSTRAINT FK11lr8u8vfj0m3dv9hmxpj5653
FOREIGN KEY (name_id)
REFERENCES application_localised_string (id);

ALTER TABLE domain
  ADD CONSTRAINT FKhgtdmfui3wtjng46asuqfa79b
FOREIGN KEY (description_id)
REFERENCES application_localised_string (id);

ALTER TABLE domain
  ADD CONSTRAINT FKilj10y6a5e5wvfxr4otivxy8f
FOREIGN KEY (name_id)
REFERENCES application_localised_string (id);

ALTER TABLE part_of_speech
  ADD CONSTRAINT FKqgj4aq3ne5hjb61eo7gagdngw
FOREIGN KEY (name_id)
REFERENCES application_localised_string (id);

ALTER TABLE relation_tests
  ADD CONSTRAINT FK9q4toynhnsw62qcw0rws2n6ym
FOREIGN KEY (element_A_part_of_speech_id)
REFERENCES part_of_speech (id);

ALTER TABLE relation_tests
  ADD CONSTRAINT FKfx1y4ddftr5baay3du383k9kh
FOREIGN KEY (element_B_part_of_speech_id)
REFERENCES part_of_speech (id);

ALTER TABLE relation_tests
  ADD CONSTRAINT FK404q0976re9p6m0h1oyanxhnd
FOREIGN KEY (relation_type_id)
REFERENCES relation_type (id);

ALTER TABLE sense
  ADD CONSTRAINT FKeuhrtymboieklw932horawdvk
FOREIGN KEY (domain_id)
REFERENCES domain (id);

ALTER TABLE sense
  ADD CONSTRAINT FKa45bf1te6qdk0wu7441xrdhvv
FOREIGN KEY (lexicon_id)
REFERENCES lexicon (id);

ALTER TABLE sense
  ADD CONSTRAINT FKjvdptha3oq3lsr3kt3f04lo5u
FOREIGN KEY (part_of_speech_id)
REFERENCES part_of_speech (id);

ALTER TABLE sense
  ADD CONSTRAINT FKk1w1bikgc6pcqsm4v5jbnahdq
FOREIGN KEY (synset_id)
REFERENCES synset (id);

ALTER TABLE sense
  ADD CONSTRAINT FK98i2qhqmrcfki79ul7ua8tup7
FOREIGN KEY (word_id)
REFERENCES word (id);

ALTER TABLE sense_attributes
  ADD CONSTRAINT FKhnsf3ffqr27ceqnrji9g69hxp
FOREIGN KEY (user_id)
REFERENCES users (id);

ALTER TABLE sense_attributes
  ADD CONSTRAINT FKjevbefuvttet3sb4u1h8h4gys
FOREIGN KEY (sense_id)
REFERENCES sense (id);

ALTER TABLE sense_examples
  ADD CONSTRAINT FK8vf5o4pb6dmm3jmy1npt7snxe
FOREIGN KEY (sense_attribute_id)
REFERENCES sense_attributes (sense_id);

ALTER TABLE sense_relation
  ADD CONSTRAINT FKk682ashm51g6a7u4unytrt1ic
FOREIGN KEY (child_sense_id)
REFERENCES sense (id);

ALTER TABLE sense_relation
  ADD CONSTRAINT FKprx8p7wb6h19eavxc1wjvnbhf
FOREIGN KEY (parent_sense_id)
REFERENCES sense (id);

ALTER TABLE sense_relation
  ADD CONSTRAINT FKddrqi5c2vnofp8wdrbsgcw3ct
FOREIGN KEY (relation_type_id)
REFERENCES relation_type (id);

ALTER TABLE relation_type
  ADD CONSTRAINT FK3qs6td1pvv97n4834gc95s1w
FOREIGN KEY (description_id)
REFERENCES application_localised_string (id);

ALTER TABLE relation_type
  ADD CONSTRAINT FK7nfuf14f6hfcb6goi3bqbqgms
FOREIGN KEY (display_text_id)
REFERENCES application_localised_string (id);

ALTER TABLE relation_type
  ADD CONSTRAINT FKk1msw7t7lxfr5ciyfqpvdncip
FOREIGN KEY (name_id)
REFERENCES application_localised_string (id);

ALTER TABLE relation_type
  ADD CONSTRAINT FK8k2lma1x3l6nm7rm7rjxjx3a9
FOREIGN KEY (parent_relation_type_id)
REFERENCES relation_type (id);

ALTER TABLE relation_type
  ADD CONSTRAINT FK6bdgdngxm2rl0vium1q98i9c1
FOREIGN KEY (reverse_relation_type_id)
REFERENCES relation_type (id);

ALTER TABLE relation_type
  ADD CONSTRAINT FKkd3s4gwfo72pasivl4jvtqnr9
FOREIGN KEY (short_display_text_id)
REFERENCES application_localised_string (id);

ALTER TABLE relation_type_allowed_lexicons
  ADD CONSTRAINT FK5ynuaw5d0qyhywfxj0u8vxuyl
FOREIGN KEY (lexicon_id)
REFERENCES lexicon (id);

ALTER TABLE relation_type_allowed_lexicons
  ADD CONSTRAINT FK1te1f64fg0gdrsp8whnxsk5ux
FOREIGN KEY (relation_type_id)
REFERENCES relation_type (id);

ALTER TABLE relation_type_allowed_parts_of_speech
  ADD CONSTRAINT FK5ynuaw5d0qyhywfxj0u8vxuylzxc
FOREIGN KEY (part_of_speech_id)
REFERENCES part_of_speech (id);

ALTER TABLE relation_type_allowed_parts_of_speech
  ADD CONSTRAINT FK5ynuaw5d0qyhywfxj0u8vxuylzxd
FOREIGN KEY (relation_type_id)
REFERENCES relation_type (id);

ALTER TABLE synset
  ADD CONSTRAINT FKfxflmrbnq64hax2r7gs1gbeuj
FOREIGN KEY (lexicon_id)
REFERENCES lexicon (id);

ALTER TABLE synset
  ADD CONSTRAINT FKfxflmrbnq64hax2r7gs1gzxcc
FOREIGN KEY (status_id)
REFERENCES dictionaries (id);

ALTER TABLE synset_attributes
  ADD CONSTRAINT FKd4daq7s6mjs49n2flpjndk0ob
FOREIGN KEY (owner_id)
REFERENCES users (id);

ALTER TABLE synset_attributes
  ADD CONSTRAINT FKlru0bqxvyea356fr15w2wdu7i
FOREIGN KEY (synset_id)
REFERENCES synset (id);

ALTER TABLE synset_examples
  ADD CONSTRAINT FK3po12pm1bqwwgq9ejvlrvg4sx
FOREIGN KEY (synset_attributes_id)
REFERENCES synset_attributes (synset_id);

ALTER TABLE synset_relation
  ADD CONSTRAINT FK4q4yini7xmac0dojilalv3l6j
FOREIGN KEY (child_synset_id)
REFERENCES synset (id);

ALTER TABLE synset_relation
  ADD CONSTRAINT FKhcndh5xtn9k4pcrjb8ur9e1oy
FOREIGN KEY (parent_synset_id)
REFERENCES synset (id);

ALTER TABLE synset_relation
  ADD CONSTRAINT FKj3d2urv1wi643w7y6ovlei5q
FOREIGN KEY (synset_relation_type_id)
REFERENCES relation_type (id);
