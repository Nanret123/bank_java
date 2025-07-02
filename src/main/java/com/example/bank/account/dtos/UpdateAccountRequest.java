package com.example.bank.account.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

import com.example.bank.account.enums.AccountStatus;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Request payload for updating an existing account")
public class UpdateAccountRequest {

  @Schema(description = "Branch code where the account is now associated", example = "ABJ002")
  @Size(max = 10, message = "Branch code must not exceed 10 characters")
  private String branchCode;

  @Schema(description = "Updated status of the account", example = "SUSPENDED")
  private AccountStatus status;

  @Schema(description = "Optional remarks or notes for the update", example = "Account temporarily suspended due to KYC issues")
  private String remarks;

}
