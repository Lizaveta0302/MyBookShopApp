package com.example.bookshop_app.repo;

import com.example.bookshop_app.entity.Genre;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GenreRepository extends JpaRepository<Genre, Integer> {
    @Override
    Page<Genre> findAll(Pageable pageable);
}
