package com.example.bank.common.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(description = "Generic API response DTO")
public class ApiResponseDto<T> {
  @Schema(description = "Indicates if the operation was successful", example = "true")
  private boolean success;

  @Schema(description = "Response message providing additional context", example = "Operation completed successfully")
  private String message;

  @Schema(description="data requested")
  private T data;
}
