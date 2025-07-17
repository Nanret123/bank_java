package com.example.bank.ledger.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.bank.ledger.entity.LedgerEntry;
import com.example.bank.ledger.enums.EntryType;
import com.example.bank.ledger.enums.LedgerEntryStatus;

@Repository
public interface LedgerEntryRepository extends JpaRepository<LedgerEntry, UUID> {
  Optional<LedgerEntry> findByEntryId(String entryId);

  List<LedgerEntry> findByTransactionId(UUID transactionId);

  Page<LedgerEntry> findByAccountNumber(String accountNumber, Pageable pageable);

  List<LedgerEntry> findByTransactionIdOrderByEntryDateAsc(UUID transactionId);

  @Query("SELECT l FROM LedgerEntry l WHERE " +
      "(:transactionId IS NULL OR l.transactionId = :transactionId) AND " +
      "(:accountNumber IS NULL OR l.accountNumber = :accountNumber) AND " +
      "(:entryType IS NULL OR l.entryType = :entryType) AND " +
      "(:status IS NULL OR l.status = :status) AND " +
      "(:reversed IS NULL OR l.reversed = :reversed) AND " +
      "(:fromDate IS NULL OR l.entryDate >= :fromDate) AND " +
      "(:toDate IS NULL OR l.entryDate <= :toDate) AND " +
      "(:createdBy IS NULL OR l.createdBy = :createdBy)")
  Page<LedgerEntry> findByFilters(@Param("transactionId") UUID transactionId,
      @Param("accountNumber") String accountNumber,
      @Param("entryType") EntryType entryType,
      @Param("status") LedgerEntryStatus status,
      @Param("reversed") Boolean reversed,
      @Param("fromDate") LocalDateTime fromDate,
      @Param("toDate") LocalDateTime toDate,
      @Param("createdBy") String createdBy,
      Pageable pageable);

  boolean existsByTransactionIdAndReversedFalse(UUID transactionId);
}