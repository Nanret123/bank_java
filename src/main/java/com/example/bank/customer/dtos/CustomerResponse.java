package com.example.bank.customer.dtos;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

import com.example.bank.KYC.enums.KycStatus;
import com.example.bank.customer.enums.CustomerStatus;
import com.example.bank.customer.enums.CustomerType;
import com.example.bank.customer.enums.VerificationStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "Customer Response Data Transfer Object")
public class CustomerResponse {
  @Schema(description = "Unique identifier of the customer")
  private UUID id;

  @Schema(description = "Title of the customer, e.g., Mr, Mrs, Dr")
  private String title;

  @Schema(description = "Customer's first name")
  private String firstName;

  @Schema(description = "Customer's middle name")
  private String middleName;

  @Schema(description = "Customer's last name")
  private String lastName;

  @Schema(description = "Full name of the customer")
  private String fullName;

  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
  @Schema(description = "Date of birth of the customer in yyyy-MM-dd format")
  private LocalDate dateOfBirth;

  @Schema(description = "Customer's age")
  private Integer age;

  @Schema(description = "Gender of the customer")
  private String gender;

  @Schema(description = "Marital status of the customer")
  private String maritalStatus;

  @Schema(description = "Nationality of the customer")
  private String nationality;

  @Schema(description = "Customer's email address")
  private String email;

  @Schema(description = "Customer's primary phone number")
  private String phoneNumber;

  @Schema(description = "Alternative contact number")
  private String alternativePhone;

  @Schema(description = "Residential address of the customer")
  private String residentialAddress;

  @Schema(description = "City of residence")
  private String city;

  @Schema(description = "State of residence")
  private String state;

  @Schema(description = "Country of residence")
  private String country;

  @Schema(description = "Bank Verification Number")
  private String bvn;

  @Schema(description = "Type of identification (e.g., Passport, Driver's License)")
  private String idType;

  @Schema(description = "Identification number")
  private String idNumber;

  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
  @Schema(description = "Expiry date of the ID in yyyy-MM-dd format")
  private LocalDate idExpiryDate;

  @Schema(description = "Occupation of the customer")
  private String occupation;

  @Schema(description = "Type of customer (e.g., Individual, Corporate)")
  private CustomerType customerType;

  @Schema(description = "Account status (e.g., Active, Inactive)")
  private CustomerStatus status;

  @Schema(description = "Customer risk rating")
  private String riskRating;

  @Schema(description = "KYC verification status")
  private KycStatus kycStatus;

  @Schema(description = "Name of emergency contact")
  private String emergencyContactName;

  @Schema(description = "Relationship with emergency contact")
  private String emergencyContactRelationship;

  @Schema(description = "Phone number of emergency contact")
  private String emergencyContactPhone;

  @Schema(description = "Branch code where the customer was registered")
  private String branchCode;

  @Schema(description = "Verification status of the customer", example = "Pending")
  private VerificationStatus verificationStatus;

  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
  @Schema(description = "Timestamp when the customer record was created")
  private LocalDateTime createdAt;

  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
  @Schema(description = "Timestamp when the customer record was last updated")
  private LocalDateTime updatedAt;

  @Schema(description = "User who created the record")
  private String createdBy;
}
