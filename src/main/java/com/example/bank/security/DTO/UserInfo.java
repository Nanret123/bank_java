package com.example.bank.security.DTO;

import java.time.LocalDateTime;
import java.util.UUID;

import com.example.bank.security.entity.UserRole;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "User information object containing details about the user")
public class UserInfo {
  @Schema(description = "Unique identifier for the user", example = "123e4567-e89b-12d3-a456-426614174000")
  private UUID id;

  @Schema(description = "Username of the user", example = "john_doe")
  private String username;

  @Schema(description = "Email address of the user", example = "johnDoe@example.com")
  private String email;

  @Schema(description = "Full name of the user", example = "John Doe")
  private String fullName;

  @Schema(description = "Role of the user in the system", example = "ADMIN")
  private UserRole role;

  @Schema(description = "Branch code associated with the user", example = "BR001")
  private String branchCode;

  @Schema(description = "Last login time of the user", example = "2023-10-01T12:00:00Z")
  private LocalDateTime lastLogin;

  @Schema(description="Indicates if user has changed their password", example = "false")
  private boolean isPasswordChanged;

  @Schema(description = "Indicates the time of creation", example = "true")
  private LocalDateTime createdAt;

  @Schema(description = "Indicates the time of last update", example = "2023-10-01T12:00:00Z")
    private LocalDateTime updatedAt;

}
