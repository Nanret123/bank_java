package com.example.bank.KYC.dto;

import java.util.Map;

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
@Schema(description = "DTO for updating KYC documents")
public class KycUpdateDto {

  @Schema(description = "Bank Verification Number", example = "12345678901")
  private String bvn;

  @Schema(description = "Risk level of the customer", example = "LOW")
  private RiskLevel riskLevel;

  @Schema(description = "Map of document type to the uploaded document file", type = "object")
  private Map<DocumentType, MultipartFile> documents;

}
