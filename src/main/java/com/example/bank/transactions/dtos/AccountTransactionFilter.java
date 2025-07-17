package com.example.bank.transactions.dtos;

import com.example.bank.common.dto.PaginationRequest;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Filter parameters for getting account transactions")
public class AccountTransactionFilter extends PaginationRequest{
  @Schema(description = "account number", example = "1234567890")
  private String accountNumber;
}
