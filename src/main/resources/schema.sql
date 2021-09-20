CREATE SCHEMA IF NOT EXISTS shop;
DROP TABLE IF EXISTS shop.authors;
DROP TABLE IF EXISTS shop.books;

CREATE TABLE shop.authors
(
    author_id  BIGSERIAL PRIMARY KEY,
    first_name VARCHAR(50),
    last_name  VARCHAR(50)
);

CREATE TABLE shop.books
(
    id        BIGSERIAL PRIMARY KEY,
    title     VARCHAR(250) NOT NULL,
    price_old VARCHAR(250) DEFAULT NULL,
    price     VARCHAR(250) DEFAULT NULL,
    author_id BIGSERIAL,
    FOREIGN KEY (author_id) REFERENCES shop.authors (author_id)
);