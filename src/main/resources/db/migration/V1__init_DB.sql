drop table if exists shop.authors cascade;
drop table if exists shop.balance_transaction cascade;
drop table if exists shop.book2author cascade;
drop table if exists shop.book2genre cascade;
drop table if exists shop.book2user cascade;
drop table if exists shop.book2user_type cascade;
drop table if exists shop.book_file_type cascade;
drop table if exists shop.book_review cascade;
drop table if exists shop.book_review_like cascade;
drop table if exists shop.books cascade;
drop table if exists shop.document cascade;
drop table if exists shop.faq cascade;
drop table if exists shop.file_download cascade;
drop table if exists shop.genre cascade;
drop table if exists shop.message cascade;
drop table if exists shop.user_contact cascade;
drop table if exists shop.users cascade;

create table shop.authors
(
    id         serial not null,
    first_name varchar(255),
    last_name  varchar(255),
    primary key (id)
);

create table shop.balance_transaction
(
    id          serial    not null,
    book_id     INT       NOT NULL,
    description TEXT      NOT NULL,
    time        TIMESTAMP NOT NULL,
    user_id     INT       NOT NULL,
    value       INT       NOT NULL DEFAULT 0,
    primary key (id)
);

create table shop.book2author
(
    id         serial not null,
    author_id  INT    NOT NULL,
    book_id    INT    NOT NULL,
    sort_index INT    NOT NULL DEFAULT 0,
    primary key (id)
);

create table shop.book2genre
(
    id       serial not null,
    book_id  INT    NOT NULL,
    genre_id INT    NOT NULL,
    primary key (id)
);

create table shop.book2user
(
    id      serial    not null,
    book_id INT       NOT NULL,
    time    TIMESTAMP NOT NULL,
    type_id INT       NOT NULL,
    user_id INT       NOT NULL,
    primary key (id)
);

create table shop.book2user_type
(
    id   serial       not null,
    code VARCHAR(255) NOT NULL,
    name VARCHAR(255) NOT NULL,
    primary key (id)
);

create table shop.book_file_type
(
    id          serial       not null,
    description TEXT,
    name        VARCHAR(255) NOT NULL,
    primary key (id)
);

create table shop.book_review
(
    id      serial    not null,
    book_id INT       NOT NULL,
    text    TEXT      NOT NULL,
    time    TIMESTAMP NOT NULL,
    user_id INT       NOT NULL,
    primary key (id)
);

create table shop.book_review_like
(
    id        serial    not null,
    review_id INT       NOT NULL,
    time      TIMESTAMP NOT NULL,
    user_id   INT       NOT NULL,
    value     SMALLINT  NOT NULL,
    primary key (id)
);

create table shop.books
(
    id        serial not null,
    price     varchar(255),
    price_old varchar(255),
    title     varchar(255),
    author_id int4,
    primary key (id)
);

create table shop.document
(
    id         serial       not null,
    slug       VARCHAR(255) NOT NULL,
    sort_index INT          NOT NULL DEFAULT 0,
    text       TEXT         NOT NULL,
    title      VARCHAR(255) NOT NULL,
    primary key (id)
);

create table shop.faq
(
    id         serial       not null,
    answer     TEXT         NOT NULL,
    question   VARCHAR(255) NOT NULL,
    sort_index INT          NOT NULL DEFAULT 0,
    primary key (id)
);

create table shop.file_download
(
    id      serial not null,
    book_id INT    NOT NULL,
    count   INT    NOT NULL DEFAULT 1,
    user_id INT    NOT NULL,
    primary key (id)
);

create table shop.genre
(
    id        serial       not null,
    name      VARCHAR(255) NOT NULL,
    parent_id INT,
    slug      VARCHAR(255) NOT NULL,
    primary key (id)
);

create table shop.message
(
    id      serial       not null,
    email   VARCHAR(255),
    name    VARCHAR(255),
    subject VARCHAR(255) NOT NULL,
    text    TEXT         NOT NULL,
    time    TIMESTAMP    NOT NULL,
    user_id INT,
    primary key (id)
);

create table shop.user_contact
(
    id          serial       not null,
    approved    SMALLINT     NOT NULL,
    code        VARCHAR(255) NOT NULL,
    code_time   TIMESTAMP,
    code_trails INT,
    contact     VARCHAR(255) NOT NULL,
    type        int4,
    user_id     INT          NOT NULL,
    primary key (id)
);

create table shop.users
(
    id       serial       not null,
    balance  INT          NOT NULL,
    hash     VARCHAR(255) NOT NULL,
    name     VARCHAR(255) NOT NULL,
    reg_time TIMESTAMP    NOT NULL,
    primary key (id)
);
alter table shop.books drop constraint if exists book_author_fk;
alter table shop.books
    add constraint book_author_fk foreign key (author_id) references shop.authors;
