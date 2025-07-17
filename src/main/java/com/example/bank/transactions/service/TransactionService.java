package com.example.bank.transactions.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.bank.account.entity.Account;
import com.example.bank.account.entity.AccountConfiguration;
import com.example.bank.account.repository.AccountConfigurationRepository;
import com.example.bank.account.repository.AccountRepository;
import com.example.bank.account.services.AccountService;
import com.example.bank.audit.annotations.Auditable;
import com.example.bank.audit.enums.OperationType;
import com.example.bank.common.exception.ResourceNotFoundException;
import com.example.bank.common.util.BuildPageable;
import com.example.bank.ledger.dtos.DoubleEntryRequest;
import com.example.bank.ledger.service.LedgerService;
import com.example.bank.transactions.dtos.AccountTransactionFilter;
import com.example.bank.transactions.dtos.DepositRequest;
import com.example.bank.transactions.dtos.TransactionFilter;
import com.example.bank.transactions.dtos.TransactionResponse;
import com.example.bank.transactions.dtos.TransferRequest;
import com.example.bank.transactions.dtos.WithdrawalRequest;
import com.example.bank.transactions.entity.Transaction;
import com.example.bank.transactions.enums.TransactionStatus;
import com.example.bank.transactions.enums.TransactionType;
import com.example.bank.transactions.interfaces.ITransactionService;
import com.example.bank.transactions.interfaces.TransactionOperation;
import com.example.bank.transactions.mapper.TransactionMapper;
import com.example.bank.transactions.repository.TransactionRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class TransactionService implements ITransactionService {

  @Value("${banking.transaction.withdrawal.fee:10.00}")
  private BigDecimal withdrawalFee;

  @Value("${banking.transaction.approval-threshold}")
  private BigDecimal approvalThreshold;

  @Value("${banking.transaction.transfer.fee:25.00}")
  private BigDecimal transferFee;

  @Value("${banking.transaction.vat.rate:0.075}")
  private BigDecimal vatRate;

  @Value("${banking.bank.control.account}")
  private String bankControlAccount;

  private final TransactionRepository transactionRepository;
  private final AccountRepository accountRepository;
  private final AccountConfigurationRepository accountConfig;
  private final AccountService accountService;
  private final TransactionMapper transactionMapper;
  private final LedgerService ledgerService;

  @Override
  @Auditable(
    operation = OperationType.TRANSACTION,
    module = "transaction",
    entityType = "Deposit",
    captureArgs = true,
    captureResult = true
)
  public TransactionResponse processDeposit(DepositRequest request, UUID initiatedBy) {

    // Validate request
    validateDepositRequest(request);

    // Create transaction
    Transaction transaction = transactionMapper.depositRequestToTransaction(request);

    transaction.setTransactionType(TransactionType.DEPOSIT);
    transaction.setFromAccountNumber(bankControlAccount);
    transaction.setTransactionFee(transferFee);
    transaction.setVatAmount(vatRate);
    transaction.setInitiatedBy(initiatedBy);

    Transaction processed = processTransaction(transaction, initiatedBy, tx -> {
      BigDecimal totalCharges = tx.getTransactionFee().add(tx.getVatAmount());
      BigDecimal netAmount = request.getAmount().subtract(totalCharges);

      accountService.debitAccount(bankControlAccount, netAmount);
      accountService.creditAccount(request.getAccountNumber(), netAmount);
    });

    return transactionMapper.toResponse(processed);
  }

  @Override
  @Auditable(
    operation = OperationType.TRANSACTION,
    module = "transaction",
    entityType = "Withdrawal",
    captureArgs = true,
    captureResult = true
)
  public TransactionResponse processWithdrawal(WithdrawalRequest request, UUID initiatedBy) {

    // Validate request
    validateWithdrawalRequest(request);

    // Create transaction
    Transaction transaction = transactionMapper.withdrawalRequestToTransaction(request);

    transaction.setTransactionType(TransactionType.WITHDRAWAL);
    transaction.setToAccountNumber(bankControlAccount);
    transaction.setTransactionFee(withdrawalFee);
    transaction.setVatAmount(vatRate);
    transaction.setInitiatedBy(initiatedBy);

    // Total amount to debit (amount + fees)
    BigDecimal totalDebitAmount = request.getAmount().add(withdrawalFee).add(vatRate);

    Transaction processed = processTransaction(transaction, initiatedBy, tx -> {
      accountService.debitAccount(request.getAccountNumber(), totalDebitAmount);
      accountService.creditAccount(bankControlAccount, request.getAmount());
    });

    return transactionMapper.toResponse(processed);
  }

  @Override
  @Auditable(
    operation = OperationType.TRANSACTION,
    module = "transaction",
    entityType = "Transfer",
    captureArgs = true,
    captureResult = true
)
  public TransactionResponse processTransfer(TransferRequest request, UUID initiatedBy) {
    validateTransferRequest(request);

    // Create transaction
    Transaction transaction = transactionMapper.toTransferTransaction(request);

    transaction.setTransactionFee(transferFee);
    transaction.setVatAmount(vatRate);

    // Total amount to debit from source account (amount + fees)
    BigDecimal totalDebitAmount = request.getAmount().add(transferFee).add(vatRate);

    Transaction processed = processTransaction(transaction, initiatedBy, tx -> {
      accountService.debitAccount(request.getFromAccountNumber(), totalDebitAmount);
      accountService.creditAccount(request.getToAccountNumber(), request.getAmount());
    });

    return transactionMapper.toResponse(processed);
  }

  @Override
  @Transactional(readOnly = true)
  public TransactionResponse getTransactionById(UUID id) {
    Transaction transaction = findTransactionEntityById(id);
    return transactionMapper.toResponse(transaction);
  }

  @Override
  @Transactional(readOnly = true)
  public TransactionResponse getTransactionByReference(String transactionReference) {
    Transaction transaction = transactionRepository.findByTransactionReference(transactionReference)
        .orElseThrow(
            () -> new ResourceNotFoundException("Transaction not found with reference: " + transactionReference));
    return transactionMapper.toResponse(transaction);
  }

  @Override
  @Transactional(readOnly = true)
  public Page<TransactionResponse> getTransactions(TransactionFilter filter) {

    Specification<Transaction> spec = TransactionSpecification.buildSpecification(filter);
    Pageable pageable = BuildPageable.createPageable(filter);

    Page<Transaction> transactions = transactionRepository.findAll(spec, pageable);

    return transactions.map(transactionMapper::toResponse);
  }

  @Override
  @Auditable(
    operation = OperationType.APPROVE_TRANSACTION,
    module = "transaction",
    entityType = "Transaction",
    captureArgs = true,
    captureResult = true
)
  public TransactionResponse approveTransaction(UUID transactionId, UUID approvedBy) {
    Transaction transaction = transactionRepository.findById(transactionId)
        .orElseThrow(() -> new ResourceNotFoundException("Transaction not found"));

    if (transaction.getStatus() != TransactionStatus.PROCESSING) {
      throw new IllegalStateException("Only pending transactions can be approved");
    }

    try {
      switch (transaction.getTransactionType()) {
        case DEPOSIT -> {
          BigDecimal totalCharges = transaction.getTransactionFee().add(transaction.getVatAmount());
          BigDecimal netAmount = transaction.getAmount().subtract(totalCharges);
          accountService.creditAccount(transaction.getToAccountNumber(), netAmount);
          accountService.debitAccount(bankControlAccount, netAmount);
        }

        case WITHDRAWAL -> {
          BigDecimal totalDebit = transaction.getAmount().add(transaction.getTransactionFee())
              .add(transaction.getVatAmount());
          accountService.debitAccount(transaction.getFromAccountNumber(), totalDebit);
          accountService.creditAccount(bankControlAccount, transaction.getAmount());
        }

        case TRANSFER -> {
          BigDecimal totalDebitAmount = transaction.getAmount().add(transferFee).add(vatRate);
          BigDecimal totalDebit = transaction.getAmount();
          accountService.debitAccount(transaction.getFromAccountNumber(), totalDebitAmount);
          accountService.creditAccount(transaction.getToAccountNumber(), totalDebit);
        }

        default ->
          throw new IllegalArgumentException("Unsupported transaction type: " + transaction.getTransactionType());
      }

      ledgerService.createDoubleEntry(toDoubleEntryRequest(transaction));

      transaction.setStatus(TransactionStatus.COMPLETED);
      transaction.setApprovedBy(approvedBy);
      transaction.setUpdatedAt(LocalDateTime.now());

      transactionRepository.save(transaction);

      return transactionMapper.toResponse(transaction);

    } catch (Exception e) {
      transaction.setStatus(TransactionStatus.FAILED);
      transactionRepository.save(transaction);
      throw new RuntimeException("Transaction approval failed: " + e.getMessage(), e);
    }
  }

  @Override
  @Auditable(
    operation = OperationType.REVERSE_TRANSACTION,
    module = "transaction",
    entityType = "Transaction",
    captureArgs = true,
    captureResult = true
)
  public TransactionResponse reverseTransaction(UUID transactionId, UUID reversedBy) {
    Transaction original = transactionRepository.findById(transactionId)
        .orElseThrow(() -> new ResourceNotFoundException("Transaction not found"));

    if (original.getStatus() != TransactionStatus.COMPLETED) {
      throw new IllegalStateException("Only completed transactions can be reversed");
    }

    if (original.isReversed()) {
      throw new IllegalStateException("Transaction has already been reversed");
    }

    Transaction reversal = new Transaction();
    reversal.setTransactionType(TransactionType.REVERSAL);
    reversal.setTransactionReference("REV-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
    reversal.setReversalReference(original.getTransactionReference());
    reversal.setAmount(original.getAmount());
    reversal.setStatus(TransactionStatus.COMPLETED);
    reversal.setInitiatedBy(reversedBy);
    reversal.setApprovedBy(reversedBy);
    reversal.setTransactionFee(BigDecimal.ZERO);
    reversal.setVatAmount(BigDecimal.ZERO);
    reversal.setTransactionDate(LocalDateTime.now());
    reversal.setValueDate(LocalDateTime.now());

    // Reverse the original logic
    switch (original.getTransactionType()) {
      case DEPOSIT -> {
        reversal.setFromAccountNumber(original.getToAccountNumber());
        reversal.setToAccountNumber(original.getFromAccountNumber());

        accountService.debitAccount(original.getToAccountNumber(), original.getAmount());
        accountService.creditAccount(original.getFromAccountNumber(), original.getAmount());
      }
      case WITHDRAWAL -> {
        reversal.setFromAccountNumber(original.getToAccountNumber());
        reversal.setToAccountNumber(original.getFromAccountNumber());

        accountService.debitAccount(original.getToAccountNumber(), original.getAmount());
        accountService.creditAccount(original.getFromAccountNumber(), original.getAmount());
      }
      case TRANSFER -> {
        reversal.setFromAccountNumber(original.getToAccountNumber());
        reversal.setToAccountNumber(original.getFromAccountNumber());

        accountService.creditAccount(original.getFromAccountNumber(), original.getAmount());
        accountService.debitAccount(original.getToAccountNumber(), original.getAmount());
      }
      default -> throw new IllegalStateException("Cannot reverse transaction type: " + original.getTransactionType());
    }

    // Save reversal
    transactionRepository.save(reversal);

    // Update original
    original.setReversed(true);
    original.setReversalReference(reversal.getTransactionReference());
    transactionRepository.save(original);

    ledgerService.reverseTransaction(
        original.getId(),
        reversal.getId(),
        reversedBy);

    return transactionMapper.toResponse(reversal);
  }

  @Override
  @Transactional(readOnly = true)
  public Page<TransactionResponse> getTransactionsForAccount(AccountTransactionFilter filter) {
    Pageable pageable = BuildPageable.createPageable(filter);

    String accountNumber = filter.getAccountNumber();

    Page<Transaction> page = transactionRepository
        .findByFromAccountNumberOrToAccountNumber(accountNumber, accountNumber, pageable);

    return page.map(transactionMapper::toResponse);
  }

  public List<TransactionResponse> getPendingTransactionsForAccount(UUID accountId) {
    Account account = accountRepository.findById(accountId)
        .orElseThrow(() -> new ResourceNotFoundException("Account not found"));

    return transactionRepository.findPendingTransactionsByAccountNumber(account.getAccountNumber())
        .stream()
        .map(transactionMapper::toResponse)
        .toList();
  }

  @Override
  @Transactional(readOnly = true)
  public Page<TransactionResponse> getTransactionHistory(AccountTransactionFilter request) {

    Pageable pageable = BuildPageable.createPageable(request);

    Page<Transaction> transactions = transactionRepository
        .findByFromAccountNumberOrToAccountNumber(request.getAccountNumber(), request.getAccountNumber(), pageable);

    return transactions.map(transactionMapper::toResponse);
  }

  @Override
  public Map<String, Object> getDailyTransactionSummary(String dateString) {
    LocalDate date;
    try {
      date = LocalDate.parse(dateString, DateTimeFormatter.ISO_DATE); // e.g., "2025-07-13"
    } catch (DateTimeParseException ex) {
      throw new IllegalArgumentException("Invalid date format. Use ISO format (yyyy-MM-dd)");
    }

    // Calculate full day range
    LocalDateTime startOfDay = date.atStartOfDay(); // 00:00:00
    LocalDateTime endOfDay = date.atTime(LocalTime.MAX); // 23:59:59.999999999

    // Fetch metrics
    BigDecimal inflow = transactionRepository.getDailyInflow(startOfDay, endOfDay);
    BigDecimal outflow = transactionRepository.getDailyOutflow(startOfDay, endOfDay);
    long totalTransactions = transactionRepository.countDailyTransactions(startOfDay, endOfDay);

    BigDecimal totalAmount = inflow.add(outflow);

    // Build response
    Map<String, Object> summary = new LinkedHashMap<>();
    summary.put("date", date);
    summary.put("totalTransactions", totalTransactions);
    summary.put("totalAmount", totalAmount);
    summary.put("totalInflow", inflow);
    summary.put("totalOutflow", outflow);

    return summary;
  }

  // private helper methods
  private void validateDepositRequest(DepositRequest request) {
    // Check if account exists
    Account account = accountRepository.findByAccountNumber(request.getAccountNumber())
        .orElseThrow(
            () -> new ResourceNotFoundException("Account not found with number: " + request.getAccountNumber()));

    // 2. Check if account is active
    if (!account.canCredit()) {
      throw new IllegalStateException("Account cannot be credited: " + request.getAccountNumber());
    }
  }

  private void validateWithdrawalRequest(WithdrawalRequest request) {
    // Check if account exists
    Account account = accountRepository.findByAccountNumber(request.getAccountNumber())
        .orElseThrow(
            () -> new ResourceNotFoundException("Account not found with number: " + request.getAccountNumber()));

    AccountConfiguration config = accountConfig
        .findByAccountType(account.getAccountType())
        .orElseThrow(() -> new ResourceNotFoundException(
            "Account configuration not found for type: " + account.getAccountType()));

    if (!account.canDebit(request.getAmount(), config)) {
      throw new IllegalStateException("Insufficient funds or overdraft limit exceeded." + request.getAccountNumber());
    }
  }

  private void validateTransferRequest(TransferRequest request) {

    String fromAccountNumber = request.getFromAccountNumber();
    String toAccountNumber = request.getToAccountNumber();

    if (fromAccountNumber.equals(toAccountNumber)) {
      throw new RuntimeException("Sender and receiver account must be different.");
    }

    Account fromAccount = accountRepository.findByAccountNumber(fromAccountNumber)
        .orElseThrow(() -> new ResourceNotFoundException("Sender account not found"));

    Account toAccount = accountRepository.findByAccountNumber(toAccountNumber)
        .orElseThrow(() -> new ResourceNotFoundException("Receiver account not found"));

    AccountConfiguration config = accountConfig.findByAccountType(fromAccount.getAccountType())
        .orElseThrow(() -> new ResourceNotFoundException("Account config not found"));

    if (!fromAccount.canDebit(request.getAmount(), config)) {
      throw new RuntimeException("Insufficient balance or overdraft limit exceeded.");
    }

    if (!toAccount.canCredit()) {
      throw new RuntimeException("Receiver account cannot be credited.");
    }

    // Check for duplicate transaction reference
    // 4. Check for duplicate deposit within 10 minutes
    LocalDateTime now = LocalDateTime.now();
    LocalDateTime tenMinutesAgo = now.minusMinutes(10);

    boolean isDuplicate = transactionRepository
        .existsByFromAccountNumberAndToAccountNumberAndAmountAndTransactionTypeAndTransactionDateBetween(
            request.getFromAccountNumber(),
            request.getToAccountNumber(),
            request.getAmount(),
            TransactionType.TRANSFER,
            tenMinutesAgo,
            now);

    if (isDuplicate) {
      throw new RuntimeException("Duplicate transfer detected within 10 minutes.");
    }
  }

  Transaction findTransactionEntityById(UUID transactionId) {
    return transactionRepository.findById(transactionId)
        .orElseThrow(() -> new ResourceNotFoundException("Transaction not found"));
  };

  public Transaction processTransaction(
      Transaction transaction,
      UUID initiatedBy,
      TransactionOperation operationIfApproved) {
    transaction.setInitiatedBy(initiatedBy);
    transaction = transactionRepository.save(transaction);

    try {
      log.info("transaction: {}", transaction);
      if (transaction.getAmount().compareTo(approvalThreshold) <= 0) {
        // Auto-approve
        operationIfApproved.execute(transaction);
        DoubleEntryRequest ledgerRequest = toDoubleEntryRequest(transaction);
        ledgerService.createDoubleEntry(ledgerRequest);

        transaction.setStatus(TransactionStatus.COMPLETED);
        transaction.setApprovedBy(initiatedBy);
      } else {
        transaction.setStatus(TransactionStatus.PROCESSING);
      }

      return transactionRepository.save(transaction);
    } catch (Exception e) {
      transaction.setStatus(TransactionStatus.FAILED);
      transactionRepository.save(transaction);
      throw new RuntimeException("Transaction failed: " + e.getMessage(), e);
    }
  }

  private DoubleEntryRequest toDoubleEntryRequest(Transaction tx) {
    DoubleEntryRequest.DoubleEntryRequestBuilder builder = DoubleEntryRequest.builder()
        .transactionId(tx.getId())
        .amount(tx.getAmount())
        .createdBy(tx.getInitiatedBy())
        .entryDate(tx.getTransactionDate());

    switch (tx.getTransactionType()) {
      case DEPOSIT: {
        builder
            .debitAccountNumber(bankControlAccount)
            .creditAccountNumber(tx.getToAccountNumber());
        break;
      }
      case WITHDRAWAL: {
        builder
            .debitAccountNumber(tx.getFromAccountNumber())
            .creditAccountNumber(bankControlAccount);
        break;
      }
      case TRANSFER: {
        builder
            .debitAccountNumber(tx.getFromAccountNumber())
            .creditAccountNumber(tx.getToAccountNumber());
        break;
      }
      default:
        throw new IllegalArgumentException("Unsupported transaction type: " + tx.getTransactionType());
    }

    return builder.build();
  }

}
