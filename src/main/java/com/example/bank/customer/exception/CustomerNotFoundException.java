package com.example.bank.customer.exception;

public class CustomerNotFoundException extends RuntimeException {
  public CustomerNotFoundException(String message) {
    super(message);
  }
}
