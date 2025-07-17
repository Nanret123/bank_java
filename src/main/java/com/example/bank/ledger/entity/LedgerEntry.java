package com.example.bank.ledger.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import com.example.bank.ledger.enums.EntryType;
import com.example.bank.ledger.enums.LedgerEntryStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "ledger_entries")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LedgerEntry {

  @Id
  @GeneratedValue
  private UUID id;

  @Column(name = "entry_id", unique = true, nullable = false)
  private String entryId;

  @Column(name = "transaction_id", nullable = false)
  private UUID transactionId;

  @Column(name = "account_number", nullable = false)
  private String accountNumber;

  @Enumerated(EnumType.STRING)
  @Column(name = "entry_type", nullable = false)
  private EntryType entryType; // DEBIT or CREDIT

  @Column(nullable = false, precision = 19, scale = 2)
  private BigDecimal amount;

  @Column(name = "entry_date", nullable = false)
  private LocalDateTime entryDate;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private LedgerEntryStatus status;

  @Column(name = "reversed", nullable = false)
  @Builder.Default
  private Boolean reversed = false;

  @Column(name = "reversal_entry_id")
  private String reversalEntryId;

  @Column(name = "reversal_reason")
  private String reversalReason;

  @Column(name = "reversed_at")
  private LocalDateTime reversedAt;

  @Column(name = "original_entry_id")
  private String originalEntryId;

  @Column(name = "created_by", nullable = false)
  private UUID createdBy;

  @Version
  private Long version;

  @Column(name = "created_at", nullable = false, updatable = false)
  private LocalDateTime createdAt;

  @Column(name = "updated_at")
  private LocalDateTime updatedAt;

  @PrePersist
public void prePersist() {
    setDefaults();
    onCreate();
}

private void setDefaults() {
    if (entryDate == null) {
        entryDate = LocalDateTime.now();
    }
}

private void onCreate() {
    createdAt = LocalDateTime.now();
    updatedAt = LocalDateTime.now();
}

  @PreUpdate
   protected void onUpdate() {
    updatedAt = LocalDateTime.now();
  }
}
