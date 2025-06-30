package com.example.bank.account.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.example.bank.account.enums.AccountType;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "account_configurations")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AccountConfiguration {

  @Id
  @GeneratedValue
  private UUID id;

  @Enumerated(EnumType.STRING)
  @Column(name = "account_type", nullable = false, unique = true)
  private AccountType accountType;

  @Column(name = "minimum_balance", precision = 15, scale = 2)
  private BigDecimal minimumBalance;

  @Column(name = "maximum_balance", precision = 15, scale = 2)
  private BigDecimal maximumBalance;

  @Column(name = "daily_withdrawal_limit", precision = 15, scale = 2)
  private BigDecimal dailyWithdrawalLimit;

  @Column(name = "monthly_transaction_limit")
  private Integer monthlyTransactionLimit;

  @Column(name = "interest_rate", precision = 5, scale = 4)
  private BigDecimal interestRate;

  @Column(name = "maintenance_fee", precision = 10, scale = 2)
  private BigDecimal maintenanceFee;

  @Column(name = "overdraft_allowed")
  private Boolean overdraftAllowed;

  @Column(name = "overdraft_limit", precision = 15, scale = 2)
  private BigDecimal overdraftLimit;

  @Column(name = "is_active", nullable = false)
  @Builder.Default
  private Boolean isActive = true;

  @CreationTimestamp
  @Column(name = "created_at", nullable = false, updatable = false)
  private LocalDateTime createdAt;

  @UpdateTimestamp
  @Column(name = "updated_at")
  private LocalDateTime updatedAt;

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
