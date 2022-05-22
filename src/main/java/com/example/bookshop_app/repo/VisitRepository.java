package com.example.bookshop_app.repo;

import com.example.bookshop_app.entity.Visit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface VisitRepository extends JpaRepository<Visit, Long> {

    @Override
    Visit saveAndFlush(Visit visit);

    Visit findByUserIdAndAndBookId(Integer userId, Integer bookId);

    @Modifying
    @Query(value = "UPDATE shop.visits v SET visited_at=:visitedAt WHERE v.id =:id", nativeQuery = true)
    void updateVisitedAt(@Param("id") Long id, @Param("visitedAt") LocalDateTime visitedAt);

    List<Visit> findAllByVisitedAtAfterAndUserIdOrderByVisitedAt(LocalDateTime lowLimitDate, Integer userId);
}
