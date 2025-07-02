package com.example.bank.account.services;

import java.util.Map;
import java.util.Random;

import org.springframework.stereotype.Service;

import com.example.bank.account.enums.AccountType;
import com.example.bank.account.repository.AccountRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class AccountNumberGenerationService {

  private final AccountRepository accountRepository;

  // Account number format: {BranchCode}{AccountTypeCode}{4-digit-sequence}
  // Example: 001SAV00000001, 001CHK0000

  private static final Map<AccountType, String> ACCOUNT_TYPE_CODES = Map.of(
      AccountType.SAVINGS, "SAV",
      AccountType.LOAN, "LON",
      AccountType.FIXED_DEPOSIT, "FD",
      AccountType.CURRENT, "CUR"
  // Add more account types as needed
  );

  public String generateAccountNumber(AccountType accountType, String branchCode) {
    String typeCode = ACCOUNT_TYPE_CODES.get(accountType);
    if (typeCode == null) {
      throw new IllegalArgumentException("Unsupported account type: " + accountType);
    }

    String branchDigits = branchCode.substring(3);

    String accountNumber;
    int maxAttempts = 10;
    int attempts = 0;

    do {
      String sequence = generateSequence();
      accountNumber = branchDigits + typeCode + sequence;
      attempts++;

      if (attempts >= maxAttempts) {
        throw new RuntimeException("Failed to generate unique account number after " + maxAttempts + " attempts");
      }
    } while (!isAccountNumberUnique(accountNumber));

    log.info("Generated account number: {}", accountNumber);
    return accountNumber;
  }

  public boolean isAccountNumberValid(String accountNumber) {
    // Basic validation: length should be 10 (3 branch + 3 type + 4 sequence)
    if (accountNumber == null || accountNumber.length() != 14) {
      return false;
    }

    // Check if it contains only alphanumeric characters
    return accountNumber.matches("^[A-Z0-9]+$");
  }

  private String generateSequence() {
    // Generate 4-digit sequence
    long timestamp = System.currentTimeMillis();
    Random random = new Random();
    int randomNum = random.nextInt(1000);

    String sequence = String.valueOf(timestamp % 100000000L + randomNum);
    return String.format("%04d", Long.parseLong(sequence) % 100000000L);
  }

  private boolean isAccountNumberUnique(String accountNumber) {
    return !accountRepository.existsByAccountNumber(accountNumber);
  }

}
