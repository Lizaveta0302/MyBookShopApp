package com.example.bookshop_app.entity;

import io.swagger.annotations.ApiModel;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(schema = "shop", name = "visits")
@ApiModel(description = "data model of visit entity")
public class Visit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "user_id", columnDefinition = "INT NOT NULL")
    private Integer userId;
    @Column(name = "book_id", columnDefinition = "INT NOT NULL")
    private Integer bookId;
    @Column(name = "visited_at", columnDefinition = "TIMESTAMP NOT NULL")
    private LocalDateTime visitedAt;

    public Visit() {
        super();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getBookId() {
        return bookId;
    }

    public void setBookId(Integer bookId) {
        this.bookId = bookId;
    }

    public LocalDateTime getVisitedAt() {
        return visitedAt;
    }

    public void setVisitedAt(LocalDateTime visitedAt) {
        this.visitedAt = visitedAt;
    }
}