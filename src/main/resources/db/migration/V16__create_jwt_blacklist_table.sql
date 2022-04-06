drop table if exists shop.jwt_blacklist CASCADE;

create table shop.jwt_blacklist
(
    id    SERIAL,
    token VARCHAR NOT NULL UNIQUE,
    primary key (id)
);