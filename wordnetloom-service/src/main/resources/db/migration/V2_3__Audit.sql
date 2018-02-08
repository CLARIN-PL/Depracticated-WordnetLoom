create table REVINFO (
  REV integer  NOT NULL AUTO_INCREMENT,
  REVTSTMP bigint,
  primary key (REV)
);

create table tracker_dictionaries (
  id bigint not null,
  REV integer not null,
  dtype varchar(31) not null,
  REVTYPE tinyint,
  REVEND integer,
  description_id bigint,
  name_id bigint,
  primary key (id, REV)
);

create table tracker_domain (
  id bigint not null,
  REV integer not null,
  REVTYPE tinyint,
  REVEND integer,
  description_id bigint,
  name_id bigint,
  primary key (id, REV)
);

create table tracker_lexicon (
  id bigint not null,
  REV integer not null,
  REVTYPE tinyint,
  REVEND integer,
  identifier varchar(255),
  language_name varchar(255),
  lexicon_version varchar(255),
  name varchar(255),
  primary key (id, REV)
);

create table tracker_part_of_speech (
  id bigint not null,
  REV integer not null,
  REVTYPE tinyint,
  REVEND integer,
  color varchar(255),
  name_id bigint,
  primary key (id, REV)
);

create table tracker_relation_type (
  id bigint not null,
  REV integer not null,
  REVTYPE tinyint,
  REVEND integer,
  auto_reverse bit default 0,
  color varchar(255),
  description_id bigint,
  display_text_id bigint,
  multilingual bit default 0,
  name_id bigint,
  node_position varchar(255),
  relation_argument varchar(255),
  short_display_text_id bigint,
  parent_relation_type_id bigint,
  reverse_relation_type_id bigint,
  primary key (id, REV)
);

create table tracker_relation_type_allowed_lexicons (
  REV integer not null,
  relation_type_id bigint not null,
  lexicon_id bigint not null,
  REVTYPE tinyint,
  REVEND integer,
  primary key (REV, relation_type_id, lexicon_id)
);

create table tracker_relation_type_allowed_parts_of_speech (
  REV integer not null,
  relation_type_id bigint not null,
  part_of_speech_id bigint not null,
  REVTYPE tinyint,
  REVEND integer,
  primary key (REV, relation_type_id, part_of_speech_id)
);

create table tracker_sense (
  id bigint not null,
  REV integer not null,
  REVTYPE tinyint,
  REVEND integer,
  synset_position int default 0,
  variant int default 1,
  domain_id bigint,
  lexicon_id bigint,
  part_of_speech_id bigint,
  synset_id bigint,
  word_id bigint,
  primary key (id, REV)
);

create table tracker_sense_attributes (
  sense_id bigint not null,
  REV integer not null,
  REVTYPE tinyint,
  REVEND integer,
  comment text,
  definition text,
  error_comment varchar(255),
  link varchar(255),
  aspect_id bigint,
  user_id bigint,
  register_id bigint,
  primary key (sense_id, REV)
);

create table tracker_sense_examples (
  id bigint not null,
  REV integer not null,
  REVTYPE tinyint,
  REVEND integer,
  example varchar(255),
  type varchar(255),
  sense_attribute_id bigint,
  primary key (id, REV)
);

create table tracker_sense_relation (
  id bigint not null,
  REV integer not null,
  REVTYPE tinyint,
  REVEND integer,
  child_sense_id bigint,
  parent_sense_id bigint,
  relation_type_id bigint,
  primary key (id, REV)
);

create table tracker_synset (
  id bigint not null,
  REV integer not null,
  REVTYPE tinyint,
  REVEND integer,
  abstract boolean,
  split integer,
  lexicon_id bigint,
  status_id bigint,
  primary key (id, REV)
);

create table tracker_synset_attributes (
  synset_id bigint not null,
  REV integer not null,
  REVTYPE tinyint,
  REVEND integer,
  comment text,
  definition text,
  error_comment text,
  ili_id varchar(255),
  princeton_id varchar(255),
  owner_id bigint,
  primary key (synset_id, REV)
);

create table tracker_synset_examples (
  id bigint not null,
  REV integer not null,
  REVTYPE tinyint,
  REVEND integer,
  example varchar(255),
  synset_attribute_id bigint,
  primary key (id, REV)
);

create table tracker_synset_relation (
  id bigint not null,
  REV integer not null,
  REVTYPE tinyint,
  REVEND integer,
  child_synset_id bigint,
  parent_synset_id bigint,
  synset_relation_type_id bigint,
  primary key (id, REV)
);

create table tracker_users (
  id bigint not null,
  REV integer not null,
  REVTYPE tinyint,
  REVEND integer,
  email varchar(255),
  firstname varchar(255),
  lastname varchar(255),
  primary key (id, REV)
);

create table tracker_word (
  id bigint not null,
  REV integer not null,
  REVTYPE tinyint,
  REVEND integer,
  word varchar(255),
  primary key (id, REV)
);

