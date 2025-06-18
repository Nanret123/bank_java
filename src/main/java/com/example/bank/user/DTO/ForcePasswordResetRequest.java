package com.example.bank.user.DTO;

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
public class ForcePasswordResetRequest {
  @NotBlank(message = "New password is required")
  @Size(min = 8, message = "Password must be at least 8 characters")
  private String newPassword;

  @Builder.Default
  private boolean forcePasswordChangeOnLogin = true;

}
