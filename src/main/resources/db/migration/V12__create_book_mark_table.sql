drop table if exists shop.book_mark;

create table shop.book_mark
(
    id      BIGSERIAL,
    book_id INT      NOT NULL,
    user_id INT,
    mark    SMALLINT NOT NULL CHECK (mark > 0 and mark < 6),
    CONSTRAINT book_mark_fk
        FOREIGN KEY (book_id) REFERENCES shop.books (id),
    CONSTRAINT user_mark_fk
        FOREIGN KEY (user_id) REFERENCES shop.users (id),
    primary key (id)
);
CREATE UNIQUE INDEX user_book_mark_unique_index
    ON shop.book_mark (book_id, user_id);