package com.example.bank.KYC.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import com.example.bank.KYC.enums.KycStatus;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Response DTO representing a user's KYC profile")
public class KycProfileResponseDto {
  
  @Schema(description = "Unique identifier for the customer", example = "1001")
  private UUID customerId;

  @Schema(description = "Customer's first name", example = "John")
  private String customerFirstName;

  @Schema(description = "Customer's last name", example = "Doe")
  private String customerLastName;

  @Schema(description = "Customer's email address", example = "john.doe@example.com")
  private String customerEmail;

  @Schema(description = "Customer's phone number", example = "+2348012345678")
  private String customerPhone;

  @Schema(description = "Customer's date of birth", example = "1990-05-12")
  private String dateOfBirth;

  @Schema(description = "Customer's nationality", example = "Nigerian")
  private String nationality;

  @Schema(description = "City of residence", example = "Lagos")
  private String city;

  @Schema(description = "State of residence", example = "Lagos State")
  private String state;

  @Schema(description = "Country of residence", example = "Nigeria")
  private String country;

  @Schema(description = "Customer's occupation", example = "Software Engineer")
  private String occupation;

  @Schema(description = "Bank Verification Number")
  private String bvn;

  @Schema(description = "Current status of the KYC profile", example = "APPROVED")
  private KycStatus status;

  @Schema(description = "List of uploaded KYC documents")
  private List<KycDocumentDto> documents;

  private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

}