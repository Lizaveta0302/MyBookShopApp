package com.example.bookshop_app.repo;

import com.example.bookshop_app.entity.book.BookMark;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BookMarkRepository extends JpaRepository<BookMark, Integer> {

    @Query(value = "select count(*) from shop.book_mark where book_id = :bookId and mark = 1\n" +
            "union all\n" +
            "select count(*) from shop.book_mark where book_id = :bookId and mark = 2\n" +
            "union all\n" +
            "select count(*) from shop.book_mark where book_id = :bookId and mark = 3\n" +
            "union all\n" +
            "select count(*) from shop.book_mark where book_id = :bookId and mark = 4\n" +
            "union all\n" +
            "select count(*) from shop.book_mark where book_id = :bookId and mark = 5\n" +
            "union all\n" +
            "select  ROUND(AVG(mark)) from shop.book_mark where book_id = :bookId", nativeQuery = true)
    List<Integer> getBookRating(@Param("bookId") Integer bookId);

    @Override
    BookMark saveAndFlush(BookMark bookMark);
}
