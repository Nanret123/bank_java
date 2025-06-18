package com.example.bank.common.dto;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonInclude;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "Generic API response DTO")
public class ApiResponseDto<T> {
  @Schema(description = "Indicates if the operation was successful", example = "true")
  private boolean success;

  @Schema(description = "Response message providing additional context", example = "Operation completed successfully")
  private String message;

  @Schema(description = "data requested")
  private T data;

  // @Schema(description = "Error message if the operation failed", example = "An error occurred")
  // private String error;

  @Schema(description = "Timestamp of the response", example = "2023-10-01T12:00:00")
  private LocalDateTime timestamp;

  // @Schema(description = "Path of the request that generated this response", example = "/api/v1/resource")
  // private String path;

  // @Schema(description = "HTTP status code of the response", example = "200")
  // private Integer statusCode;

  public static <T> ApiResponseDto<T> success(String message, T data) {
    return ApiResponseDto.<T>builder()
        .success(true)
        .message(message)
        .data(data)
        .timestamp(LocalDateTime.now())
        .build();
  }

  // public static <T> ApiResponseDto<T> error(String message, String error, String path, Integer statusCode) {
  //   return ApiResponseDto.<T>builder()
  //       .success(false)
  //       .message(message)
  //       .error(error)
  //       .timestamp(LocalDateTime.now())
  //       .path(path)
  //       .statusCode(statusCode)
  //       .build();
  // }
}
