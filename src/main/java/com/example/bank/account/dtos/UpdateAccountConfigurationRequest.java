package com.example.bank.account.dtos;

import java.math.BigDecimal;

import jakarta.validation.constraints.DecimalMin;
import lombok.Data;

@Data
public class UpdateAccountConfigurationRequest {

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

  private Boolean overdraftAllowed;

  @DecimalMin(value = "0.0", message = "Overdraft limit cannot be negative")
  private BigDecimal overdraftLimit;
}
