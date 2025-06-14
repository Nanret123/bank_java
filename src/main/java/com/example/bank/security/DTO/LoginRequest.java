package com.example.bank.security.DTO;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Request object for user login")
public class LoginRequest {
  @NotBlank(message = "username is required")
  @Schema(description = "Username of the user", example = "john_doe")
  private String username;

  @NotBlank(message = "password is required")
  @Schema(description = "Password of the user", example = "securePassword123")
  private String password;

  @Schema(description = "Device information for security purposes", example = "Dell Laptop, Windows 10")
  @NotBlank(message = "deviceInfo is required")
  private String deviceInfo;
}
