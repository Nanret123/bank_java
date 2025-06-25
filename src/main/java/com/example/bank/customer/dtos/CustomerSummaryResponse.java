package com.example.bank.customer.dtos;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

import com.example.bank.KYC.enums.KycStatus;
import com.example.bank.customer.enums.RiskLevel;
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
@Schema(description = "Summary response for customer profile")
public class CustomerSummaryResponse {

    @Schema(description = "Unique identifier of the customer", example = "f47ac10b-58cc-4372-a567-0e02b2c3d479")
    private UUID id;

    @Schema(description = "Full name of the customer", example = "Jane Doe")
    private String fullName;

    @Schema(description = "Email address of the customer", example = "jane.doe@example.com")
    private String email;

    @Schema(description = "Phone number of the customer", example = "+2348012345678")
    private String phoneNumber;

    @Schema(description = "Type of customer (e.g., Individual, Corporate)", example = "Individual")
    private String customerType;

    @Schema(description = "Account status (e.g., Active, Inactive)", example = "Active")
    private String status;

    @Schema(description = "KYC verification status", example = "Completed")
    private KycStatus kycStatus;

    @Schema(description = "Customer risk rating", example = "Low")
    private RiskLevel riskRating;

    @Schema(description = "Branch code associated with the customer", example = "BR001")
    private String branchCode;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @Schema(description = "Customer's date of birth in yyyy-MM-dd format", example = "1990-05-20")
    private LocalDate dateOfBirth;

    @Schema(description = "Customer's age", example = "34")
    private Integer age;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "Date and time when the customer profile was created", example = "2025-06-23 10:45:00")
    private LocalDateTime createdAt;

}