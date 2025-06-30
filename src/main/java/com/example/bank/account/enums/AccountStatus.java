package com.example.bank.account.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum AccountStatus {
  ACTIVE("Active", "Account is active and operational"),
  INACTIVE("Inactive", "Account is temporarily inactive"),
  SUSPENDED("Suspended", "Account is suspended due to compliance issues"),
  FROZEN("Frozen", "Account is frozen by authority"),
  CLOSED("Closed", "Account is permanently closed"),
  PENDING_APPROVAL("Pending Approval", "Account is awaiting approval"),
  DORMANT("Dormant", "Account is dormant due to inactivity");

  private final String displayName;
  private final String description;

  public boolean isOperational() {
    return this == ACTIVE;
  }

  public boolean canAcceptTransactions() {
    return this == ACTIVE || this == INACTIVE;
  }
}
