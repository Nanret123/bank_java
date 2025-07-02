package com.example.bank.account.services;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.bank.account.dtos.AccountConfigurationResponse;
import com.example.bank.account.dtos.CreateAccountConfigurationRequest;
import com.example.bank.account.dtos.UpdateAccountConfigurationRequest;
import com.example.bank.account.entity.AccountConfiguration;
import com.example.bank.account.mapper.AccountConfigurationMapper;
import com.example.bank.account.repository.AccountConfigurationRepository;
import com.example.bank.common.exception.ResourceNotFoundException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class AccountConfigurationService {

  private final AccountConfigurationRepository accountConfigurationRepository;
  private final AccountConfigurationMapper accountConfigurationMapper;

  // Core Configuration Operations
  public AccountConfigurationResponse createConfiguration(CreateAccountConfigurationRequest request) {

    // Check if configuration already exists for this account type
    if (accountConfigurationRepository.existsByAccountType(request.getAccountType())) {
      throw new IllegalStateException(
          "Configuration already exists for account type: " + request.getAccountType());
    }

    AccountConfiguration config = AccountConfiguration.builder()
        .accountType(request.getAccountType())
        .minimumBalance(request.getMinimumBalance())
        .maximumBalance(request.getMaximumBalance())
        .dailyWithdrawalLimit(request.getDailyWithdrawalLimit())
        .monthlyTransactionLimit(request.getMonthlyTransactionLimit())
        .interestRate(request.getInterestRate())
        .maintenanceFee(request.getMaintenanceFee())
        .overdraftAllowed(request.getOverdraftAllowed())
        .overdraftLimit(request.getOverdraftLimit())
        .build();

    AccountConfiguration savedConfig = accountConfigurationRepository.save(config);

    return accountConfigurationMapper.toResponse(savedConfig);
  }

  @Transactional(readOnly = true)
  public AccountConfigurationResponse getConfigurationById(UUID id) {
    AccountConfiguration config = findConfigurationById(id);
    return accountConfigurationMapper.toResponse(config);
  }

  public AccountConfigurationResponse updateConfiguration(UUID id, UpdateAccountConfigurationRequest request) {

    AccountConfiguration config = findConfigurationById(id);

    // Use the mapper to update the fields
    accountConfigurationMapper.updateConfigurationFromRequest(request, config);

    config.setUpdatedAt(LocalDateTime.now());

    AccountConfiguration savedConfig = accountConfigurationRepository.save(config);

    return accountConfigurationMapper.toResponse(savedConfig);
  }

  public void deleteConfiguration(UUID id) {
    AccountConfiguration config = findConfigurationById(id);
    accountConfigurationRepository.delete(config);

  }

  // Configuration Management
  @Transactional(readOnly = true)
  public List<AccountConfigurationResponse> getAllConfigurations() {
    List<AccountConfiguration> configurations = accountConfigurationRepository.findAll();
    return configurations.stream()
        .map(accountConfigurationMapper::toResponse)
        .collect(Collectors.toList());
  }

  private AccountConfiguration findConfigurationById(UUID id) {
    return accountConfigurationRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Configuration not found"));
  }

  public AccountConfigurationResponse activateConfiguration(UUID id) {
        
        AccountConfiguration config = findConfigurationById(id);
        config.setIsActive(true);
        config.setUpdatedAt(LocalDateTime.now());
        
        AccountConfiguration savedConfig = accountConfigurationRepository.save(config);
        log.info("Account configuration activated successfully");
        
        return accountConfigurationMapper.toResponse(savedConfig);
    }

    public AccountConfigurationResponse deactivateConfiguration(UUID id) {
        
        AccountConfiguration config = findConfigurationById(id);
        config.setIsActive(false);
        config.setUpdatedAt(LocalDateTime.now());
        
        AccountConfiguration savedConfig = accountConfigurationRepository.save(config);
        
        return accountConfigurationMapper.toResponse(savedConfig);
    }


}