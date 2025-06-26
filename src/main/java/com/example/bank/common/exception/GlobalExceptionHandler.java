package com.example.bank.common.exception;

import java.time.LocalDateTime;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import com.example.bank.common.dto.ApiResponseDto;
import com.example.bank.common.dto.ErrorResponse;
import com.example.bank.customer.exception.CustomerNotFoundException;

import jakarta.servlet.http.HttpServletRequest;

@ControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(ResourceNotFoundException.class)
  public ResponseEntity<ErrorResponse> handleNotFound(ResourceNotFoundException ex) {
    return new ResponseEntity<>(new ErrorResponse(ex.getMessage(), 404), HttpStatus.NOT_FOUND);
  }
  @ExceptionHandler(CustomerNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleCustomerNotFound(CustomerNotFoundException ex) {
       return new ResponseEntity<>(new ErrorResponse(ex.getMessage(), 404), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
public ResponseEntity<?> handleTypeMismatch(MethodArgumentTypeMismatchException ex) {
    String message = String.format(
        "Invalid value '%s' for parameter '%s'. Expected one of: %s",
        ex.getValue(),
        ex.getName()
    );

    return ResponseEntity.badRequest().body(Map.of(
        "success", false,
        "message", message,
        "status", 400
    ));
}

@ExceptionHandler(HttpMessageNotReadableException.class)
public ResponseEntity<?> handleEnumJsonParseError(HttpMessageNotReadableException ex) {
    String message = "Invalid enum value or request body format.";

    // Optional: log cause for debugging
    if (ex.getCause() != null) {
        System.out.println("Root cause: " + ex.getCause());
    }

    return ResponseEntity.badRequest().body(Map.of(
        "success", false,
        "message", message,
        "status", 400
    ));
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