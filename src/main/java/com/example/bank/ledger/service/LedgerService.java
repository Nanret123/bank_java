package com.example.bank.ledger.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.bank.common.exception.ResourceNotFoundException;
import com.example.bank.common.util.BuildPageable;
import com.example.bank.ledger.dtos.CreateLedgerEntryRequest;
import com.example.bank.ledger.dtos.DoubleEntryRequest;
import com.example.bank.ledger.dtos.LedgerEntryFilter;
import com.example.bank.ledger.dtos.LedgerEntryResponse;
import com.example.bank.ledger.entity.LedgerEntry;
import com.example.bank.ledger.enums.EntryType;
import com.example.bank.ledger.enums.LedgerEntryStatus;
import com.example.bank.ledger.interfaces.ILedgerEntry;
import com.example.bank.ledger.mapper.LedgerEntryMapper;
import com.example.bank.ledger.repository.LedgerEntryRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class LedgerService implements ILedgerEntry {

  private final LedgerEntryRepository ledgerEntryRepository;
  private final LedgerEntryMapper ledgerEntryMapper;

  @Override
  public LedgerEntryResponse createEntry(CreateLedgerEntryRequest request) {

    LedgerEntry entry = ledgerEntryMapper.toEntity(request);
    entry.setEntryId(generateEntryId());
    entry.setStatus(LedgerEntryStatus.PENDING);

    LedgerEntry savedEntry = ledgerEntryRepository.save(entry);

    return ledgerEntryMapper.toResponse(savedEntry);
  }

  @Override
  public List<LedgerEntryResponse> createDoubleEntry(DoubleEntryRequest request) {

    // Validate that amounts balance
    validateDoubleEntryRequest(request);

    // Create debit entry
    LedgerEntry debitEntry = createLedgerEntry(
        request.getTransactionId(),
        request.getDebitAccountNumber(),
        EntryType.DEBIT,
        request.getAmount(),
        request.getCreatedBy(),
        request.getEntryDate());

    // Create credit entry
    LedgerEntry creditEntry = createLedgerEntry(
        request.getTransactionId(),
        request.getCreditAccountNumber(),
        EntryType.CREDIT,
        request.getAmount(),
        request.getCreatedBy(),
        request.getEntryDate());

    List<LedgerEntry> savedEntries = ledgerEntryRepository.saveAll(List.of(debitEntry, creditEntry));

    return ledgerEntryMapper.toResponseList(savedEntries);
  }

  @Override
  @Transactional(readOnly = true)
  public LedgerEntryResponse getByEntryId(String entryId) {

    LedgerEntry entry = ledgerEntryRepository.findByEntryId(entryId)
        .orElseThrow(() -> new ResourceNotFoundException("Ledger entry not found with ID: " + entryId));

    return ledgerEntryMapper.toResponse(entry);
  }

  @Override
  @Transactional(readOnly = true)
  public List<LedgerEntryResponse> getByTransactionId(UUID transactionId) {

    List<LedgerEntry> entries = ledgerEntryRepository.findByTransactionIdOrderByEntryDateAsc(transactionId);

    if (entries.isEmpty()) {
      throw new ResourceNotFoundException("No ledger entries found for transaction: " + transactionId);
    }

    return ledgerEntryMapper.toResponseList(entries);
  }

  @Override
  @Transactional(readOnly = true)
  public Page<LedgerEntryResponse> getEntries(LedgerEntryFilter filter) {

    Pageable pageable = BuildPageable.createPageable(filter);

    Page<LedgerEntry> entries = ledgerEntryRepository.findByFilters(
        filter.getTransactionId(),
        filter.getAccountNumber(),
        filter.getEntryType(),
        filter.getStatus(),
        filter.getReversed(),
        filter.getFromDateTime(),
        filter.getToDateTime(),
        filter.getCreatedBy(),
        pageable);

    return entries.map(ledgerEntryMapper::toResponse);
  }

  @Override
  public LedgerEntryResponse reverseEntry(String entryId, UUID reversedBy) {

    LedgerEntry originalEntry = ledgerEntryRepository.findByEntryId(entryId)
        .orElseThrow(() -> new ResourceNotFoundException("Ledger entry not found with ID: " + entryId));

    if (originalEntry.getReversed()) {
      throw new IllegalStateException("Entry is already reversed: " + entryId);
    }

    UUID reversalTransactionId = UUID.randomUUID();

    // Create reversal entry
      LedgerEntry reversalEntry = createReversalEntry(originalEntry, reversalTransactionId, reversedBy);
      
    LedgerEntry savedReversalEntry = ledgerEntryRepository.save(reversalEntry);

    // Mark original entry as reversed
    originalEntry.setReversed(true);
    originalEntry.setReversalEntryId(savedReversalEntry.getEntryId());
    originalEntry.setReversedAt(LocalDateTime.now());
    ledgerEntryRepository.save(originalEntry);

    return ledgerEntryMapper.toResponse(savedReversalEntry);
  }

  @Override
  public List<LedgerEntryResponse> reverseTransaction(UUID originalTransactionId, UUID reversalTransactionId, UUID reversedBy) {

    List<LedgerEntry> originalEntries = ledgerEntryRepository.findByTransactionIdOrderByEntryDateAsc(originalTransactionId);

    if (originalEntries.isEmpty()) {
      throw new ResourceNotFoundException("No entries found for transaction: " + originalTransactionId);
    }

    // Check if any entry is already reversed
    boolean hasReversedEntries = originalEntries.stream().anyMatch(LedgerEntry::getReversed);
    if (hasReversedEntries) {
      throw new IllegalStateException("Transaction contains already reversed entries: " + originalTransactionId);
    }

    // Create reversal entries
    List<LedgerEntry> reversalEntries = originalEntries.stream()
        .map(entry -> createReversalEntry(entry, reversalTransactionId, reversedBy))
        .collect(Collectors.toList());

    List<LedgerEntry> savedReversalEntries = ledgerEntryRepository.saveAll(reversalEntries);

    // Mark original entries as reversed
    for (int i = 0; i < originalEntries.size(); i++) {
      LedgerEntry originalEntry = originalEntries.get(i);
      originalEntry.setReversed(true);
      originalEntry.setReversalEntryId(savedReversalEntries.get(i).getEntryId());
    }
    ledgerEntryRepository.saveAll(originalEntries);

    return ledgerEntryMapper.toResponseList(savedReversalEntries);
  }

  private String generateEntryId() {
    return UUID.randomUUID().toString();
  }

  @Override
  @Transactional(readOnly = true)
  public void validateTransaction(UUID transactionId) {

    List<LedgerEntry> entries = ledgerEntryRepository.findByTransactionId(transactionId);

    if (entries.isEmpty()) {
      throw new ResourceNotFoundException("No entries found for transaction: " + transactionId);
    }

    // validate balance
    BigDecimal totalBalance = entries.stream()
        .map(entry -> entry.getEntryType() == EntryType.DEBIT
            ? entry.getAmount()
            : entry.getAmount().negate())
        .reduce(BigDecimal.ZERO, BigDecimal::add);

    if (totalBalance.compareTo(BigDecimal.ZERO) != 0) {
      throw new IllegalStateException(
          String.format("Transaction %s is not balanced. Imbalance: %s", transactionId, totalBalance));
    }

  }

  private LedgerEntry createLedgerEntry(UUID transactionId, String accountNumber,
      EntryType entryType, BigDecimal amount,
      UUID createdBy, LocalDateTime entryDate) {
    return LedgerEntry.builder()
        .entryId(generateEntryId())
        .transactionId(transactionId)
        .accountNumber(accountNumber)
        .entryType(entryType)
        .amount(amount)
        .entryDate(entryDate != null ? entryDate : LocalDateTime.now())
        .status(LedgerEntryStatus.POSTED)
        .reversed(false)
        .createdBy(createdBy)
        .build();
  }

  private void validateDoubleEntryRequest(DoubleEntryRequest request) {
    if (request.getDebitAccountNumber().equals(request.getCreditAccountNumber())) {
      throw new IllegalArgumentException("Debit and credit accounts cannot be the same");
    }

    if (request.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
      throw new IllegalArgumentException("Amount must be positive");
    }
  }

  private LedgerEntry createReversalEntry(LedgerEntry originalEntry, UUID reversalTransactionId, UUID reversedBy) {
    // Reverse the entry type
    EntryType reversalEntryType = originalEntry.getEntryType() == EntryType.DEBIT ? EntryType.CREDIT : EntryType.DEBIT;

    return LedgerEntry.builder()
        .entryId(generateEntryId())
        .transactionId(reversalTransactionId)
        .accountNumber(originalEntry.getAccountNumber())
        .entryType(reversalEntryType)
        .amount(originalEntry.getAmount())
        .entryDate(LocalDateTime.now())
        .status(LedgerEntryStatus.REVERSED)
        .reversed(false)
        .originalEntryId(originalEntry.getEntryId())
        .createdBy(reversedBy)
        .build();
  }
}
