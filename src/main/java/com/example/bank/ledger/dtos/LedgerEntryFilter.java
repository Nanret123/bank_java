package com.example.bank.ledger.dtos;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

import com.example.bank.common.dto.PaginationRequest;
import com.example.bank.ledger.enums.EntryType;
import com.example.bank.ledger.enums.LedgerEntryStatus;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Schema(description = "Filter criteria for retrieving ledger entries")
public class LedgerEntryFilter extends PaginationRequest {

   private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

  @Schema(description = "Filter by transaction ID", example = "TXN123456789")
  private UUID transactionId;

  @Schema(description = "Filter by account number", example = "1234567890")
  private String accountNumber;

  @Schema(description = "Filter by entry type (e.g., DEBIT or CREDIT)", example = "CREDIT")
  private EntryType entryType;

  @Schema(description = "Filter by status of the ledger entry", example = "POSTED")
  private LedgerEntryStatus status;

  @Schema(description = "Filter by reversal status (true = reversed, false = not reversed)", example = "false")
  private Boolean reversed;

  @Schema(description = "Start date for filtering ledger entries", example = "2025-01-01")
  private String fromDate;

  @Schema(description = "End date for filtering ledger entries", example = "2025-12-31")
  private String toDate;

  @Schema(description = "Filter by the user or system that created the ledger entry", example = "adminUser")
  private String createdBy;

   public LocalDateTime getFromDateTime() {
        if (fromDate != null && !fromDate.isBlank()) {
            return LocalDate.parse(fromDate, DATE_FORMATTER).atStartOfDay();
        }
        return null;
    }

    public LocalDateTime getToDateTime() {
        if (toDate != null && !toDate.isBlank()) {
            return LocalDate.parse(toDate, DATE_FORMATTER).atTime(23, 59, 59);
        }
        return null;
    }
}