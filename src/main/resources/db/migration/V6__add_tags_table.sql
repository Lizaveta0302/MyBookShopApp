create table if not exists shop.tags
(
    id         serial not null,
    tag_name  varchar(60) NOT NULL,
    primary key (id)
);

create table if not exists shop.book2tag
(
    book_id INT NOT NULL,
    tag_id  INT NOT NULL,
    primary key (book_id, tag_id),
    CONSTRAINT book_tag_fk_1
        FOREIGN KEY (tag_id) REFERENCES shop.tags (id),
    CONSTRAINT book_tag_fk_2
        FOREIGN KEY (book_id) REFERENCES shop.books (id)
);