alter table tracker_dictionaries
  add constraint FK6i6p4bbfsy06tmi2e7j7ylm3r
foreign key (REV)
references REVINFO(REV);

alter table tracker_dictionaries
  add constraint FKf2cve2ha4ugdo8ixdy4385g6x
foreign key (REVEND)
references REVINFO(REV);

alter table tracker_domain
  add constraint FKnt6sndy6khd9no3dhjktqi59y
foreign key (REV)
references REVINFO(REV);

alter table tracker_domain
  add constraint FK4gecdehc64qaxt9w3es3ug4t4
foreign key (REVEND)
references REVINFO(REV);

alter table tracker_lexicon
  add constraint FKmhxlbs3hwmjisuc8yej1pd9ei
foreign key (REV)
references REVINFO(REV);

alter table tracker_lexicon
  add constraint FKny5yqbp681v0kwrcj9ydf3ra3
foreign key (REVEND)
references REVINFO(REV);

alter table tracker_part_of_speech
  add constraint FKgm5vtes85590hcgp4b6uyi59f
foreign key (REV)
references REVINFO(REV);

alter table tracker_part_of_speech
  add constraint FKdhqh2050fotkj23drtda8ajul
foreign key (REVEND)
references REVINFO(REV);

alter table tracker_relation_type
  add constraint FKalxuxhcudxqx2rp0m8l5s2ngf
foreign key (REV)
references REVINFO(REV);

alter table tracker_relation_type
  add constraint FKq9vved38e4qagt7w3h1yw9rpg
foreign key (REVEND)
references REVINFO(REV);

alter table tracker_relation_type_allowed_lexicons
  add constraint FKededrfamajkwtt7w9d5a599wg
foreign key (REV)
references REVINFO(REV);

alter table tracker_relation_type_allowed_lexicons
  add constraint FKig74q4lve0ku9rx59mps4qeh3
foreign key (REVEND)
references REVINFO(REV);

alter table tracker_relation_type_allowed_parts_of_speech
  add constraint FKb1xvmr0r2npb90g6g39cou8h6
foreign key (REV)
references REVINFO(REV);

alter table tracker_relation_type_allowed_parts_of_speech
  add constraint FKcllplaw90f6gqu59k92pu1j53
foreign key (REVEND)
references REVINFO(REV);

alter table tracker_sense
  add constraint FKb3ucdyys4lu93clokjmhjrptq
foreign key (REV)
references REVINFO(REV);

alter table tracker_sense
  add constraint FKj6mp679d0c5hj0c58nnhs1kx9
foreign key (REVEND)
references REVINFO(REV);

alter table tracker_sense_attributes
  add constraint FK9c7fscejfmwpsqqt7i86tu6mp
foreign key (REV)
references REVINFO(REV);

alter table tracker_sense_attributes
  add constraint FKkwap3fks8fvnb0l7if6palmwg
foreign key (REVEND)
references REVINFO(REV);

alter table tracker_sense_examples
  add constraint FKqwa0mawyvljkuylsw3jkdjigp
foreign key (REV)
references REVINFO(REV);

alter table tracker_sense_examples
  add constraint FK7nj8dqmd27fa9pu9yfo59uf55
foreign key (REVEND)
references REVINFO(REV);

alter table tracker_sense_relation
  add constraint FKqvleynwpmvu26pklqxyx9jxqs
foreign key (REV)
references REVINFO(REV);

alter table tracker_sense_relation
  add constraint FKobohk2lwfltkbdgoyqdx8fkc3
foreign key (REVEND)
references REVINFO(REV);

alter table tracker_synset
  add constraint FK90mwt2pgoad0ci1qacl849ndd
foreign key (REV)
references REVINFO(REV);

alter table tracker_synset
  add constraint FKahoaah0fadhf6fu05sh9r3w8t
foreign key (REVEND)
references REVINFO(REV);

alter table tracker_synset_relation
  add constraint FKmr7e4u61lgkyfj1ahyqu2plm5
foreign key (REV)
references REVINFO(REV);

alter table tracker_synset_relation
  add constraint FKqvxad592194m0x6y80xr3234k
foreign key (REVEND)
references REVINFO(REV);

alter table tracker_users
  add constraint FK19jg2m1fslgl7bnk4ldo2avx0
foreign key (REV)
references REVINFO(REV);

alter table tracker_users
  add constraint FK1r41ohhhyy0tx30vlx43spoft
foreign key (REVEND)
references REVINFO(REV);

alter table tracker_word
  add constraint FKx8a182a6i7djvjn4mvgjofbx
foreign key (REV)
references REVINFO(REV);

alter table tracker_word
  add constraint FKnott61u5l21aqb7k10lv6xsk9
  foreign key (REVEND)
references REVINFO(REV);

alter table tracker_synset_examples
  add constraint FKap91opmqd9na1kut4uetkdb4n
foreign key (REVEND)
references REVINFO(REV);