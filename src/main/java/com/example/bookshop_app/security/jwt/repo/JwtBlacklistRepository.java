package com.example.bookshop_app.security.jwt.repo;

import com.example.bookshop_app.security.jwt.JwtBlacklist;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JwtBlacklistRepository extends JpaRepository<JwtBlacklist, Integer> {
    JwtBlacklist findByTokenEquals(String token);
}
