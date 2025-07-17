package com.example.bank.transactions.enums;

public enum TransactionStatus {
  PENDING("Pending", "Transaction is awaiting processing"),
  PROCESSING("Processing", "Transaction is being processed"),
  COMPLETED("Completed", "Transaction has been successfully completed"),
  FAILED("Failed", "Transaction has failed"),
  CANCELLED("Cancelled", "Transaction has been cancelled"),
  REVERSED("Reversed", "Transaction has been reversed"),
  REJECTED("Rejected", "Transaction has been rejected"),
  TIMEOUT("Timeout", "Transaction has timed out");

  private final String displayName;
  private final String description;

  TransactionStatus(String displayName, String description) {
    this.displayName = displayName;
    this.description = description;
  }

  public String getDisplayName() {
    return displayName;
  }

  public String getDescription() {
    return description;
  }

  public boolean isInProgress() {
    return this == PENDING || this == PROCESSING;
  }

  public boolean isTerminal() {
    return this == COMPLETED || this == FAILED ||
        this == CANCELLED || this == REVERSED ||
        this == REJECTED || this == TIMEOUT;
  }

  public boolean isSuccessful() {
    return this == COMPLETED;
  }

  public boolean canTransitionTo(TransactionStatus newStatus) {
    switch (this) {
      case PENDING:
        return newStatus == PROCESSING || newStatus == CANCELLED ||
            newStatus == REJECTED || newStatus == TIMEOUT;
      case PROCESSING:
        return newStatus == COMPLETED || newStatus == FAILED ||
            newStatus == TIMEOUT;
      case COMPLETED:
        return newStatus == REVERSED;
      case FAILED:
      case CANCELLED:
      case REVERSED:
      case REJECTED:
      case TIMEOUT:
        return false; // Terminal states
      default:
        return false;
    }
  }
}