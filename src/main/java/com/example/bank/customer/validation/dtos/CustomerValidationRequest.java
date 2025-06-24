package com.example.bank.customer.validation.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@Schema(description = "Request object for validating a customer's email and phone number")
public class CustomerValidationRequest {

  @Schema(description = "Email address of the customer", example = "customer@example.com")
  private String email;

  @Schema(description = "Phone number of the customer in international format", example = "+2348012345678")
  private String phoneNumber;
}