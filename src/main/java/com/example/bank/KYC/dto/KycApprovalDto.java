package com.example.bank.KYC.dto;

import java.util.UUID;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor

@Schema(description = "DTO for approving a KYC submission")
public class KycApprovalDto {

  @NotBlank(message = "Reviewer ID is required")
  @Schema(description = "ID of the user reviewing the KYC", example = "admin_123")
  private UUID reviewerId;
}