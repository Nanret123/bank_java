package com.example.bank.account.dtos;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import com.example.bank.account.enums.AccountType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AccountConfigurationResponse {
  private UUID id;
  private AccountType accountType;
  private BigDecimal minimumBalance;
  private BigDecimal maximumBalance;
  private BigDecimal dailyWithdrawalLimit;
  private Integer monthlyTransactionLimit;
  private BigDecimal interestRate;
  private BigDecimal maintenanceFee;
  private Boolean overdraftAllowed;
  private BigDecimal overdraftLimit;
  private Boolean isActive;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;
}