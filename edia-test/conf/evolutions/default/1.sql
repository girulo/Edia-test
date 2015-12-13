# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table document (
  id                        bigint auto_increment not null,
  title                     varchar(255),
  description               varchar(255),
  constraint pk_document primary key (id))
;




# --- !Downs

SET FOREIGN_KEY_CHECKS=0;

drop table document;

SET FOREIGN_KEY_CHECKS=1;

