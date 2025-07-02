package com.example.bank.account.dtos;

import java.math.BigDecimal;
import java.util.UUID;

import com.example.bank.account.enums.AccountType;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Request payload for creating a new bank account")
public class CreateAccountRequest {

  @Schema(description = "Customer ID associated with the account", example = "f3b8a7f4-9a87-4b76-b291-cc4b8d5e28f4")
  @NotNull(message = "Customer ID is required")
  private UUID customerId;

  @Schema(description = "Type of account (e.g., SAVINGS, CURRENT)", example = "SAVINGS")
  @NotNull(message = "Account type is required")
  private AccountType accountType;

  @Schema(description = "Branch code where the account is opened", example = "LAG001")
  @NotBlank(message = "Branch code is required")
  private String branchCode;

  @Schema(description = "Currency of the account", example = "NGN", defaultValue = "NGN")
  @Builder.Default
  private String currency = "NGN";

  @Schema(description = "Initial deposit or balance in the account", example = "1000.00", defaultValue = "0.00")
  @DecimalMin(value = "0.0", inclusive = true, message = "Initial balance must be non-negative")
  @Digits(integer = 13, fraction = 2, message = "Invalid balance format")
  @Builder.Default
  private BigDecimal initialBalance = BigDecimal.ZERO;

  @Schema(description = "Optional remarks or notes", example = "Customer requested to open a new savings account")
  private String remarks;

}
