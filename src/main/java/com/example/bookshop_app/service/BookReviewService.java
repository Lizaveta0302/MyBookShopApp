package com.example.bookshop_app.service;

import com.example.bookshop_app.aop.annotation.Loggable;
import com.example.bookshop_app.entity.book.Book;
import com.example.bookshop_app.entity.book.review.BookReview;
import com.example.bookshop_app.entity.book.review.BookReviewLike;
import com.example.bookshop_app.repo.BookReviewLikeRepository;
import com.example.bookshop_app.repo.BookReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class BookReviewService {

    private BookReviewRepository bookReviewRepository;
    @Autowired
    private BookReviewLikeRepository bookReviewLikeRepository;

    @Autowired
    public BookReviewService(BookReviewRepository bookReviewRepository) {
        this.bookReviewRepository = bookReviewRepository;
    }

    public List<BookReview> getAllBookReviews(Book book) {
        return bookReviewRepository.findAllByBook(book);
    }

    public BookReview getBookReviewById(Long reviewId) {
        return bookReviewRepository.getById(reviewId);
    }

    @Loggable
    public BookReviewLike insertBookReviewLike(BookReviewLike bookReviewLike) {
        BookReviewLike like = bookReviewLikeRepository.saveAndFlush(bookReviewLike);
        return like;
    }

    @Transactional
    public void deleteBookReviewLike(Long reviewId, Integer userId) {
        bookReviewLikeRepository.deleteByReviewIdAndUserId(reviewId, userId);
    }
}
