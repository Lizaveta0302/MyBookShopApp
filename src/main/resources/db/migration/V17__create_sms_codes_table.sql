drop table if exists shop.sms_keys CASCADE;

create table shop.sms_keys
(
    id          BIGSERIAL,
    code        VARCHAR NOT NULL,
    expire_time TIMESTAMP,
    primary key (id)
);