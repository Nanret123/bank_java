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
@Schema(description = "Request body for transferring funds between accounts")
public class TransferRequest {
   @Schema(description = "Account number from which the funds will be transferred", example = "1234567890", required = true)
  @NotBlank(message = "From account number is required")
  private String fromAccountNumber;

  @NotBlank(message = "To account number is required")
   @Schema(description = "Account number to which the funds will be transferred", example = "0987654321", required = true)
  private String toAccountNumber;

  @NotNull(message = "Amount is required")
  @DecimalMin(value = "0.01", message = "Amount must be greater than 0")
  @Digits(integer = 17, fraction = 2, message = "Invalid amount format")
   @Schema(description = "The amount to transfer. Must be greater than 0", example = "500.00", required = true)
  private BigDecimal amount;

   @Schema(description = "Optional narration for the transfer", example = "Transfer for rent payment")
  private String narration;
}