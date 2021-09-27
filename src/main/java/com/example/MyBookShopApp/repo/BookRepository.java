package com.example.MyBookShopApp.repo;

import com.example.MyBookShopApp.entity.book.Book;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookRepository extends JpaRepository<Book, Integer> {
    List<Book> findBooksByAuthorId(Integer id);

    List<Book> findBooksByTitle(String title);
}
