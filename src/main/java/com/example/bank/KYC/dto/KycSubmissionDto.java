package com.example.bank.KYC.dto;

import java.util.Map;
import java.util.UUID;

import org.springframework.web.multipart.MultipartFile;

import com.example.bank.KYC.enums.DocumentType;
import com.example.bank.customer.enums.RiskLevel;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO for submitting KYC documents")
public class KycSubmissionDto {

  @Schema(description = "Bank Verification Number", example = "12345678901")
  @NotNull(message="Customer BVN is required")
  private String bvn;

  @Schema(description = "Risk level of the customer", example = "LOW")
  @NotNull(message="Customer risk level is required")
  private RiskLevel riskLevel;

  @NotNull(message = "Customer ID is required")
  @Schema(description = "Unique identifier for the customer", required = true, example = "3fa85f64-5717-4562-b3fc-2c963f66afa6")
  private UUID customerId;

  @Schema(description = "Map of document type to the uploaded document file", type = "object")
  private Map<DocumentType, MultipartFile> documents;

}
