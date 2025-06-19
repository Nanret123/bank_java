package com.example.bank.user.DTO;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "User Profile DTO")
public class UserProfile {
  @Schema(description = "User ID", example = "123e4567-e89b-12d3-a456-426614174000")
  private String userId;

  @Schema(description = "Username", example = "john_doe")
  private String username;

  @Schema(description = "Branch code", example = "BR001")
  private String branchCode;

  @Schema(description = "Phone number", example = "+1234567890")
  private String phoneNumber;

  @Schema(description = "Address", example = "123 Main St, Springfield")
  private String address;

  @Schema(description = "Email address", example = "johndoe@email.com")
  private String email;

  @Schema(description = "Full name of the user", example = "John Doe")
  private String fullName;

  @Schema(description = "User bio or description", example = "Software Engineer with 5 years of experience")
  private String bio;

  @Schema(description = "Profile picture URL", example = "https://example.com/profile.jpg")
  private String avatarUrl;

}
