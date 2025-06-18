package com.example.bank.common.exception;

import lombok.Getter;

@Getter
public class ResourceNotFoundException extends RuntimeException {
  private final String resourceName;
  private final String fieldName;

  public ResourceNotFoundException(String message) {
        super(message);
        this.resourceName = null;
        this.fieldName = null;
    }
}
