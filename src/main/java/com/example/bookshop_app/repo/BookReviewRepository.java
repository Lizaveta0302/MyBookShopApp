package com.example.bookshop_app.repo;

import com.example.bookshop_app.entity.book.Book;
import com.example.bookshop_app.entity.book.review.BookReview;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookReviewRepository extends JpaRepository<BookReview, Long> {

    @Override
    BookReview saveAndFlush(BookReview bookReview);

    List<BookReview> findAllByBook(Book book);
}
