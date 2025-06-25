package com.example.bank.customer.dtos;

import com.example.bank.KYC.enums.KycStatus;
import com.example.bank.common.dto.PaginationRequest;
import com.example.bank.customer.enums.CustomerStatus;
import com.example.bank.customer.enums.CustomerType;
import com.example.bank.customer.enums.RiskLevel;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "Filter criteria for searching and retrieving customer records. Allows filtering by name, status, creation date, KYC status, status, risk rating, branch code,and other customer-related fields.")
public class CustomerFilter extends PaginationRequest {

  @Parameter(description = "Filter by status (optional)")
  private CustomerStatus status;

  @Parameter(description = "Filter by customer type (optional)")
  private CustomerType customerType;

  @Parameter(description = "Filter by branch code (optional)")
  String branchCode;

  @Parameter(description = "Filter by Kyc Status (optional)")
  private KycStatus kycStatus;

  @Parameter(description = "Filter by Risk Rating (optional)")
  private RiskLevel riskRating;

  @Schema(description = "Filter customers by creation date in yyyy-MM-dd format")
  private String createdAt;
}
