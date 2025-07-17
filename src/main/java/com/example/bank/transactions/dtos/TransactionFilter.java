package com.example.bank.transactions.dtos;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.UUID;

import org.springframework.format.annotation.DateTimeFormat;

import com.example.bank.common.dto.PaginationRequest;
import com.example.bank.transactions.enums.TransactionStatus;
import com.example.bank.transactions.enums.TransactionType;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Filter parameters for searching transactions")
public class TransactionFilter extends PaginationRequest {

  @Schema(description = "Type of transaction", example = "DEPOSIT")
  private TransactionType transactionType;

  @Schema(description = "Source account number", example = "1234567890")
  private String sourceAccountNumber;

  @Schema(description = "Destination account number", example = "0987654321")
  private String destinationAccountNumber;

  @Schema(description = "Transaction status", example = "COMPLETED")
  private TransactionStatus status;

  @Schema(description = "User who initiated the transaction", example = "7e2c6c9e-0547-4c1c-933f-9325c13b2d5c")
  private UUID initiatedBy;

  @Schema(description = "User who approved the transaction", example = "7e2c6c9e-0547-4c1c-933f-9325c13b2d5c")
  private UUID approvedBy;

  @Schema(description = "Start of transaction date (inclusive)", example = "2024-01-01")
  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
  private LocalDate fromDate;

  @Schema(description = "End of transaction date (inclusive)", example = "2024-01-31")
  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
  private LocalDate toDate;

  @Schema(description = "Minimum transaction amount", example = "100.00")
  private BigDecimal minAmount;

  @Schema(description = "Maximum transaction amount", example = "10000.00")
  private BigDecimal maxAmount;

  @Schema(description = "Transaction reference code", example = "TXN-8D92B1A3")
  private String transactionReference;

  public LocalDateTime getFromDateTime() {
    return fromDate != null ? fromDate.atStartOfDay() : null;
  }

  public LocalDateTime getToDateTime() {
    return toDate != null ? toDate.atTime(LocalTime.MAX) : null;
  }
}