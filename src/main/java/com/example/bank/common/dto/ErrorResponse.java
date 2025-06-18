package com.example.bank.common.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Error response")
public class ErrorResponse {

    @Schema(description = "Error timestamp")
    private String timestamp;

    @Schema(description = "HTTP status code", example = "400")
    private int status;

    @Schema(description = "Error type", example = "Bad Request")
    private String error;

    @Schema(description = "Error message", example = "Invalid input data")
    private String message;

    @Schema(description = "Request path", example = "/api/auth/login")
    private String path;

    @Schema(description = "Validation errors, if any")
    private Map<String, String> validationErrors;

    @Schema(description = "List of field validation errors, if any")
    private List<ValidationError> fieldErrors;

    public ErrorResponse(String message, int status) {
    this.timestamp = LocalDateTime.now().toString();
    this.status = status;
    this.message = message;
}


}