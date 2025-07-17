package com.example.bank.ledger.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.bank.common.dto.ApiResponseDto;
import com.example.bank.common.util.ApiResponseUtil;
import com.example.bank.ledger.dtos.CreateLedgerEntryRequest;
import com.example.bank.ledger.dtos.DoubleEntryRequest;
import com.example.bank.ledger.dtos.LedgerEntryFilter;
import com.example.bank.ledger.dtos.LedgerEntryResponse;
import com.example.bank.ledger.interfaces.ILedgerEntry;
import com.example.bank.security.service.UserPrincipal;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/ledger-entries")
@RequiredArgsConstructor
@Tag(name = "Ledger Entries", description = "Ledger double-entry and reversal operations(only used in transaction operations)")
public class LedgerEntryController {

  private final ILedgerEntry ledgerService;

  @PostMapping
  @Operation(summary = "Create a single ledger entry")
  public ResponseEntity<ApiResponseDto<LedgerEntryResponse>> createEntry(
      @Valid @RequestBody CreateLedgerEntryRequest request) {

    LedgerEntryResponse response = ledgerService.createEntry(request);
    return ApiResponseUtil.success("Ledger entry created successfully", response, HttpStatus.CREATED);
  }

  @PostMapping("/double")
  @Operation(summary = "Create double ledger entry (debit & credit)")
  public ResponseEntity<ApiResponseDto<List<LedgerEntryResponse>>> createDoubleEntry(
      @Valid @RequestBody DoubleEntryRequest request) {

    List<LedgerEntryResponse> response = ledgerService.createDoubleEntry(request);
    return ApiResponseUtil.success("Double entry created successfully", response, HttpStatus.CREATED);
  }

  @GetMapping("/{entryId}")
  @Operation(summary = "Get ledger entry by Entry ID")
  public ResponseEntity<ApiResponseDto<LedgerEntryResponse>> getByEntryId(@PathVariable String entryId) {
    LedgerEntryResponse response = ledgerService.getByEntryId(entryId);
    return ApiResponseUtil.success("Ledger entry retrieved", response);
  }

  @GetMapping("/transaction/{transactionId}")
  @Operation(summary = "Get ledger entries by Transaction ID")
  public ResponseEntity<ApiResponseDto<List<LedgerEntryResponse>>> getByTransactionId(
      @PathVariable UUID transactionId) {

    List<LedgerEntryResponse> response = ledgerService.getByTransactionId(transactionId);
    return ApiResponseUtil.success("Ledger entries for transaction retrieved", response);
  }

  @GetMapping
  @Operation(summary = "Get paginated ledger entries using filter")
  public ResponseEntity<ApiResponseDto<Page<LedgerEntryResponse>>> getEntries(
      @Valid LedgerEntryFilter filter) {

    Page<LedgerEntryResponse> response = ledgerService.getEntries(filter);
    return ApiResponseUtil.success("Filtered ledger entries retrieved", response);
  }

  @PostMapping("/{entryId}/reverse")
  @Operation(summary = "Reverse a single ledger entry")
  public ResponseEntity<ApiResponseDto<LedgerEntryResponse>> reverseEntry(
      @PathVariable String entryId,
      @AuthenticationPrincipal UserPrincipal user) {

    LedgerEntryResponse response = ledgerService.reverseEntry(entryId, user.getId());
    return ApiResponseUtil.success("Ledger entry reversed", response);
  }

  @PostMapping("/reverse-transaction/{transactionId}")
  @Operation(summary = "Reverse all entries for a transaction")
  public ResponseEntity<ApiResponseDto<List<LedgerEntryResponse>>> reverseTransaction(
      @PathVariable UUID transactionId,
      @AuthenticationPrincipal UserPrincipal user) {

    UUID reversalTransactionId = UUID.randomUUID(); // Generate a new reversal transaction ID
    List<LedgerEntryResponse> response = ledgerService.reverseTransaction(transactionId, reversalTransactionId,
        user.getId());
    return ApiResponseUtil.success("Ledger transaction reversed", response);
  }

  @GetMapping("/validate/{transactionId}")
  @Operation(summary = "Validate if a transaction is balanced in the ledger")
  public ResponseEntity<ApiResponseDto<String>> validateTransaction(@PathVariable UUID transactionId) {
    ledgerService.validateTransaction(transactionId);
    return ApiResponseUtil.success("Ledger transaction is balanced", "Valid");
  }
}
