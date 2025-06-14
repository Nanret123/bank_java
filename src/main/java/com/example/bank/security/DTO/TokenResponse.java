package com.example.bank.security.DTO;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Response object for token operations, such as login")
public class TokenResponse {
  @Schema(description = "Access token for the user", example = "eyJraWQiOiJ4eXoiLCJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiJ9...")
  private String accessToken;

  @Schema(description="token type, typically 'Bearer'")
  @Builder.Default
  private String tokenType = "Bearer";

@Schema(description="expiration time of the access token in seconds", example = "3600")
  private Long expiresIn;
}
