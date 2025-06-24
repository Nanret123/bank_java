package com.example.bank.customer.validation.dtos;

import java.util.ArrayList;
import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Response object indicating the existence of customer attributes")
public class ValidationResponse {

  @Schema(description = "True if the email exists in the system", example = "false")
  private boolean emailExists;

  @Schema(description = "True if the phone number exists in the system", example = "true")
  private boolean phoneNumberExists;

  public boolean isValid() {
    return !emailExists && !phoneNumberExists;
  }

  public List<String> getErrors() {
    List<String> errors = new ArrayList<>();
    if (emailExists)
      errors.add("Email already exists");
    if (phoneNumberExists)
      errors.add("Phone number already exists");
    return errors;
  }

   public String getErrorsAsString() {
        return String.join("; ", getErrors());
    }
}