package com.example.bank.transactions.interfaces;

import com.example.bank.transactions.entity.Transaction;

@FunctionalInterface
public interface TransactionOperation {
    void execute(Transaction transaction);
}