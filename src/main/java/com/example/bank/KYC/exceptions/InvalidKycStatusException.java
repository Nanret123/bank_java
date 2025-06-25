package com.example.bank.KYC.exceptions;

public class InvalidKycStatusException extends RuntimeException {
  public InvalidKycStatusException(String message) {
    super(message);
  }
}
