package com.example.bank.ledger.dtos;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import com.example.bank.ledger.enums.EntryType;

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
@Schema(description = "Request to create a new ledger entry")
public class CreateLedgerEntryRequest {

    @NotBlank
    @Schema(description = "ID of the transaction this ledger entry is linked to", example = "TXN1234567890")
    private UUID transactionId;

    @NotBlank
    @Schema(description = "Account number involved in the transaction", example = "1234567890")
    private String accountNumber;

    @NotNull
    @Schema(description = "Type of the ledger entry (e.g., DEBIT or CREDIT)", example = "DEBIT")
    private EntryType entryType;

    @NotNull
    @DecimalMin(value = "0.01", message = "Amount must be greater than 0")
    @Digits(integer = 17, fraction = 2)
    @Schema(description = "Amount of money involved in the ledger entry", example = "1500.00")
    private BigDecimal amount;

    @NotBlank
    @Schema(description = "Username or ID of the user/system that created the ledger entry", example = "adminUser")
    private UUID createdBy;

    @Schema(description = "Date and time the entry was created. If not provided, the current time will be used", example = "2025-07-11T20:30:00")
    private LocalDateTime entryDate;
}