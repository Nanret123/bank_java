package com.example.bank.transactions.enums;

public enum TransactionType {
  DEPOSIT("Deposit", "Credit transaction to account"),
  WITHDRAWAL("Withdrawal", "Debit transaction from account"),
  TRANSFER("Transfer", "Transfer between accounts"),
  REVERSAL("Reversal", "Reversal of a previous transaction"),
  FEE("Fee", "Bank fee or charge"),
  INTEREST("Interest", "Interest payment"),
  ADJUSTMENT("Adjustment", "Manual adjustment transaction");

  private final String displayName;
  private final String description;

  TransactionType(String displayName, String description) {
    this.displayName = displayName;
    this.description = description;
  }

  public String getDisplayName() {
    return displayName;
  }

  public String getDescription() {
    return description;
  }

  public boolean isDebitType() {
    return this == WITHDRAWAL || this == TRANSFER || this == FEE;
  }

  public boolean isCreditType() {
    return this == DEPOSIT || this == INTEREST;
  }
}
