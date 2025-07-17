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
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Request payload for depositing funds into an account.")
public class DepositRequest {

  @Schema(description = "Account number to receive the deposit", example = "1234567890", maxLength = 20)
  @NotBlank(message = "Account number is required")
  private String accountNumber;

  @Schema(description = "Amount to deposit", example = "1000.00", minimum = "0.01")
  @NotNull(message = "Amount is required")
  @DecimalMin(value = "0.01", inclusive = true)
  @Digits(integer = 17, fraction = 2, message = "Invalid amount format")
  private BigDecimal amount;

  @Schema(description = "Transaction narration", example = "Deposit ", maxLength = 500)
  private String narration;

}
