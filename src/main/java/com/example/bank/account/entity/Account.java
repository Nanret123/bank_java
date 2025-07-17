package com.example.bank.account.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.example.bank.account.enums.AccountStatus;
import com.example.bank.account.enums.AccountType;
import com.example.bank.customer.entity.Customer;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "accounts")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Account {

  @Id
  @GeneratedValue
  private UUID id;

  @Column(name = "account_number", unique = true, nullable = false, length = 20)
  private String accountNumber;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "customer_id", nullable = false)
  private Customer customer;

  @Enumerated(EnumType.STRING)
  @Column(name = "account_type", nullable = false)
  private AccountType accountType;

  @Column(name = "balance", nullable = false, precision = 15, scale = 2)
  @Builder.Default
  private BigDecimal balance = BigDecimal.ZERO;

  @Column(name = "available_balance", nullable = false, precision = 15, scale = 2)
  @Builder.Default
  private BigDecimal availableBalance = BigDecimal.ZERO;

  @Enumerated(EnumType.STRING)
  @Column(name = "status", nullable = false)
  @Builder.Default
  private AccountStatus status = AccountStatus.ACTIVE;

  @Column(name = "branch_code", nullable = false, length = 10)
  private String branchCode;

  @Column(name = "currency", nullable = false, length = 3)
  @Builder.Default
  private String currency = "USD";

  @CreationTimestamp
  @Column(name = "created_at", nullable = false, updatable = false)
  private LocalDateTime createdAt;

  @UpdateTimestamp
  @Column(name = "updated_at", nullable = false)
  private LocalDateTime updatedAt;

  @Column(name = "closed_at")
  private LocalDateTime closedAt;

  @Column(name = "created_by", nullable = false)
  private UUID createdBy;

  @Column(name = "updated_by")
  private UUID updatedBy;

  @Column(name = "remarks", length = 500)
  private String remarks;

  // Business logic (refactor to use config where needed)
  public boolean isActive() {
    return this.status == AccountStatus.ACTIVE;
  }

  public boolean canDebit(BigDecimal amount, AccountConfiguration config) {
    BigDecimal allowedMin = config.getOverdraftAllowed() ? config.getMinimumBalance().negate()
        : config.getMinimumBalance();
    return isActive() &&
        availableBalance.subtract(amount).compareTo(allowedMin) >= 0;
  }

  public boolean canCredit() {
    return isActive() && status != AccountStatus.CLOSED;
  }

  @PrePersist
  protected void onCreate() {
    createdAt = LocalDateTime.now();
    updatedAt = LocalDateTime.now();
  }

  @PreUpdate
  protected void onUpdate() {
    updatedAt = LocalDateTime.now();
  }
}
