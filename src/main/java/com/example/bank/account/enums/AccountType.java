package com.example.bank.account.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum AccountType {
  SAVINGS("Savings Account", "SAV", true, 3.5),
  CURRENT("Current Account", "CUR", false, 0.0),
  FIXED_DEPOSIT("Fixed Deposit", "FD", false, 5.0),
  LOAN("Loan Account", "LON", false, 0.0),
  JOINT_SAVINGS("Joint Savings Account", "JS", true, 2.5);

  private final String description;
  private final String code;
  private final boolean interestBearing;
  private final double defaultInterestRate;
}