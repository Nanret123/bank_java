package com.example.bank.security.DTO;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Request object for refreshing access tokens")
public class RefreshTokenRequest {
  @Schema(description = "Refresh token to obtain a new access token", example = "eyJraWQiOiJ4eXoiLCJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiJ9...")
  @NotBlank(message = "refreshToken is required")
  private String refreshToken;
}
