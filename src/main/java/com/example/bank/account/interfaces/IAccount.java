package com.example.bank.account.interfaces;

import java.util.List;
import java.util.UUID;

import com.example.bank.account.dtos.AccountResponse;
import com.example.bank.account.dtos.AccountSummaryResponse;
import com.example.bank.account.dtos.BalanceResponse;
import com.example.bank.account.dtos.CreateAccountRequest;
import com.example.bank.account.dtos.UpdateAccountRequest;

public interface IAccount {
    AccountResponse createAccount(CreateAccountRequest request, UUID userId);

    AccountResponse getAccountById(UUID id);

    AccountResponse getAccountByNumber(String accountNumber);

    AccountResponse updateAccount(UUID id, UpdateAccountRequest request, UUID userId);

    void deleteAccount(UUID id, UUID deletedBy);

    List<AccountSummaryResponse> getAccountsByCustomerId(UUID customerId);

    boolean isAccountNumberUnique(String accountNumber);

    BalanceResponse getAccountBalance(String accountNumber);
}
