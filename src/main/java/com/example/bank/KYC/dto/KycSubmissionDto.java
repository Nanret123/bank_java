package com.example.bank.KYC.dto;

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


@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@Schema(description = "DTO for submitting KYC documents")
public class KycSubmissionDto {

  @Schema(description = "Risk level of the customer", example = "LOW")
  @NotNull(message="Customer risk level is required")
  private RiskLevel riskLevel;

  @NotNull(message = "Customer ID is required")
  @Schema(description = "Unique identifier for the customer", required = true, example = "3fa85f64-5717-4562-b3fc-2c963f66afa6")
  private UUID customerId;

  @Schema(description="Document type for verification")
  @NotNull(message="Document type is required")
  private DocumentType documentType;

 @Schema(description = "Upload of ID document")
 @NotNull(message="document required for verification")
  private MultipartFile document;


}
