package com.example.bank.ledger.dtos;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

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
@Schema(description = "Request to perform a double-entry ledger transaction (debit and credit)")
public class DoubleEntryRequest {
  @NotBlank
  @Schema(description = "ID of the transaction for this double-entry", example = "TXN987654321")
  private UUID transactionId;

  @NotBlank
  @Schema(description = "Account number to be debited", example = "1234567890")
  private String debitAccountNumber;

  @NotBlank
  @Schema(description = "Account number to be credited", example = "0987654321")
  private String creditAccountNumber;

  @NotNull
  @DecimalMin(value = "0.01", message = "Amount must be greater than 0")
  @Digits(integer = 17, fraction = 2)
  @Schema(description = "Amount to be transferred from debit to credit account", example = "2500.00")
  private BigDecimal amount;

  @NotBlank
  @Schema(description = "ID or username of the creator of this transaction", example = "adminUser")
  private UUID createdBy;

  @Schema(description = "Timestamp of the ledger entry. If not provided, system time will be used", example = "2025-07-11T21:00:00")
  private LocalDateTime entryDate;
}