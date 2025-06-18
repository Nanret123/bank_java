package com.example.bank.user.DTO;

import com.example.bank.security.entity.UserRole;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Request object for updating an existing user")
public class UpdateUserRequest {
  @Schema(description = "Full name of the user to be updated", example = "John Doe")
  private String fullName;

  @Schema(description = "Email address of the user to be updated", example = "johndoe@example.com")
  @Email(message = "Invalid email format")
  private String email;

  @Schema(description = "Role of the user to be updated", example = "TELLER")
  private UserRole role;

  @Schema(description = "Branch code where the user is assigned", example = "BR001")
  private String branchCode;

  @Schema(description = "Indicates if the user is active", example = "true")
  private Boolean isActive;
}