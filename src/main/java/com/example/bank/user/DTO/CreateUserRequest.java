package com.example.bank.user.DTO;

import com.example.bank.security.entity.UserRole;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Request object for creating a new user")
public class CreateUserRequest {
    @Schema(description = "Username for the new user", example = "john_doe")
    @NotBlank(message = "Username is required")
    private String username;

    @Schema(description = "Full name of the new user", example = "John Doe")
    @NotBlank(message = "Full name is required")
    private String fullName;

    @Schema(description = "Email address of the new user", example = "johndoe@example.com")
    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;

    @Schema(description = "Role of the new user", example = "TELLER")
    @NotNull(message = "Role is required")
    private UserRole role;

    @Schema(description = "Branch code where the user will be assigned", example = "BR001")
    @NotBlank(message = "Branch code is required")
    private String branchCode;

    @Schema(description = "Phone number of the new user", example = "+1234567890")
    @NotBlank(message = "Phone number is required")
    private String phoneNumber;

    @Schema(description = "Address of the new user", example = "Main St, Springfield")
    @NotBlank(message = "Address is required")
    private String address;
}
