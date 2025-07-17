package com.example.bank.ledger.dtos;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import com.example.bank.ledger.enums.EntryType;
import com.example.bank.ledger.enums.LedgerEntryStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LedgerEntryResponse {
  private UUID id;
  private String entryId;
  private UUID transactionId;
  private String accountNumber;
  private EntryType entryType;
  private BigDecimal amount;
  private LocalDateTime entryDate;
  private LedgerEntryStatus status;
  private Boolean reversed;
  private String reversalEntryId;
  private String originalEntryId;
  private String createdBy;
  private Long version;
}