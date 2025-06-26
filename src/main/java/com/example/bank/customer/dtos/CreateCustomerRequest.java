package com.example.bank.customer.dtos;

import java.time.LocalDate;

import com.example.bank.customer.enums.CustomerType;
import com.example.bank.customer.enums.Gender;
import com.example.bank.customer.enums.MaritalStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description="Data Transfer Object to create a new customer")
public class CreateCustomerRequest {
  @Schema(description = "First name of the customer", example = "John")
  @NotBlank(message = "First name is required")
  private String firstName;

  @Schema(description = "Middle name of the customer", example = "Michael")
  @NotBlank(message = "Middle name is required")
  private String middleName;

  @Schema(description = "Last name of the customer", example = "Doe")
  @NotBlank(message = "Last name is required")
  private String lastName;

  @Schema(description = "Title of the customer", example = "Mr")
  private String title;

  @Schema(description = "Date of birth (format: yyyy-MM-dd)", example = "1990-01-01")
  @NotNull(message = "Date of birth is required")
  @Past(message = "Date of birth must be in the past")
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
  private LocalDate dateOfBirth;

  @Schema(description = "Gender of the customer", example = "Male")
  @NotNull(message = "Gender is required")
  private Gender gender;

  @Schema(description = "Marital status of the customer", example = "MARRIED")
  @NotNull(message = "Marital status is required")
  private MaritalStatus maritalStatus;

  @Schema(description = "Nationality of the customer", example = "Nigerian")
  @NotBlank(message ="Nationality is required")
  private String nationality;

  @Schema(description = "Email address of the customer", example = "john.doe@example.com")
  @Email(message = "Please provide a valid email address")
  private String email;

  @Schema(description = "Primary phone number of the customer", example = "+2348012345678")
  @NotBlank(message = "Phone number is required")
  @Pattern(regexp = "^[+]?[0-9]{10,15}$", message = "Please provide a valid phone number")
  private String phoneNumber;

  @Schema(description = "Alternative phone number of the customer", example = "+2348098765432")
  @Pattern(regexp = "^[+]?[0-9]{10,15}$", message = "Please provide a valid alternative phone number")
  @NotBlank(message = "Alternative phone number is required")
  private String alternativePhone;

  @Schema(description = "Residential address of the customer", example = "123 Broad Street")
  @NotBlank(message = "Residential address is required")
  private String residentialAddress;

  @Schema(description = "City of residence", example = "Abuja")
  @NotBlank(message = "City is required")
  private String city;

  @Schema(description = "State of residence", example = "FCT")
  @NotBlank(message = "State is required")
  private String state;

  @Schema(description = "Country of residence", example = "Nigeria")
  @NotBlank(message = "Country is required")
  private String country;

  @Schema(description = "Occupation of the customer", example = "Software Engineer")
  @NotBlank(message = "Occupation is required")
  private String occupation;

  @Schema(description = "Customer type", example = "INDIVIDUAL")
  @NotNull(message = "Customer type is required")
  private CustomerType customerType;

  @Schema(description = "Emergency contact name", example = "Jane Doe")
  @NotBlank(message = "Emergency contact name is required")
  private String emergencyContactName;

  @Schema(description = "Relationship with emergency contact", example = "Sister")
  @NotBlank(message = "Emergency contact relationship is required")
  private String emergencyContactRelationship;

  @Schema(description = "Phone number of emergency contact", example = "+2347011122233")
  @NotBlank(message = "Emergency contact phone number is required")
  private String emergencyContactPhone;

  @Schema(description = "Branch code associated with the customer", example = "BR001")
  @NotBlank(message = "Branch code is required")
  private String branchCode;
}