package com.example.bank.account.dtos;

import com.example.bank.account.enums.AccountStatus;
import com.example.bank.account.enums.AccountType;
import com.example.bank.common.dto.PaginationRequest;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "Filter criteria for searching and retrieving customer account records. Allows filtering by account type, status and, branch code.")
public class AccountFilterRequest extends PaginationRequest {
    @Parameter(description = "Filter by status (optional)")
  private AccountStatus status;

  @Parameter(description = "Filter by account type (optional)")
  private AccountType accountType;

  @Parameter(description = "Filter by branch code (optional)")
  String branchCode;
}
