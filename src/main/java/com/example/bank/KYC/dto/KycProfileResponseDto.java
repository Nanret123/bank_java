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

  @Schema(description = "Residential address of the customer", example = "123 Broad Street")
  private String address;

  @Schema(description = "City of residence", example = "Lagos")
  private String city;

  @Schema(description = "State of residence", example = "Lagos State")
  private String state;

  @Schema(description = "Country of residence", example = "Nigeria")
  private String country;

  @Schema(description = "Postal or ZIP code", example = "100001")
  private String postalCode;

  @Schema(description = "Customer's occupation", example = "Software Engineer")
  private String occupation;

  @Schema(description = "Name of the customer's employer", example = "Tech Ltd.")
  private String employerName;

  @Schema(description = "Source of income", example = "Salary")
  private String sourceOfIncome;

  @Schema(description = "Any additional information provided by the customer")
  private String additionalInfo;

  @Schema(description = "Current status of the KYC profile", example = "APPROVED")
  private KycStatus status;

  @Schema(description = "Reason for rejection, if KYC is rejected", example = "Incomplete documents")
  private String rejectionReason;

  @Schema(description = "Name or ID of the reviewer", example = "admin_user")
  private String reviewedBy;

  @Schema(description = "Date and time when the profile was reviewed")
  private LocalDateTime reviewedAt;

  @Schema(description = "List of uploaded KYC documents")
  private List<KycDocumentDto> documents;

  @Schema(description = "Date and time when the KYC profile was created")
  private LocalDateTime createdAt;

  @Schema(description = "Date and time when the KYC profile was last updated")
  private LocalDateTime updatedAt;

}