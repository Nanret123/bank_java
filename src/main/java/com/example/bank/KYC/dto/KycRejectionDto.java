package com.example.bank.KYC.dto;

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
@Schema(description = "DTO for rejecting a KYC submission")
public class KycRejectionDto {

  @NotBlank(message = "Reviewer ID is required")
  @Schema(description = "ID of the user rejecting the KYC", example = "admin_123")
  private String reviewerId;

  @NotBlank(message = "Rejection reason is required")
  @Schema(description = "Reason for rejecting the KYC", example = "Uploaded documents are unclear")
  private String rejectionReason;
}