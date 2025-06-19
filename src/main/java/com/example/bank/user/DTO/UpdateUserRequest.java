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

  @Schema(description = "phone number of the user to be updated", example = "+1234567890")
  private String phoneNumber;

  @Schema(description = "Bio or description of the user", example = "Experienced software developer with a passion for building scalable applications.")
  private String bio;

  @Schema(description = "Address of the user", example = "123 Main St, Springfield")
  private String address;

}