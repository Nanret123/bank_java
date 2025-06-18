package com.example.bank.user.DTO;

import java.util.UUID;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Response object for user creation containing details about the newly created user")
public class UserCreationResponse {

  @Schema(description = "Unique identifier of the newly created user", example = "123e4567-e89b-12d3-a456-426614174000")
  private UUID id;

  @Schema(description = "Full name of the newly created user", example = "John Doe")
  private String fullname;

  @Schema(description="Branch code of the user", example="BR001")
  private String branchCode;

  @Schema(description = "Username of the newly created user", example = "john_doe")
  private String username;

  @Schema(description = "temporary password for the newly created user", example = "tempPass123")
  private String temporaryPassword;

  @Schema(description="the email of the user", example="johndoe@email.com")
  private String email;

  @Schema(description = "Indicates if the user is required to change their password upon first login", example = "true")
  private boolean requiresPasswordChange;


}
