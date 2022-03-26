drop table if exists shop.book_review_like CASCADE;

create table shop.book_review_like
(
    id        BIGSERIAL,
    review_id BIGINT NOT NULL,
    user_id   INT,
    value     INT NOT NULL,
    time      TIMESTAMP,
    CONSTRAINT review_review_like_fk
        FOREIGN KEY (review_id) REFERENCES shop.book_review (id),
    CONSTRAINT user_book_review_like_fk
        FOREIGN KEY (user_id) REFERENCES shop.users (id),
    primary key (id)
);
CREATE UNIQUE INDEX user_book_review_like_unique_index
    ON shop.book_review_like (review_id, user_id);