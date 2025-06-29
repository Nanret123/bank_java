package com.example.bank.KYC.dto;

import java.util.UUID;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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

  @NotNull(message = "Reviewer ID is required")
  @Schema(description = "ID of the user rejecting the KYC", example = "admin_123")
  private UUID reviewerId;

  @NotBlank(message = "Rejection reason is required")
  @Schema(description = "Reason for rejecting the KYC", example = "Uploaded documents are unclear")
  private String rejectionReason;
}