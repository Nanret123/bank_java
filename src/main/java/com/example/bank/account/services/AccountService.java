package com.example.bank.account.services;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.bank.account.dtos.AccountFilterRequest;
import com.example.bank.account.dtos.AccountResponse;
import com.example.bank.account.dtos.AccountSummaryResponse;
import com.example.bank.account.dtos.BalanceResponse;
import com.example.bank.account.dtos.CreateAccountRequest;
import com.example.bank.account.dtos.TotalAccountSummary;
import com.example.bank.account.dtos.UpdateAccountRequest;
import com.example.bank.account.entity.Account;
import com.example.bank.account.entity.AccountConfiguration;
import com.example.bank.account.enums.AccountStatus;
import com.example.bank.account.enums.AccountType;
import com.example.bank.account.interfaces.IAccount;
import com.example.bank.account.mapper.AccountMapper;
import com.example.bank.account.repository.AccountConfigurationRepository;
import com.example.bank.account.repository.AccountRepository;
import com.example.bank.common.exception.ResourceNotFoundException;
import com.example.bank.common.util.BuildPageable;
import com.example.bank.customer.entity.Customer;
import com.example.bank.customer.repository.CustomerRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class AccountService implements IAccount {

  private final AccountRepository accountRepository;
  private final CustomerRepository customerRepository;
  private final AccountNumberGenerationService accountGenerationService;
  private final AccountConfigurationRepository accountConfigurationRepository;
  private final AccountMapper accountMapper;

  @Override
  public AccountResponse createAccount(CreateAccountRequest request, UUID userId) {
    // check if customer exists
    Customer customer = customerRepository.findById(request.getCustomerId())
        .orElseThrow(() -> new ResourceNotFoundException("Customer not found"));

    boolean exists = accountRepository.existsByCustomerIdAndAccountType(
        request.getCustomerId(), request.getAccountType());

    if (exists) {
      throw new RuntimeException("Customer already has an account of type: " + request.getAccountType());
    }

    // Get account configuration
    AccountConfiguration config = getAccountConfiguration(request.getAccountType());

    // Generate unique account number
    String accountNumber = accountGenerationService.generateAccountNumber(
        request.getAccountType(),
        request.getBranchCode());

    // Validate initial balance against configuration
    validateInitialBalance(request.getInitialBalance(), config);

    // Create account entity
    Account account = Account.builder()
        .accountNumber(accountNumber)
        .customer(customer)
        .accountType(request.getAccountType())
        .balance(request.getInitialBalance())
        .availableBalance(request.getInitialBalance())
        .branchCode(request.getBranchCode())
        .currency(request.getCurrency())
        .createdBy(userId)
        .updatedBy(userId)
        .remarks(request.getRemarks())
        .build();

    Account savedAccount = accountRepository.save(account);

    return accountMapper.toResponse(savedAccount);
  }

  @Override
  @Transactional(readOnly = true)
  public AccountResponse getAccountById(UUID id) {

    Account account = accountRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Account not found"));

    return accountMapper.toResponse(account);
  }

  @Override
  @Transactional(readOnly = true)
  public AccountResponse getAccountByNumber(String accountNumber) {
    Account account = accountRepository.findByAccountNumber(accountNumber)
        .orElseThrow(() -> new ResourceNotFoundException("Account not found with number: " + accountNumber));

    return accountMapper.toResponse(account);
  }

  @Override
  public AccountResponse updateAccount(UUID accountId, UpdateAccountRequest request, UUID userId) {

    Account account = getAccountEntityById(accountId);

    if (request.getStatus() != null) {
      validateStatusTransition(account.getStatus(), request.getStatus());
    }

    // Update fields
    updateAccountFields(account, request, userId);

    Account updatedAccount = accountRepository.save(account);

    return accountMapper.toResponse(updatedAccount);
  }

  @Override
  public void deleteAccount(UUID id, UUID deletedBy) {

    Account account = getAccountEntityById(id);

    if (account.getStatus() == AccountStatus.CLOSED) {
      throw new UnsupportedOperationException("Account is already closed");
    }

    // Check if account can be closed (e.g., balance should be zero)
    if (account.getBalance().compareTo(BigDecimal.ZERO) != 0) {
      throw new UnsupportedOperationException("Cannot close account with non-zero balance");
    }

    account.setStatus(AccountStatus.CLOSED);
    account.setClosedAt(LocalDateTime.now());
    account.setUpdatedBy(deletedBy);

    accountRepository.save(account);
  }

  @Override
  @Transactional(readOnly = true)
  public List<AccountSummaryResponse> getAccountsByCustomerId(UUID customerId) {

    List<Account> accounts = accountRepository.findByCustomerId(customerId);
    return accounts.stream()
        .map(accountMapper::toSummaryResponse)
        .collect(Collectors.toList());
  }

  @Override
  @Transactional(readOnly = true)
  public boolean isAccountNumberUnique(String accountNumber) {
    return !accountRepository.existsByAccountNumber(accountNumber);
  }

  // private helper methods
  private AccountConfiguration getAccountConfiguration(AccountType accountType) {
    return accountConfigurationRepository.findByAccountType(accountType)
        .orElseThrow(() -> new RuntimeException(
            "No active configuration found for account type: " + accountType));
  }

  private void validateInitialBalance(BigDecimal initialBalance, AccountConfiguration config) {
    if (config.getMinimumBalance() != null &&
        initialBalance.compareTo(config.getMinimumBalance()) < 0) {
      throw new RuntimeException(
          "Initial balance cannot be less than minimum balance: " + config.getMinimumBalance());
    }

    if (config.getMaximumBalance() != null &&
        initialBalance.compareTo(config.getMaximumBalance()) > 0) {
      throw new RuntimeException(
          "Initial balance cannot exceed maximum balance: " + config.getMaximumBalance());
    }
  }

  private Account getAccountEntityById(UUID id) {
    return accountRepository.findById(id)
        .orElseThrow(() -> new EntityNotFoundException("Account not found"));
  }

  private void updateAccountFields(Account account, UpdateAccountRequest request, UUID userId) {
    if (request.getBranchCode() != null) {
      account.setBranchCode(request.getBranchCode());
    }
    if (request.getStatus() != null) {
      account.setStatus(request.getStatus());
      if (request.getStatus() == AccountStatus.CLOSED) {
        account.setClosedAt(LocalDateTime.now());
      }
    }
    if (request.getRemarks() != null) {
      account.setRemarks(request.getRemarks());
    }
    account.setUpdatedBy(userId);
  }

  private void validateStatusTransition(AccountStatus currentStatus, AccountStatus newStatus) {
    // Define valid status transitions
    Map<AccountStatus, Set<AccountStatus>> validTransitions = Map.of(
        AccountStatus.ACTIVE,
        Set.of(AccountStatus.INACTIVE, AccountStatus.CLOSED, AccountStatus.FROZEN, AccountStatus.SUSPENDED),
        AccountStatus.INACTIVE, Set.of(AccountStatus.ACTIVE, AccountStatus.CLOSED),
        AccountStatus.FROZEN, Set.of(AccountStatus.ACTIVE, AccountStatus.CLOSED),
        AccountStatus.SUSPENDED, Set.of(AccountStatus.ACTIVE),
        AccountStatus.CLOSED, Set.of() // No transitions from CLOSED
    );

    if (!validTransitions.get(currentStatus).contains(newStatus)) {
      throw new IllegalStateException(
          String.format("Invalid status transition from %s to %s", currentStatus, newStatus));
    }
  }

  public Page<AccountSummaryResponse> getAccounts(AccountFilterRequest filter) {
    Specification<Account> spec = AccountSpecification.hasBranchCode(filter.getBranchCode())
        .and(AccountSpecification.hasStatus(filter.getStatus()))
        .and(AccountSpecification.hasAccountType(filter.getAccountType()));

    Pageable pageable = BuildPageable.createPageable(filter);

    Page<Account> accounts = accountRepository.findAll(spec, pageable);

    return accounts.map(accountMapper::toSummaryResponse);
  }

  public AccountResponse changeAccountStatus(UUID id, AccountStatus status, UUID userId) {

    Account account = getAccountEntityById(id);

    // Business rule: Can't reactivate a closed account
    if (account.getStatus() == AccountStatus.CLOSED && status == AccountStatus.ACTIVE) {
      throw new IllegalStateException("Cannot reactivate a closed account");
    }

    // Set closed date if closing account
    if (status == AccountStatus.CLOSED) {
      account.setClosedAt(LocalDateTime.now());
    }

    account.setStatus(status);
    account.setUpdatedAt(LocalDateTime.now());
    account.setUpdatedBy(userId);

    Account savedAccount = accountRepository.save(account);

    return accountMapper.toResponse(savedAccount);
  }

  @Override
  @Transactional(readOnly = true)
  public BalanceResponse getAccountBalance(String accountNumber) {
    Account account = accountRepository.findByAccountNumber(accountNumber)
        .orElseThrow(() -> new EntityNotFoundException("Account not found: " + accountNumber));
    if (account.getStatus() == AccountStatus.CLOSED) {
      throw new IllegalStateException("Cannot retrieve balance for a closed account");
    }
    return BalanceResponse.builder()
        .balance(account.getBalance())
        .availableBalance(account.getAvailableBalance())
        .currency(account.getCurrency())
        .build();
  }

  @Transactional(readOnly = true)
  public TotalAccountSummary getAccountsSummary() {

    List<Account> allAccounts = accountRepository.findAll();

    if (allAccounts.isEmpty()) {
      return TotalAccountSummary.builder()
          .totalAccounts(0L)
          .totalBalance(BigDecimal.ZERO)
          .summaryByType(Map.of())
          .summaryByStatus(Map.of())
          .build();
    }

    // Calculate total balance
    BigDecimal totalBalance = allAccounts.stream()
        .map(Account::getBalance)
        .reduce(BigDecimal.ZERO, BigDecimal::add);

    // Group by account type
    Map<AccountType, TotalAccountSummary.AccountTypeSummary> summaryByType = allAccounts.stream()
        .collect(Collectors.groupingBy(Account::getAccountType))
        .entrySet().stream()
        .collect(Collectors.toMap(
            Map.Entry::getKey,
            entry -> {
              List<Account> accountsOfType = entry.getValue();
              BigDecimal typeTotal = accountsOfType.stream()
                  .map(Account::getBalance)
                  .reduce(BigDecimal.ZERO, BigDecimal::add);
              BigDecimal averageBalance = accountsOfType.size() > 0
                  ? typeTotal.divide(BigDecimal.valueOf(accountsOfType.size()), 2, RoundingMode.HALF_UP)
                  : BigDecimal.ZERO;

              return TotalAccountSummary.AccountTypeSummary.builder()
                  .count((long) accountsOfType.size())
                  .totalBalance(typeTotal)
                  .averageBalance(averageBalance)
                  .build();
            }));

    // Group by status
    Map<AccountStatus, Long> summaryByStatus = allAccounts.stream()
        .collect(Collectors.groupingBy(Account::getStatus, Collectors.counting()));

    return TotalAccountSummary.builder()
        .totalAccounts((long) allAccounts.size())
        .totalBalance(totalBalance)
        .summaryByType(summaryByType)
        .summaryByStatus(summaryByStatus)
        .build();
  }

  @Transactional
  public void creditAccount(String accountNumber, BigDecimal amount) {
    // 1. Fetch the account
    Account account = accountRepository.findByAccountNumber(accountNumber)
        .orElseThrow(() -> new ResourceNotFoundException("Account not found: " + accountNumber));

    // 2. Validate if account can be credited
    if (!account.canCredit()) {
      throw new IllegalStateException("Account cannot be credited: " + accountNumber);
    }

    // 3. Update balance and available balance
    account.setBalance(account.getBalance().add(amount));
    account.setAvailableBalance(account.getAvailableBalance().add(amount));

    // 4. Save the updated account
    accountRepository.save(account);

    // 5. (Optional) Log the credit transaction — useful for audit purposes
    // log.info("Credited {} {} to account {} via transactionRef {}", amount,
    // account.getCurrency(), accountNumber, transactionReference);
  }

  @Transactional
  public void debitAccount(String accountNumber, BigDecimal amount) {
    // 1. Fetch the account
    Account account = accountRepository.findByAccountNumber(accountNumber)
        .orElseThrow(() -> new ResourceNotFoundException("Account not found: " + accountNumber));

    // 4. Perform the debit
    account.setBalance(account.getBalance().subtract(amount));
    account.setAvailableBalance(account.getAvailableBalance().subtract(amount));

    // 5. Save the updated account
    accountRepository.save(account);
  }

}
