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
@Schema(description = "Response object for user login")
public class LoginResponse {
  @Schema(description = "Access token for the user")
  private String accessToken;

  @Schema(description = "Refresh token for the user")
  private String refreshToken;

  @Schema(description = "Type of the token, typically 'Bearer'")
  @Builder.Default
  private String tokenType = "Bearer";

  @Schema(description = "Expiration time of the access token in seconds")
  private Long expiresIn;

  @Schema(description = "User information associated with the login")
  private UserInfo user;

}
