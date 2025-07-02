package com.example.bank.account.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.bank.account.entity.AccountConfiguration;
import com.example.bank.account.enums.AccountType;

@Repository
public interface AccountConfigurationRepository extends JpaRepository<AccountConfiguration, UUID> {

  Optional<AccountConfiguration> findByAccountType(AccountType accountType);

  boolean existsByAccountType(AccountType accountType);
}
