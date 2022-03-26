drop table if exists shop.book_review CASCADE;

create table shop.book_review
(
    id      BIGSERIAL,
    book_id INT          NOT NULL,
    user_id INT,
    text    VARCHAR NOT NULL,
    time    TIMESTAMP,
    CONSTRAINT book_review_fk
        FOREIGN KEY (book_id) REFERENCES shop.books (id),
    CONSTRAINT user_review_fk
        FOREIGN KEY (user_id) REFERENCES shop.users (id),
    primary key (id)
);
CREATE UNIQUE INDEX user_book_review_unique_index
    ON shop.book_review (book_id, user_id);