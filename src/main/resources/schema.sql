CREATE SCHEMA shop;
DROP TABLE IF EXISTS shop.authors;
DROP TABLE IF EXISTS shop.books;

CREATE TABLE shop.authors
(
    author_id INT AUTO_INCREMENT PRIMARY KEY,
    name      VARCHAR(250) NOT NULL
);

CREATE TABLE shop.books
(
    id        INT AUTO_INCREMENT PRIMARY KEY,
    title     VARCHAR(250) NOT NULL,
    price_old VARCHAR(250) DEFAULT NULL,
    price     VARCHAR(250) DEFAULT NULL,
    author_id INT,
    FOREIGN KEY (author_id) REFERENCES shop.authors (author_id)
);