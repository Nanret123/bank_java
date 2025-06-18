package com.example.bank.common.util;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.example.bank.common.dto.ApiResponseDto;

public class ApiResponseUtil {

  private ApiResponseUtil() {
    // Prevent instantiation
  }

  // 200 OK with data
  public static <T> ResponseEntity<ApiResponseDto<T>> success(String message, T data) {
    ApiResponseDto<T> response = ApiResponseDto.<T>builder()
        .success(true)
        .message(message)
        .data(data)
        .build();
    return ResponseEntity.ok(response);
  }

  // 200 OK without data
  public static ResponseEntity<ApiResponseDto<Void>> success(String message) {
    ApiResponseDto<Void> response = ApiResponseDto.<Void>builder()
        .success(true)
        .message(message)
        .data(null)
        .build();
    return ResponseEntity.ok(response);
  }

  // Custom status with data
  public static <T> ResponseEntity<ApiResponseDto<T>> success(String message, T data, HttpStatus status) {
    ApiResponseDto<T> response = ApiResponseDto.<T>builder()
        .success(true)
        .message(message)
        .data(data)
        .build();
    return ResponseEntity.status(status).body(response);
  }

}
