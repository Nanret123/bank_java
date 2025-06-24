package com.example.bank.customer.interfaces;

public interface IPagination {
    int getPage();
    int getSize();
    String getSortBy();
    String getSortDirection();
}
