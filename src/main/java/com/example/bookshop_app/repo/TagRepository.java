package com.example.bookshop_app.repo;

import com.example.bookshop_app.entity.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TagRepository extends JpaRepository<Tag, Integer> {
    @Override
    Page<Tag> findAll(Pageable pageable);

    @Override
    List<Tag> findAll(Sort sort);

}
