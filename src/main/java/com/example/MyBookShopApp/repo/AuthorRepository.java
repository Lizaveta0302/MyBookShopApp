package com.example.MyBookShopApp.repo;

import com.example.MyBookShopApp.entity.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface AuthorRepository extends JpaRepository<Author, Integer> {
    @Query(value = "SELECT a FROM Author a LEFT JOIN FETCH a.bookList WHERE a.id = :id")
    @Override
    Optional<Author> findById(Integer id);
}
