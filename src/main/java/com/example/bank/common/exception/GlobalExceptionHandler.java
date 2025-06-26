package com.example.bank.common.exception;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.example.bank.common.dto.ErrorResponse;

import jakarta.servlet.http.HttpServletRequest;

@ControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(ResourceNotFoundException.class)
  public ResponseEntity<ErrorResponse> handleNotFound(ResourceNotFoundException ex) {
    return new ResponseEntity<>(new ErrorResponse(ex.getMessage(), 404), HttpStatus.NOT_FOUND);
  }

  @ExceptionHandler(InsufficientFundsException.class)
  public ResponseEntity<ErrorResponse> handleFunds(InsufficientFundsException ex) {
    return new ResponseEntity<>(new ErrorResponse(ex.getMessage(), 400), HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(BusinessException.class)
  public ResponseEntity<ErrorResponse> handleBusiness(BusinessException ex) {
    return new ResponseEntity<>(new ErrorResponse(ex.getMessage(), 400), HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(InvalidFileException.class)
  public ResponseEntity<ErrorResponse> handleInvalidFileException(InvalidFileException ex) {

    ErrorResponse error = ErrorResponse.builder()
        .timestamp(LocalDateTime.now().toString())
        .status(HttpStatus.BAD_REQUEST.value())
        .error("Invalid File")
        .message(ex.getMessage())
        .build();

    return ResponseEntity.badRequest().body(error);
  }

  @ExceptionHandler(ValidationException.class)
  public ResponseEntity<ErrorResponse> handleValidation(
      ValidationException ex,
      HttpServletRequest request) {
    ErrorResponse errorResponse = ErrorResponse.builder()
        .timestamp(LocalDateTime.now().toString())
        .status(HttpStatus.BAD_REQUEST.value())
        .error(HttpStatus.BAD_REQUEST.getReasonPhrase())
        .message(ex.getMessage())
        .path(request.getRequestURI())
        .build();

    return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ErrorResponse> handleMethodArgumentNotValid(
      MethodArgumentNotValidException ex,
      HttpServletRequest request) {
    String message = ex.getBindingResult()
        .getFieldErrors()
        .stream()
        .map(error -> error.getField() + ": " + error.getDefaultMessage())
        .findFirst()
        .orElse("Validation error");

    ErrorResponse errorResponse = ErrorResponse.builder()
        .timestamp(LocalDateTime.now().toString())
        .status(HttpStatus.BAD_REQUEST.value())
        .error(HttpStatus.BAD_REQUEST.getReasonPhrase())
        .message(message)
        .path(request.getRequestURI())
        .build();

    return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<ErrorResponse> handleGeneric(Exception ex) {
    return new ResponseEntity<>(new ErrorResponse("Something went wrong", 500), HttpStatus.INTERNAL_SERVER_ERROR);
  }
}