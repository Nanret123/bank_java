package com.example.bank.user.DTO;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Request DTO for updating user profile")
public class UpdateProfileRequest {

  @Schema(description = "User bio or description", example = "Software Engineer with 5 years of experience")
  private String bio;

  @Schema(description = "User address", example = "Main St, Springfield")
  private String address;
}
