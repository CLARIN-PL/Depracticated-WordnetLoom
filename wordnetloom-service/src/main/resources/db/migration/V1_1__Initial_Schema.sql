CREATE TABLE localised (
    id bigint NOT NULL AUTO_INCREMENT,
    primary key (id)
);

CREATE TABLE localised_strings (
    id bigint not null,
    strings text,
    strings_KEY varchar(255) not null,
    primary key (id, strings_KEY)
);

CREATE TABLE lexicon (
    id bigint NOT NULL AUTO_INCREMENT,
    identifier varchar(255) not null COMMENT 'Short identifiactor representing lexicon',
    language_name varchar(255) not null COMMENT 'Language of lexion',
     name varchar(255) not null COMMENT 'Lexicon name',
    primary key (id)
);

CREATE TABLE domain (
    id bigint NOT NULL AUTO_INCREMENT,
    description_id bigint,
    name_id bigint,
    primary key (id)
);

CREATE TABLE part_of_speech (
    id bigint NOT NULL AUTO_INCREMENT,
    name_id bigint,
    primary key (id)
);

CREATE TABLE dictionaries (
    dtype varchar(31) not null,
    id bigint NOT NULL AUTO_INCREMENT,
    description_id bigint,
    name_id bigint,
    primary key (id)
);

CREATE TABLE corpus_example (
    id bigint NOT NULL AUTO_INCREMENT,
    text text,
    word varchar(255),
    primary key (id)
);

CREATE TABLE users (
    id bigint NOT NULL AUTO_INCREMENT,
    email varchar(255) not null,
    firstname varchar(255) not null,
    lastname varchar(255) not null,
    password varchar(64) not null,
    role varchar(255),
    primary key (id)
);

create table relation_tests (
        dtype varchar(31) not null,
        id bigint  NOT NULL AUTO_INCREMENT,
        position int default 0 not null,
        test text,
        element_A_part_of_speech_id bigint,
        element_B_part_of_speech_id bigint,
        synset_relation_type_id bigint not null,
        sense_relation_type_id bigint not null,
        primary key (id)
);

create table sense (
        id bigint  NOT NULL AUTO_INCREMENT,
        synset_position integer,
        variant int default 1 not null,
        domain_id bigint not null,
        lexicon_id bigint not null,
        part_of_speech_id bigint not null,
        synset_id bigint,
        word_id bigint not null,
        primary key (id)
);

create table sense_attributes (
        sense_id bigint not null,
        comment text,
        definition text,
        link varchar(255),
        register varchar(255),
        user_id bigint,
        primary key (sense_id)
);

create table sense_examples (
       sense_attributes_id bigint not null,
       example text
);

create table sense_relation (
    id bigint  NOT NULL AUTO_INCREMENT,
    child_sense_id bigint not null,
    parent_sense_id bigint not null,
    relation_type_id bigint not null,
    primary key (id)
);

create table sense_relation_type (
    id bigint  NOT NULL AUTO_INCREMENT,
    auto_reverse bit not null,
    description_id bigint,
    display_text_id bigint,
    name_id bigint,
    parent_relation_type_id bigint,
    reverse_relation_type_id bigint,
    short_display_text_id bigint,
    primary key (id)
);

create table sense_relation_type_allowed_lexicons (
        sense_relation_type_id bigint not null,
        lexicon_id bigint not null,
        primary key (sense_relation_type_id, lexicon_id)
);

create table synset (
        id bigint  NOT NULL AUTO_INCREMENT,
        split integer,
        lexicon_id bigint not null,
        primary key (id)
);

create table synset_attributes (
        synset_id bigint not null,
        comment text,
        definition text,
        isAbstract boolean,
        princetonId varchar(255),
        owner_id bigint,
        primary key (synset_id)
);

create table synset_examples (
        synset_attributes_id bigint not null,
        example text
);

create table synset_relation (
        id bigint  NOT NULL AUTO_INCREMENT,
        child_synset_id bigint not null,
        parent_synset_id bigint not null,
        synset_relation_type_id bigint not null,
        primary key (id)
);

create table synset_relation_type (
        id bigint  NOT NULL AUTO_INCREMENT,
        auto_reverse bit not null,
        multilingual bit not null,
        description_id bigint,
        display_text_id bigint,
        name_id bigint,
        parent_relation_type_id bigint,
        reverse_relation_type_id bigint,
        short_display_text_id bigint,
        primary key (id)
);

create table word (
        id bigint  NOT NULL AUTO_INCREMENT,
        word varchar(255) not null,
        primary key (id)
);

create table word_form (
        id bigint  NOT NULL AUTO_INCREMENT,
        form varchar(255),
        tag varchar(255),
        word varchar(255),
        primary key (id)
);

alter table sense_relation_type_allowed_lexicons
        add constraint UK_7p9ttfi403pkoiua29qff7vwi unique (lexicon_id);

alter table dictionaries
        add constraint FKflyxm5y0r293f9s1sv4q7weix
        foreign key (description_id)
        references localised(id);

alter table dictionaries
        add constraint FK11lr8u8vfj0m3dv9hmxpj5653
        foreign key (name_id)
        references localised(id);

alter table domain
        add constraint FKhgtdmfui3wtjng46asuqfa79b
        foreign key (description_id)
        references localised(id);

alter table domain
        add constraint FKilj10y6a5e5wvfxr4otivxy8f
        foreign key (name_id)
        references localised(id);

alter table localised_strings
        add constraint FKr34d31h0g5c9nnltxw9hujkee
        foreign key (id)
        references localised(id);

alter table part_of_speech
        add constraint FKqgj4aq3ne5hjb61eo7gagdngw
        foreign key (name_id)
        references localised(id);

