package com.example.bank.user.DTO;

import com.example.bank.security.entity.UserRole;

import io.swagger.v3.oas.annotations.Parameter;
import lombok.Data;

@Data
public class UserFilter {
  @Parameter(description = "Page number", example = "0")
  private int page = 0;

  @Parameter(description = "Number of items per page", example = "10")
  private int size = 10;

  @Parameter(description = "Sort by field", example = "id")
  private String sortBy = "id";

  @Parameter(description = "Sort direction (asc or desc)", example = "asc")
  private String sortDirection = "asc";

  @Parameter(description = "Role of the user", example = "TELLER")
   private UserRole role;

  @Parameter(description = "Branch code associated with the user", example = "BR001")
    private String branchCode;

  @Parameter(description = "Indicates if the user is active", example = "true")
    private Boolean active;

  @Parameter(description = "Search term for full name", example = "john")
  private String searchTerm;

}
