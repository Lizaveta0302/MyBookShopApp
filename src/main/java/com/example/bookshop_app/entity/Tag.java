package com.example.bookshop_app.entity;

import com.example.bookshop_app.entity.book.Book;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(schema = "shop", name = "tags")
@NamedEntityGraph(name = "tag_entity_graph", attributeNodes = @NamedAttributeNode("books"),
        subgraphs = {
                @NamedSubgraph(
                        name = "tag-books-author-subgraph",
                        attributeNodes =
                                {
                                        @NamedAttributeNode("author")
                                }
                )
        }
)
public class Tag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "tag_name", columnDefinition = "VARCHAR(60) NOT NULL")
    private String tagName;

    @ManyToMany(mappedBy = "tags")
    private Set<Book> books = new HashSet<>();

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTagName() {
        return tagName;
    }

    public void setTagName(String tagName) {
        this.tagName = tagName;
    }

    public Set<Book> getBooks() {
        return books;
    }

    public void setBooks(Set<Book> books) {
        this.books = books;
    }
}
