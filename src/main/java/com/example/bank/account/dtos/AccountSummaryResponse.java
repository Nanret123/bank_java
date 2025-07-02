package com.example.bank.account.dtos;

import java.math.BigDecimal;
import java.util.UUID;

import com.example.bank.account.enums.AccountStatus;
import com.example.bank.account.enums.AccountType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AccountSummaryResponse {
    private UUID id;
    private String accountNumber;
    private AccountType accountType;
    private BigDecimal balance;
    private BigDecimal availableBalance;
    private AccountStatus status;
    private String currency;
}
