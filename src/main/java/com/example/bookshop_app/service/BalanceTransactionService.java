package com.example.bookshop_app.service;

import com.example.bookshop_app.entity.BalanceTransaction;
import com.example.bookshop_app.repo.BalanceTransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BalanceTransactionService {

    @Autowired
    private BalanceTransactionRepository balanceTransactionRepository;

    public List<BalanceTransaction> getTransactionHistoryByUserId(Integer id) {
        return balanceTransactionRepository.findAllByUserId(id);
    }

    public Page<BalanceTransaction> getTransactionHistory(Integer offset, Integer limit) {
        Pageable nextPage = PageRequest.of(offset, limit);
        return balanceTransactionRepository.findAllByOrderByTimeDesc(nextPage);
    }
}
