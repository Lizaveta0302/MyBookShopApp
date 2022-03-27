package com.example.bookshop_app.repo;

import com.example.bookshop_app.entity.book.review.BookReviewLike;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookReviewLikeRepository extends JpaRepository<BookReviewLike, Long> {

    @Override
    BookReviewLike saveAndFlush(BookReviewLike bookReviewLike);

    void deleteByReviewIdAndUserId(Long reviewId, Integer userId);
}
