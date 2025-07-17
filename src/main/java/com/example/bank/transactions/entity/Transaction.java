package com.example.bank.transactions.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import com.example.bank.transactions.enums.TransactionStatus;
import com.example.bank.transactions.enums.TransactionType;

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
@Table(name = "transactions")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Transaction {

  @Id
  @GeneratedValue
  private UUID id;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private TransactionType transactionType;

  @Column(nullable=false)
  private String fromAccountNumber;

  private String toAccountNumber;

  @Column(nullable = false, precision = 19, scale = 2)
  private BigDecimal amount;

  @Column(unique = true, nullable = false) 
  private String transactionReference; 

  @Column(length = 500)
  private String narration;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false, length = 20)
  private TransactionStatus status;

  @Column(name = "transaction_date", nullable = false)
  private LocalDateTime transactionDate;

  @Column(nullable = false)
  private LocalDateTime valueDate;

   @Column(nullable = false)
  private UUID initiatedBy;

  private UUID approvedBy;

  private String reversalReference;

  @Builder.Default
  private boolean reversed = false;

  @Column(nullable = false, updatable = false)
  private LocalDateTime createdAt;

  private LocalDateTime updatedAt;

  @Version
  private Integer version;

  @Column(precision = 19, scale = 2)
  private BigDecimal transactionFee;

  @Column(precision = 19, scale = 2)
  private BigDecimal vatAmount;

  @PrePersist
  public void prePersist() {
    if (this.transactionReference == null) {
      this.transactionReference = generateTransactionReference();
    }
    this.createdAt = LocalDateTime.now();
    this.updatedAt = LocalDateTime.now();

  }

  @PreUpdate
  public void preUpdate() {
    this.updatedAt = LocalDateTime.now();
  }

  private String generateTransactionReference() {
     return (this.transactionType == TransactionType.REVERSAL ? "REV-" : "TXN-") + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
  }

  // === Business logic helpers ===

  public boolean isDebitTransaction() {
    return TransactionType.WITHDRAWAL.equals(this.transactionType) ||
        TransactionType.TRANSFER.equals(this.transactionType);
  }

  public boolean isCreditTransaction() {
    return TransactionType.DEPOSIT.equals(this.transactionType);
  }

  public boolean isTransferTransaction() {
    return TransactionType.TRANSFER.equals(this.transactionType);
  }

  public boolean isPending() {
    return TransactionStatus.PENDING.equals(this.status);
  }

  public boolean isCompleted() {
    return TransactionStatus.COMPLETED.equals(this.status);
  }

  public boolean isFailed() {
    return TransactionStatus.FAILED.equals(this.status);
  }

  public boolean isReversed() {
    return TransactionStatus.REVERSED.equals(this.status);
  }

  public boolean canBeReversed() {
    return TransactionStatus.COMPLETED.equals(this.status) &&
        this.reversalReference == null;
  }
}
