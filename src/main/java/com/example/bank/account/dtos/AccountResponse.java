package com.example.bank.account.dtos;

import java.math.BigDecimal;
import java.time.LocalDateTime;
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
public class AccountResponse {
  private UUID id;
  private String accountNumber;
  private UUID customerId;
  private AccountType accountType;
  private BigDecimal balance;
  private BigDecimal availableBalance;
  private AccountStatus status;
  private String branchCode;
  private String currency;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;
  private LocalDateTime closedAt;
  private UUID createdBy;
  private UUID updatedBy;
  private String remarks;
}