package com.example.bank.transactions.controller;

import java.util.Map;
import java.util.UUID;

import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.bank.common.dto.ApiResponseDto;
import com.example.bank.common.util.ApiResponseUtil;
import com.example.bank.security.service.UserPrincipal;
import com.example.bank.transactions.dtos.AccountTransactionFilter;
import com.example.bank.transactions.dtos.DepositRequest;
import com.example.bank.transactions.dtos.TransactionFilter;
import com.example.bank.transactions.dtos.TransactionResponse;
import com.example.bank.transactions.dtos.TransferRequest;
import com.example.bank.transactions.dtos.WithdrawalRequest;
import com.example.bank.transactions.service.TransactionService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/transactions")
@RequiredArgsConstructor
@Tag(name = "Transaction Management", description = "Endpoints for managing transactions")
public class TransactionController {

  private final TransactionService transactionService;

  @PostMapping("/deposit")
  @Operation(summary = "Process a deposit transaction")
  public ResponseEntity<ApiResponseDto<TransactionResponse>> processDeposit(
      @Valid @RequestBody DepositRequest request,
      @AuthenticationPrincipal UserPrincipal user) {

    TransactionResponse response = transactionService.processDeposit(request, user.getId());
    return ApiResponseUtil.success("Deposit successful", response, HttpStatus.CREATED);
  }

  @PostMapping("/withdraw")
  @Operation(summary = "Process a withdrawal transaction")
  public ResponseEntity<ApiResponseDto<TransactionResponse>> processWithdrawal(
      @Valid @RequestBody WithdrawalRequest request,
      @AuthenticationPrincipal UserPrincipal user) {

    TransactionResponse response = transactionService.processWithdrawal(request, user.getId());
    return ApiResponseUtil.success("Withdrawal successful", response, HttpStatus.CREATED);
  }

  @PostMapping("/transfer")
  @Operation(summary = "Process a transfer transaction")
  public ResponseEntity<ApiResponseDto<TransactionResponse>> processTransfer(
      @Valid @RequestBody TransferRequest request,
      @AuthenticationPrincipal UserPrincipal user) {

    TransactionResponse response = transactionService.processTransfer(request, user.getId());
    return ApiResponseUtil.success("Transfer successful", response, HttpStatus.CREATED);
  }

  @GetMapping("/{id}")
  @Operation(summary = "Get transaction by ID")
  public ResponseEntity<ApiResponseDto<TransactionResponse>> getTransactionById(
      @PathVariable UUID id) {

    TransactionResponse response = transactionService.getTransactionById(id);
    return ApiResponseUtil.success("Transaction retrieved", response);
  }

  @GetMapping("/reference/{reference}")
  @Operation(summary = "Get transaction by reference")
  public ResponseEntity<ApiResponseDto<TransactionResponse>> getTransactionByReference(
      @PathVariable String reference) {

    TransactionResponse response = transactionService.getTransactionByReference(reference);
    return ApiResponseUtil.success("Transaction retrieved", response);
  }

  @GetMapping("/transactions/summary")
  public ResponseEntity<ApiResponseDto<Map<String, Object>>> getDailySummary(@RequestParam String date) {
    Map<String, Object> result = transactionService.getDailyTransactionSummary(date);
    return ApiResponseUtil.success("Daily transaction summary", result);
  }

  @GetMapping("/transactions/history")
public ResponseEntity<ApiResponseDto<Page<TransactionResponse>>> getHistory(@ModelAttribute AccountTransactionFilter request) {
  Page<TransactionResponse> result =  transactionService.getTransactionHistory(request);
  return ApiResponseUtil.success("Transaction history retrieved", result);
}

  @GetMapping
  @Operation(summary = "Get transactions with filter")
  public ResponseEntity<ApiResponseDto<Page<TransactionResponse>>> getTransactions(
      @Valid @ModelAttribute @ParameterObject TransactionFilter filter) {

    Page<TransactionResponse> response = transactionService.getTransactions(filter);
    return ApiResponseUtil.success("Transactions fetched", response);
  }

  @GetMapping("/account")
  @Operation(summary = "Get transactions for an account")
  public ResponseEntity<ApiResponseDto<Page<TransactionResponse>>> getTransactionsForAccount(
      @Valid @ModelAttribute @ParameterObject AccountTransactionFilter filter) {

    Page<TransactionResponse> response = transactionService.getTransactionsForAccount(filter);
    return ApiResponseUtil.success("Account transactions fetched", response);
  }

  @PostMapping("/{id}/approve")
  @Operation(summary = "Approve a transaction")
  public ResponseEntity<ApiResponseDto<TransactionResponse>> approveTransaction(
      @PathVariable UUID id,
      @AuthenticationPrincipal UserPrincipal user) {

    TransactionResponse response = transactionService.approveTransaction(id, user.getId());
    return ApiResponseUtil.success("Transaction approved", response);
  }

  @PostMapping("/{id}/reverse")
  @Operation(summary = "Reverse a transaction")
  public ResponseEntity<ApiResponseDto<TransactionResponse>> reverseTransaction(
      @PathVariable UUID id,
      @AuthenticationPrincipal UserPrincipal user) {

    TransactionResponse response = transactionService.reverseTransaction(id, user.getId());
    return ApiResponseUtil.success("Transaction reversed", response);
  }
}
