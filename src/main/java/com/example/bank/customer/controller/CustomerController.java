package com.example.bank.customer.controller;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.example.bank.common.dto.ApiResponseDto;
import com.example.bank.common.util.ApiResponseUtil;
import com.example.bank.customer.dtos.CreateCustomerRequest;
import com.example.bank.customer.dtos.CustomerFilter;
import com.example.bank.customer.dtos.CustomerResponse;
import com.example.bank.customer.dtos.CustomerSearchRequest;
import com.example.bank.customer.dtos.CustomerStatisticsResponse;
import com.example.bank.customer.dtos.CustomerStatusUpdateRequest;
import com.example.bank.customer.dtos.CustomerSummaryResponse;
import com.example.bank.customer.dtos.RejectKycRequest;
import com.example.bank.customer.dtos.UpdateCustomerRequest;
import com.example.bank.customer.service.CustomerService;
import com.example.bank.customer.validation.dtos.CustomerValidationRequest;
import com.example.bank.security.service.UserPrincipal;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/customers")
@RequiredArgsConstructor
@Tag(name = "Customers", description = "Endpoints for managing customers")
public class CustomerController {

  private final CustomerService customerService;

  @PostMapping
  @Operation(summary = "Create a new customer")
  public ResponseEntity<ApiResponseDto<CustomerResponse>> createCustomer(
      @Valid @RequestBody CreateCustomerRequest request,
      @AuthenticationPrincipal UserPrincipal userDetails) {
    UUID userId = userDetails.getId();
    CustomerResponse response = customerService.createCustomer(request, userId);
    return ApiResponseUtil.success("Customer created successfully", response, HttpStatus.CREATED);
  }

  @GetMapping("/{id}")
  @Operation(summary = "Get customer by ID")
  public ResponseEntity<ApiResponseDto<CustomerResponse>> getCustomerById(@PathVariable UUID id) {
    CustomerResponse response = customerService.getCustomerById(id)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Customer not found"));
    return ApiResponseUtil.success("Customer retrieved successfully", response);
  }

  @PutMapping("/{id}")
  @Operation(summary = "Update customer")
  public ResponseEntity<ApiResponseDto<CustomerResponse>> updateCustomer(
      @PathVariable UUID id,
      @Valid @RequestBody UpdateCustomerRequest request,
      @RequestHeader("user-id") UUID userId) {
    CustomerResponse response = customerService.updateCustomer(id, request, userId);
    return ApiResponseUtil.success("Customer updated successfully", response);
  }

  // ✅ Soft delete customer
  @DeleteMapping("/{id}")
  @Operation(summary = "Soft delete customer")
  public ResponseEntity<ApiResponseDto<Void>> deleteCustomer(@PathVariable UUID id,
      @RequestHeader("user-id") UUID userId) {
    customerService.deleteCustomer(id, userId);
    return ApiResponseUtil.success("Customer deleted successfully", null);
  }

  @PatchMapping("/{id}/restore")
  @Operation(summary = "Restore a soft-deleted customer")
  public ResponseEntity<ApiResponseDto<Void>> restoreCustomer(@PathVariable UUID id,
      @RequestHeader("user-id") UUID userId) {
    customerService.restoreCustomer(id, userId);
    return ApiResponseUtil.success("Customer restored successfully");
  }

  @PatchMapping("/{id}/kyc/approve")
  @Operation(summary = "Approve customer KYC")
  public ResponseEntity<ApiResponseDto<Void>> approveKyc(@PathVariable UUID id) {
    customerService.approveCustomerKyc(id);
    return ApiResponseUtil.success("KYC approved successfully");
  }


  @PatchMapping("/{id}/kyc/reject")
  @Operation(summary = "Reject customer KYC")
  public ResponseEntity<ApiResponseDto<Void>> rejectKyc(
      @PathVariable UUID id,
      @RequestBody @Valid RejectKycRequest request) {
    customerService.rejectCustomerKyc(id, request.getReason());
    return ApiResponseUtil.success("KYC rejected successfully");
  }

  @GetMapping
  @Operation(summary = "Get all customers with filters (Admin Only)")
  public ResponseEntity<ApiResponseDto<Page<CustomerSummaryResponse>>> getAllCustomers(CustomerFilter filter) {
    Page<CustomerSummaryResponse> customers = customerService.getAllCustomers(filter);
    return ApiResponseUtil.success("Customers retrieved successfully", customers);
  }

  @GetMapping("/search")
  @Operation(summary = "Search customers (Admin Only)")
  public ResponseEntity<ApiResponseDto<Page<CustomerSummaryResponse>>> searchCustomers(CustomerSearchRequest request) {
    Page<CustomerSummaryResponse> customers = customerService.searchCustomers(request);
    return ApiResponseUtil.success("Search completed successfully", customers);
  }

  @GetMapping("/active")
  @Operation(summary = "Get all active (non-deleted) customers")
  public ResponseEntity<ApiResponseDto<Page<CustomerSummaryResponse>>> getAllActiveCustomers(CustomerFilter filter) {
    Page<CustomerSummaryResponse> customers = customerService.getAllActiveCustomers(filter);
    return ApiResponseUtil.success("Active customers retrieved successfully", customers);
  }

  @GetMapping("/search/active")
  @Operation(summary = "Search active (non-deleted) customers")
  public ResponseEntity<ApiResponseDto<Page<CustomerSummaryResponse>>> searchActiveCustomers(
      CustomerSearchRequest request) {
    Page<CustomerSummaryResponse> customers = customerService.searchActiveCustomers(request);
    return ApiResponseUtil.success("Search completed successfully", customers);
  }

  @PatchMapping("/{id}/status")
  @Operation(summary = "Update customer status (active/inactive/suspended)")
  public ResponseEntity<ApiResponseDto<CustomerResponse>> updateCustomerStatus(
      @PathVariable UUID id,
      @RequestBody @Valid CustomerStatusUpdateRequest request,
      @RequestHeader("user-id") UUID userId) {
    CustomerResponse response = customerService.updateCustomerStatus(id, request, userId);
    return ApiResponseUtil.success("Customer status updated successfully", response);
  }

  @PostMapping("/validate")
  @Operation(summary = "Validate customer data (e.g. uniqueness, structure)")
  public ResponseEntity<ApiResponseDto<Void>> validateCustomer(@RequestBody @Valid CustomerValidationRequest request) {
    customerService.validateCustomer(request);
    return ApiResponseUtil.success("Customer validation passed");
  }

  // ✅ Get statistics
  @GetMapping("/stats")
  @Operation(summary = "Get customer statistics")
  public ResponseEntity<ApiResponseDto<CustomerStatisticsResponse>> getCustomerStatistics() {
    CustomerStatisticsResponse stats = customerService.getCustomerStatistics();
    return ApiResponseUtil.success("Statistics retrieved successfully", stats);
  }
}
