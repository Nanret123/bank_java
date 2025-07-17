package com.example.bank.transactions.dtos;

import java.math.BigDecimal;

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
@Schema(description = "Request body for a withdrawal transaction")
public class WithdrawalRequest {
  @Schema(description = "The account number from which funds will be withdrawn", example = "1234567890")
  @NotBlank(message = "Account number is required")
  private String accountNumber;

  @NotNull(message = "Amount is required")
  @DecimalMin(value = "0.01", message = "Amount must be greater than 0")
  @Digits(integer = 17, fraction = 2, message = "Invalid amount format")
  @Schema(description = "The amount to withdraw. Must be greater than 0", example = "1500.00")
  private BigDecimal amount;

  @Schema(description = "Optional description or reason for the withdrawal", example = "ATM cash withdrawal")
  private String narration;

}