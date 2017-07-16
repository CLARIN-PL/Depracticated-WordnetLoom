DROP TABLE IF EXISTS `localised`;

CREATE TABLE localised (
    id bigint NOT NULL AUTO_INCREMENT,
    primary key (id)
);

DROP TABLE IF EXISTS `localised_strings`;

CREATE TABLE localised_strings (
    id bigint not null,
    strings varchar(255),
    strings_KEY varchar(255) not null,
    primary key (id, strings_KEY)
);

DROP TABLE IF EXISTS `lexicon`;

CREATE TABLE lexicon (
    id bigint NOT NULL AUTO_INCREMENT,
    identifier varchar(255) not null COMMENT 'Short identifiactor representing lexicon',
    language_name varchar(255) not null COMMENT 'Language of lexion',
     name varchar(255) not null COMMENT 'Lexicon name',
    primary key (id)
);

DROP TABLE IF EXISTS `domain`;

CREATE TABLE domain (
    id bigint NOT NULL AUTO_INCREMENT,
    description_id bigint,
    name_id bigint,
    primary key (id)
);

DROP TABLE IF EXISTS `part_of_speech`;

CREATE TABLE part_of_speech (
    id bigint NOT NULL AUTO_INCREMENT,
    name_id bigint,
    primary key (id)
);

DROP TABLE IF EXISTS `dictionaries`;

CREATE TABLE dictionaries (
    dtype varchar(31) not null,
    id bigint NOT NULL AUTO_INCREMENT,
    description_id bigint,
    name_id bigint,
    primary key (id)
);

DROP TABLE IF EXISTS `corpus_example`;

CREATE TABLE corpus_example (
    id bigint NOT NULL AUTO_INCREMENT,
    text text,
    word bigint not null,
    primary key (id)
);

DROP TABLE IF EXISTS `users`;

CREATE TABLE users (
    id bigint NOT NULL AUTO_INCREMENT,
    email varchar(255) not null,
    firstname varchar(255) not null,
    lastname varchar(255) not null,
    password varchar(64) not null,
    role varchar(255),
    primary key (id)
)