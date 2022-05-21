drop table if exists shop.visits;

create table shop.visits
(
    id         BIGSERIAL not null,
    user_id    INT       NOT NULL,
    book_id    INT       NOT NULL,
    visited_at TIMESTAMP NOT NULL,
    CONSTRAINT visits_book_id_fk
        FOREIGN KEY (book_id) REFERENCES shop.books (id),
    CONSTRAINT visits_user_id_fk
        FOREIGN KEY (user_id) REFERENCES shop.users (id),
    primary key (id)
);

create UNIQUE INDEX user_book_visits_unique_index on shop.visits (user_id, book_id);