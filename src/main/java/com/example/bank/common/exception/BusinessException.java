package com.example.bank.common.exception;

import org.springframework.http.HttpStatus;

import lombok.Getter;

@Getter
public class BusinessException extends RuntimeException {
  private final HttpStatus httpStatus;
  private final String code;

  public BusinessException(String message) {
    super(message);
    this.httpStatus = HttpStatus.BAD_REQUEST;
    this.code = "BUSINESS_ERROR";
  }

}
