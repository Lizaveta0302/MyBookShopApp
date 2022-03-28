drop table if exists shop.users CASCADE;

create table shop.users
(
    id       SERIAL,
    name     VARCHAR NOT NULL,
    email    VARCHAR,
    phone    VARCHAR,
    password VARCHAR NOT NULL,
    primary key (id)
);