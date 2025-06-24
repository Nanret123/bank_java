package com.example.bank.customer.validation.dtos;

import java.util.ArrayList;
import java.util.List;

public class ValidationResult {
  private boolean valid = true;
  private List<String> errors = new ArrayList<>();

  public void addError(String error) {
    this.valid = false;
    this.errors.add(error);
  }

  public boolean isValid() {
    return valid;
  }

  public List<String> getErrors() {
    return errors;
  }

  public String getErrorsAsString() {
    return String.join("; ", errors);
  }
}
