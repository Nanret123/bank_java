package com.example.bank.user.DTO;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Request DTO for updating user profile")
public class UpdateProfileRequest {
  @Schema(description="User full name", example = "John Doe")
  @NotBlank(message = "Full name is required")
  private String fullName;

  @Schema(description="User phone number", example = "+1234567890")
  @Size(max = 15, message = "Phone number cannot exceed 15 characters")
  private String phoneNumber;

 @Schema(description="User bio or description", example = "Software Engineer with 5 years of experience")
  private String bio;
}
