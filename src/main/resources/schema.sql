create table if not exists movies(
    id bigint generated by default as identity not null,
    chat_id bigint not null,
    poster_url varchar(500),
    year integer,
    length varchar(128),
    description varchar,
    name_ru varchar(255),
    name_en varchar(255),
    constraint pk_movie primary key (id)
);

create sequence if not exists movies_id_seq as bigint start with 1;

create table if not exists chats(
    id bigint not null,
    last_call timestamp,
    constraint pk_chat primary key (id)
)