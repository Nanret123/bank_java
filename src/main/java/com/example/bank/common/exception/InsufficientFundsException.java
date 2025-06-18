package com.example.bank.common.exception;

public class InsufficientFundsException extends BusinessException {
  public InsufficientFundsException(String message) {
        super(message);
  }
}
