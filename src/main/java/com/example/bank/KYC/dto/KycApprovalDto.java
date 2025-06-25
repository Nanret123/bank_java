package com.example.bank.KYC.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class KycApprovalDto {
  @NotBlank(message = "Reviewer ID is required")
  private String reviewerId;

}