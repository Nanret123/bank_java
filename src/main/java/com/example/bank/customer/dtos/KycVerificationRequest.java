package com.example.bank.customer.dtos;

import com.example.bank.customer.enums.RiskLevel;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Request payload for updating KYC verification status and risk level")
public class KycVerificationRequest {

  @Schema(description = "Indicates whether the customer's BVN has been successfully verified", example = "true")
  private Boolean isBvnVerified;

  @Schema(description = "Indicates whether the customer's government-issued ID has been verified", example = "false")
  private Boolean isIdVerified;

  @Schema(description = "Risk level assigned to the customer based on AML assessment", example = "LOW")
  private RiskLevel riskLevel;
}