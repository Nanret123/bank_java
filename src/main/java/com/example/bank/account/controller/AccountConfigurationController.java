package com.example.bank.account.controller;

import java.util.List;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.bank.account.dtos.AccountConfigurationResponse;
import com.example.bank.account.dtos.CreateAccountConfigurationRequest;
import com.example.bank.account.dtos.UpdateAccountConfigurationRequest;
import com.example.bank.account.services.AccountConfigurationService;
import com.example.bank.common.dto.ApiResponseDto;
import com.example.bank.common.util.ApiResponseUtil;

import java.util.UUID;

@RestController
@RequestMapping("/api/account-configurations")
@RequiredArgsConstructor
@Tag(name = "Account Configuration", description = "Manage configurations for different account types")
public class AccountConfigurationController {

    private final AccountConfigurationService accountConfigurationService;

    @PostMapping
    @Operation(summary = "Create account configuration")
    public ResponseEntity<ApiResponseDto<AccountConfigurationResponse>> createConfiguration(
            @Valid @RequestBody CreateAccountConfigurationRequest request) {
        AccountConfigurationResponse response = accountConfigurationService.createConfiguration(request);
        return ApiResponseUtil.success("Account configuration created successfully", response, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get configuration by ID")
    public ResponseEntity<ApiResponseDto<AccountConfigurationResponse>> getConfigurationById(@PathVariable UUID id) {
        AccountConfigurationResponse response = accountConfigurationService.getConfigurationById(id);
        return ApiResponseUtil.success("Configuration retrieved successfully", response);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update account configuration")
    public ResponseEntity<ApiResponseDto<AccountConfigurationResponse>> updateConfiguration(
            @PathVariable UUID id,
            @Valid @RequestBody UpdateAccountConfigurationRequest request) {
        AccountConfigurationResponse response = accountConfigurationService.updateConfiguration(id, request);
        return ApiResponseUtil.success("Account configuration updated successfully", response);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete account configuration")
    public ResponseEntity<ApiResponseDto<Void>> deleteConfiguration(@PathVariable UUID id) {
        accountConfigurationService.deleteConfiguration(id);
        return ApiResponseUtil.success("Account configuration deleted successfully", null);
    }

    @GetMapping
    @Operation(summary = "Get all account configurations")
    public ResponseEntity<ApiResponseDto<List<AccountConfigurationResponse>>> getAllConfigurations() {
        List<AccountConfigurationResponse> configurations = accountConfigurationService.getAllConfigurations();
        return ApiResponseUtil.success("Account configurations retrieved successfully", configurations);
    }

    @PatchMapping("/{id}/activate")
    @Operation(summary = "Activate an account configuration")
    public ResponseEntity<ApiResponseDto<AccountConfigurationResponse>> activateConfiguration(@PathVariable UUID id) {
        AccountConfigurationResponse response = accountConfigurationService.activateConfiguration(id);
        return ApiResponseUtil.success("Account configuration activated successfully", response);
    }

    @PatchMapping("/{id}/deactivate")
    @Operation(summary = "Deactivate an account configuration")
    public ResponseEntity<ApiResponseDto<AccountConfigurationResponse>> deactivateConfiguration(@PathVariable UUID id) {
        AccountConfigurationResponse response = accountConfigurationService.deactivateConfiguration(id);
        return ApiResponseUtil.success("Account configuration deactivated successfully", response);
    }
}
