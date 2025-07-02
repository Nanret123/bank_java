package com.example.bank.account.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.example.bank.account.entity.Account;
import com.example.bank.account.enums.AccountStatus;
import com.example.bank.account.enums.AccountType;

@Repository
public interface AccountRepository extends JpaRepository<Account, UUID>, JpaSpecificationExecutor<Account> {

  Optional<Account> findByAccountNumber(String accountNumber);

  List<Account> findByBranchCode(String branchCode);

  List<Account> findByCustomerId(UUID customerId);

  List<Account> findByStatus(AccountStatus status);

  List<Account> findByAccountType(AccountType accountType);

  boolean existsByAccountNumber(String accountNumber);

  boolean existsByCustomerIdAndAccountType(UUID customerId, AccountType accountType);

  Page<Account> findByCreatedAtBetween(LocalDateTime startDate, LocalDateTime endDate, Pageable pageable);

}