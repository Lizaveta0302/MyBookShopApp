package com.example.MyBookShopApp.repo;

import com.example.MyBookShopApp.entity.book.Book;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BookRepository extends JpaRepository<Book, Integer> {
    Optional<List<Book>> findBooksByAuthorId(Integer id);

    Optional<List<Book>> findBooksByTitle(String title);
}
