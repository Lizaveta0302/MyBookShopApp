package com.example.bookshop_app.repo;

import com.example.bookshop_app.entity.BalanceTransaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BalanceTransactionRepository extends JpaRepository<BalanceTransaction, Integer> {

    List<BalanceTransaction> findAllByUserId(Integer id);

    Page<BalanceTransaction> findAllByOrderByTimeDesc(Pageable nextPage);
}
