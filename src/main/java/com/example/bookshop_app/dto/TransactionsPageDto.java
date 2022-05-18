package com.example.bookshop_app.dto;

import com.example.bookshop_app.entity.BalanceTransaction;

import java.util.List;

public class TransactionsPageDto {

    private Integer count;
    private List<BalanceTransaction> transactions;

    public TransactionsPageDto(List<BalanceTransaction> content) {
        this.transactions = content;
        this.count = content.size();
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public List<BalanceTransaction> getTransactions() {
        return transactions;
    }

    public void setTransactions(List<BalanceTransaction> transactions) {
        this.transactions = transactions;
    }
}
