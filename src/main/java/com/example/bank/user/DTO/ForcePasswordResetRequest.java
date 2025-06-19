package com.example.bank.user.DTO;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Request to force password reset")
public class ForcePasswordResetRequest {
  @Schema(description = "New password for the user", example = "NewPassword123!")
  @NotBlank(message = "New password is required")
  @Size(min = 8, message = "Password must be at least 8 characters")
  private String newPassword;

  @Schema(description = "Old password for the user", example = "OldPassword123!")
  @NotBlank(message = "Old password is required")
  private String oldPassword;

}
