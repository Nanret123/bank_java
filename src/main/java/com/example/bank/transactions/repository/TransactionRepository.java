package com.example.bank.transactions.repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.bank.transactions.entity.Transaction;
import com.example.bank.transactions.enums.TransactionType;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, UUID>, JpaSpecificationExecutor<Transaction> {

  // Find by transaction reference
  Optional<Transaction> findByTransactionReference(String transactionReference);

  // Check if transaction exists
  boolean existsByTransactionReference(String transactionReference);

  // Find recent transactions
  @Query("SELECT t FROM Transaction t WHERE t.transactionDate >= :since ORDER BY t.transactionDate DESC")
  List<Transaction> findRecentTransactions(@Param("since") LocalDateTime since);

  @Query("""
          SELECT SUM(t.amount) FROM Transaction t
          WHERE t.fromAccountNumber = :accountNumber
            AND t.transactionType = :transactionType
            AND t.createdAt BETWEEN :start AND :end
      """)
  Optional<BigDecimal> getTodaysTransactionTotal(
      @Param("accountNumber") String accountNumber,
      @Param("transactionType") TransactionType transactionType,
      @Param("start") LocalDateTime start,
      @Param("end") LocalDateTime end);

  Boolean existsByFromAccountNumberAndToAccountNumberAndAmountAndTransactionTypeAndTransactionDateBetween(
      String fromAccountNumber,
      String toAccountNumber,
      BigDecimal amount,
      TransactionType transactionType,
      LocalDateTime startDate,
      LocalDateTime endDate);

  Page<Transaction> findByFromAccountNumberOrToAccountNumber(
      String fromAccount, String toAccount, Pageable pageable);

  @Query("""
          SELECT t FROM Transaction t
          WHERE (t.fromAccountNumber = :accountNumber OR t.toAccountNumber = :accountNumber)
            AND t.status = 'PENDING'
          ORDER BY t.transactionDate DESC
      """)
  List<Transaction> findPendingTransactionsByAccountNumber(@Param("accountNumber") String accountNumber);

  // Inflow: money INTO an account (e.g., deposits, incoming transfers)
  @Query("""
          SELECT COALESCE(SUM(t.amount), 0) FROM Transaction t
          WHERE t.toAccountNumber IS NOT NULL
            AND t.transactionDate BETWEEN :start AND :end
            AND t.status = 'COMPLETED'
      """)
  BigDecimal getDailyInflow(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

  // Outflow: money LEAVING an account (e.g., withdrawals, transfers)
  @Query("""
          SELECT COALESCE(SUM(t.amount), 0) FROM Transaction t
          WHERE t.fromAccountNumber IS NOT NULL
            AND t.transactionDate BETWEEN :start AND :end
            AND t.status = 'COMPLETED'
      """)
  BigDecimal getDailyOutflow(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

  // Count of all transactions for the day
  @Query("""
          SELECT COUNT(t) FROM Transaction t
          WHERE t.transactionDate BETWEEN :start AND :end
            AND t.status = 'COMPLETED'
      """)
  Long countDailyTransactions(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

}