alter table relation_tests
        add constraint FK9q4toynhnsw62qcw0rws2n6ym
        foreign key (element_A_part_of_speech_id)
        references part_of_speech(id);

alter table relation_tests
        add constraint FKfx1y4ddftr5baay3du383k9kh
        foreign key (element_B_part_of_speech_id)
        references part_of_speech(id);

alter table relation_tests
        add constraint FK404q0976re9p6m0h1oyanxhnd
        foreign key (synset_relation_type_id)
        references synset_relation_type(id);

alter table relation_tests
        add constraint FK4ug3nrrgitga1nh2byhks20b9
        foreign key (sense_relation_type_id)
        references sense_relation_type(id);

alter table sense
        add constraint FKeuhrtymboieklw932horawdvk
        foreign key (domain_id)
        references domain(id);

alter table sense
        add constraint FKa45bf1te6qdk0wu7441xrdhvv
        foreign key (lexicon_id)
        references lexicon(id);

alter table sense
        add constraint FKjvdptha3oq3lsr3kt3f04lo5u
        foreign key (part_of_speech_id)
        references part_of_speech(id);

alter table sense
        add constraint FKk1w1bikgc6pcqsm4v5jbnahdq
        foreign key (synset_id)
        references synset(id);

alter table sense
        add constraint FK98i2qhqmrcfki79ul7ua8tup7
        foreign key (word_id)
        references word(id);

alter table sense_attributes
        add constraint FKhnsf3ffqr27ceqnrji9g69hxp
        foreign key (user_id)
        references users(id);

alter table sense_attributes
        add constraint FKjevbefuvttet3sb4u1h8h4gys
        foreign key (sense_id)
        references sense(id);

alter table sense_examples
        add constraint FK8vf5o4pb6dmm3jmy1npt7snxe
        foreign key (sense_attributes_id)
        references sense_attributes(sense_id);

alter table sense_relation
        add constraint FKk682ashm51g6a7u4unytrt1ic
        foreign key (child_sense_id)
        references sense(id);

alter table sense_relation
        add constraint FKprx8p7wb6h19eavxc1wjvnbhf
        foreign key (parent_sense_id)
        references sense(id);

alter table sense_relation
        add constraint FKddrqi5c2vnofp8wdrbsgcw3ct
        foreign key (relation_type_id)
        references sense_relation_type(id);

alter table sense_relation_type
        add constraint FK3qs6td1pvv97n4834gc95s1w
        foreign key (description_id)
        references localised(id);

alter table sense_relation_type
        add constraint FK7nfuf14f6hfcb6goi3bqbqgms
        foreign key (display_text_id)
        references localised(id);

alter table sense_relation_type
        add constraint FKk1msw7t7lxfr5ciyfqpvdncip
        foreign key (name_id)
        references localised(id);

alter table sense_relation_type
        add constraint FK8k2lma1x3l6nm7rm7rjxjx3a9
        foreign key (parent_relation_type_id)
        references sense_relation_type(id);

alter table sense_relation_type
        add constraint FK6bdgdngxm2rl0vium1q98i9c1
        foreign key (reverse_relation_type_id)
        references sense_relation_type(id);

alter table sense_relation_type
        add constraint FKkd3s4gwfo72pasivl4jvtqnr9
        foreign key (short_display_text_id)
        references localised(id);

alter table sense_relation_type_allowed_lexicons
        add constraint FK5ynuaw5d0qyhywfxj0u8vxuyl
        foreign key (lexicon_id)
        references lexicon(id);

alter table sense_relation_type_allowed_lexicons
        add constraint FK1te1f64fg0gdrsp8whnxsk5ux
        foreign key (sense_relation_type_id)
        references synset_relation_type(id);

alter table synset
        add constraint FKfxflmrbnq64hax2r7gs1gbeuj
        foreign key (lexicon_id)
        references lexicon(id);

alter table synset_attributes
        add constraint FKd4daq7s6mjs49n2flpjndk0ob
        foreign key (owner_id)
        references users(id);

alter table synset_attributes
        add constraint FKlru0bqxvyea356fr15w2wdu7i
        foreign key (synset_id)
        references synset(id);

alter table synset_examples
        add constraint FK3po12pm1bqwwgq9ejvlrvg4sx
        foreign key (synset_attributes_id)
        references synset_attributes(synset_id);

alter table synset_relation
        add constraint FK4q4yini7xmac0dojilalv3l6j
        foreign key (child_synset_id)
        references synset(id);

alter table synset_relation
        add constraint FKhcndh5xtn9k4pcrjb8ur9e1oy
        foreign key (parent_synset_id)
        references synset(id);

alter table synset_relation
        add constraint FKj3d2urv1wi643w7y6ovlei5q
        foreign key (synset_relation_type_id)
        references synset_relation_type(id);

alter table synset_relation_type
        add constraint FK704fedwd23gqs7emuy45h1qy0
        foreign key (description_id)
        references localised(id);

alter table synset_relation_type
        add constraint FK5odq6l2tswspy45fkbv7ihrro
        foreign key (display_text_id)
        references localised(id);

alter table synset_relation_type
        add constraint FK5wbynmcne2md0ycq9ti9bx4oc
        foreign key (name_id)
        references localised(id);

alter table synset_relation_type
        add constraint FKrb8b61hqwq931aum1i24quyjc
        foreign key (parent_relation_type_id)
        references synset_relation_type(id);

alter table synset_relation_type
        add constraint FKs6te4p1ihm1y9xbou0xwp67e4
        foreign key (reverse_relation_type_id)
        references synset_relation_type(id);

alter table synset_relation_type
        add constraint FKba1jsinxux39v3ke1wcl2p1bf
        foreign key (short_display_text_id)
        references localised(id);