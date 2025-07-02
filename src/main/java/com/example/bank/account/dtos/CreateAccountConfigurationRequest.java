package com.example.bank.account.dtos;

import java.math.BigDecimal;

import com.example.bank.account.enums.AccountType;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CreateAccountConfigurationRequest {

  @NotNull(message = "Account type is required")
  private AccountType accountType;

  @DecimalMin(value = "0.0", message = "Minimum balance cannot be negative")
  private BigDecimal minimumBalance;

  @DecimalMin(value = "0.0", message = "Maximum balance cannot be negative")
  private BigDecimal maximumBalance;

  @DecimalMin(value = "0.0", message = "Daily withdrawal limit cannot be negative")
  private BigDecimal dailyWithdrawalLimit;

  private Integer monthlyTransactionLimit;

  @DecimalMin(value = "0.0", message = "Interest rate cannot be negative")
  private BigDecimal interestRate;

  @DecimalMin(value = "0.0", message = "Maintenance fee cannot be negative")
  private BigDecimal maintenanceFee;

  private Boolean overdraftAllowed = false;

  @DecimalMin(value = "0.0", message = "Overdraft limit cannot be negative")
  private BigDecimal overdraftLimit;
}