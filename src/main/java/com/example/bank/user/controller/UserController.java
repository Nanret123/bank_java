package com.example.bank.user.controller;

import java.util.UUID;

import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.bank.common.dto.ApiResponseDto;
import com.example.bank.common.util.ApiResponseUtil;
import com.example.bank.security.service.UserPrincipal;
import com.example.bank.user.DTO.AvatarUploadResponse;
import com.example.bank.user.DTO.CreateUserRequest;
import com.example.bank.user.DTO.UpdateProfileRequest;
import com.example.bank.user.DTO.UpdateUserRequest;
import com.example.bank.user.DTO.UserCreationResponse;
import com.example.bank.user.DTO.UserFilter;
import com.example.bank.user.DTO.UserProfile;
import com.example.bank.user.DTO.UserResponse;
import com.example.bank.user.service.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/users")
@Tag(name = "User Management", description = "APIs for managing users")
@RequiredArgsConstructor
public class UserController {
  private final UserService userService;

  @PostMapping("/create")
  // @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
  @Operation(summary = "Create a new user (admin/manager only)")
  public ResponseEntity<ApiResponseDto<UserCreationResponse>> createUser(
      @Valid @RequestBody CreateUserRequest request) {
    UserCreationResponse userInfo = userService.createUser(request);

    return ApiResponseUtil.success("User created successfully", userInfo, HttpStatus.CREATED);
  }

  @DeleteMapping("/{userId}")
  @PreAuthorize("hasRole('ADMIN')")
  @Operation(summary = "Deactivate a user (admin only)")
  public ResponseEntity<ApiResponseDto<Void>> deactivateUser(@PathVariable UUID userId) {
    userService.deactivateUser(userId);
    return ApiResponseUtil.success("User deactivated successfully");
  }

  @GetMapping()
  @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
  @Operation(summary = "Get users including pageable and filter options (admin/manager only)")
  public ResponseEntity<ApiResponseDto<Page<UserResponse>>> getAllUsers(
      @Valid @ModelAttribute @ParameterObject UserFilter filter) {
    Page<UserResponse> users = userService.getAllUsers(filter);
    return ApiResponseUtil.success("Users retrieved successfully", users);

  }

  @GetMapping("/count")
  @Operation(summary = "Get total number of users")
  public ResponseEntity<ApiResponseDto<Long>> getTotalUsers() {
    long total = userService.getTotalUsers();
    return ApiResponseUtil.success("Total number of users retrieved successfully", total);
  }

  @GetMapping("/active/count")
  @Operation(summary = "Get number of active users")
  public ResponseEntity<ApiResponseDto<Long>> getActiveUsers() {
    long active = userService.getActiveUsersCount();
     return ApiResponseUtil.success("Total number of active users retrieved successfully", active);
  }

  @GetMapping("/{userId}")
  @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
  @Operation(description = "Get one user by ID (admin/manager only)")
  public ResponseEntity<ApiResponseDto<UserResponse>> getUserById(@PathVariable UUID userId) {
    UserResponse user = userService.getUserById(userId);
    return ApiResponseUtil.success("User retrieved successfully", user);

  }

  @PutMapping("/{userId}/activate")
  @PreAuthorize("hasRole('ADMIN')")
  @Operation(summary = "Activate a user (admin only)")
  public ResponseEntity<ApiResponseDto<String>> activateUser(
      @PathVariable UUID userId) {
    userService.activateUser(userId);
    return ApiResponseUtil.success("User activated successfully", "User has been activated");
  }

  @GetMapping("/profile")
  @Operation(summary = "Get current user's profile information")
  public ResponseEntity<ApiResponseDto<UserProfile>> getCurrentUserProfile(
      @AuthenticationPrincipal UserPrincipal userDetails) {
    UserProfile profile = userService.getCurrentUserProfile(userDetails.getId());
    return ApiResponseUtil.success("User profile retrieved successfully", profile);
  }

  @PutMapping("/profile")
  @Operation(summary = "Update current user's profile information")
  public ResponseEntity<ApiResponseDto<UserProfile>> updateProfile(
      @Valid @RequestBody UpdateProfileRequest request,
      @AuthenticationPrincipal UserPrincipal userDetails) {
    UserProfile profile = userService.updateProfile(
        userDetails.getId(), request);
    return ApiResponseUtil.success("User profile updated successfully", profile);
  }

  @PutMapping("/{userId}")
  @Operation(summary = "Update an existing user (admin/manager only)")
  @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
  public ResponseEntity<ApiResponseDto<UserResponse>> updateUser(
      @PathVariable UUID userId,
      @Valid @RequestBody UpdateUserRequest request) {
    UserResponse userInfo = userService.updateUser(userId, request);
    return ApiResponseUtil.success("User updated successfully", userInfo);
  }

  @PostMapping(path = "/profile/avatar", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  @Operation(summary = "Upload avatar for current user")
  public ResponseEntity<ApiResponseDto<AvatarUploadResponse>> uploadAvatar(
      @RequestParam("file") MultipartFile file,
      @AuthenticationPrincipal UserPrincipal userDetails) {

    AvatarUploadResponse response = userService.uploadAvatar(
        userDetails.getId(), file);
    return ApiResponseUtil.success("User profile updated successfully", response);
  }

}
