package com.example.bookshop_app.repo;

import com.example.bookshop_app.entity.BalanceTransaction;
import com.example.bookshop_app.entity.Genre;
import com.example.bookshop_app.entity.Tag;
import com.example.bookshop_app.entity.book.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.sql.Date;
import java.util.List;

public interface PaymentRepository extends JpaRepository<BalanceTransaction, Integer> {

}
