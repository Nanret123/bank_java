package com.example.bank.account.controller;

import java.util.List;
import java.util.UUID;

import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.bank.account.dtos.AccountFilterRequest;
import com.example.bank.account.dtos.AccountResponse;
import com.example.bank.account.dtos.AccountSummaryResponse;
import com.example.bank.account.dtos.BalanceResponse;
import com.example.bank.account.dtos.CreateAccountRequest;
import com.example.bank.account.dtos.TotalAccountSummary;
import com.example.bank.account.dtos.UpdateAccountRequest;
import com.example.bank.account.enums.AccountStatus;
import com.example.bank.account.services.AccountService;
import com.example.bank.common.dto.ApiResponseDto;
import com.example.bank.common.util.ApiResponseUtil;
import com.example.bank.security.service.UserPrincipal;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/accounts")
@RequiredArgsConstructor
@Validated
@Slf4j
@Tag(name = "Accounts", description = "Endpoints for managing customer accounts")
public class AccountController {

  private final AccountService accountService;

  @Operation(summary = "Create a new account")
  @PostMapping
  public ResponseEntity<ApiResponseDto<AccountResponse>> createAccount(
      @Valid @RequestBody CreateAccountRequest request, @AuthenticationPrincipal UserPrincipal userDetails) {
        UUID userId = userDetails.getId();
    AccountResponse response = accountService.createAccount(request, userId);
    return ApiResponseUtil.success("Account created successfully", response, HttpStatus.CREATED);
  }

  @Operation(summary = "Get account by ID")
  @GetMapping("/{id}")
  public ResponseEntity<ApiResponseDto<AccountResponse>> getAccountById(@PathVariable UUID id) {
    AccountResponse response = accountService.getAccountById(id);
    return ApiResponseUtil.success("Account retrieved successfully", response);
  }

  @Operation(summary = "Get account by account number")
  @GetMapping("/number/{accountNumber}")
  public ResponseEntity<ApiResponseDto<AccountResponse>> getAccountByNumber(@PathVariable String accountNumber) {
    AccountResponse response = accountService.getAccountByNumber(accountNumber);
    return ApiResponseUtil.success("Account retrieved successfully", response);
  }

  @Operation(summary = "Update an account")
  @PutMapping("/{id}")
  public ResponseEntity<ApiResponseDto<AccountResponse>> updateAccount(
      @PathVariable UUID id,
      @Valid @RequestBody UpdateAccountRequest request, @AuthenticationPrincipal UserPrincipal userDetails) {
        UUID userId = userDetails.getId();
    AccountResponse response = accountService.updateAccount(id, request, userId);
    return ApiResponseUtil.success("Account updated successfully", response);
  }

  @Operation(summary = "Delete an account")
  @DeleteMapping("/{id}")
  public ResponseEntity<ApiResponseDto<Void>> deleteAccount(
      @PathVariable UUID id,
      @AuthenticationPrincipal UserPrincipal userDetails) {
        UUID userId = userDetails.getId();
    accountService.deleteAccount(id, userId);
    return ApiResponseUtil.success("Account deleted successfully", null);
  }

  @GetMapping
  @Operation(summary = "Get all accounts with filters and pagination")
  public ResponseEntity<ApiResponseDto<Page<AccountSummaryResponse>>> getAllAccounts(
      @Valid @ModelAttribute @ParameterObject AccountFilterRequest filter) {
    Page<AccountSummaryResponse> accounts = accountService.getAccounts(filter);
    return ApiResponseUtil.success("Accounts retrieved successfully", accounts);
  }

  @GetMapping("/{id}/balance")
  @Operation(summary = "Get account balance")
  public ResponseEntity<ApiResponseDto<BalanceResponse>> getAccountBalance(@PathVariable UUID id) {
    BalanceResponse response = accountService.getAccountBalance(id);
    return ApiResponseUtil.success("Account balance retrieved successfully", response);
  }

  @GetMapping("/summary")
  @Operation(summary = "Get accounts summary")
  public ResponseEntity<ApiResponseDto<TotalAccountSummary>> getAccountsSummary() {
    TotalAccountSummary summary = accountService.getAccountsSummary();
    return ApiResponseUtil.success("Account summary retrieved successfully", summary);
  }

  @Operation(summary = "Get all accounts for a customer")
  @GetMapping("/customer/{customerId}")
  public ResponseEntity<ApiResponseDto<List<AccountSummaryResponse>>> getAccountsByCustomerId(
      @PathVariable UUID customerId) {
    List<AccountSummaryResponse> responses = accountService.getAccountsByCustomerId(customerId);
    return ApiResponseUtil.success("Accounts retrieved successfully", responses);
  }

  @PatchMapping("/{id}/status")
  @Operation(summary = "Change account status")
  public ResponseEntity<ApiResponseDto<AccountResponse>> changeAccountStatus(
      @PathVariable UUID id,
      @RequestParam AccountStatus status, @AuthenticationPrincipal UserPrincipal userDetails) {
        UUID userId = userDetails.getId();
    AccountResponse response = accountService.changeAccountStatus(id, status, userId);
    return ApiResponseUtil.success("Account status changed successfully", response);
  }
}