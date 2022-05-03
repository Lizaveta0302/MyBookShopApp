package com.example.bookshop_app.repo;

import com.example.bookshop_app.entity.BalanceTransaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PaymentRepository extends JpaRepository<BalanceTransaction, Integer> {

    List<BalanceTransaction> findAllByUserId(Integer id);
}
