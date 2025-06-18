package com.example.bank.user.controller;

import java.util.UUID;

import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
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
import com.example.bank.user.DTO.ForcePasswordResetRequest;
import com.example.bank.user.DTO.UpdateProfileRequest;
import com.example.bank.user.DTO.UpdateUserRequest;
import com.example.bank.user.DTO.UserCreationResponse;
import com.example.bank.user.DTO.UserFilter;
import com.example.bank.user.DTO.UserProfile;
import com.example.bank.user.DTO.UserResponse;
import com.example.bank.user.service.UserService;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
  private final UserService userService;

  @PostMapping("/create")
  //@PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
  @Operation(summary = "Create a new user")
  public ResponseEntity<ApiResponseDto<UserCreationResponse>> createUser(
      @Valid @RequestBody CreateUserRequest request) {
    UserCreationResponse userInfo = userService.createUser(request);

    return ApiResponseUtil.success("User created successfully", userInfo, HttpStatus.CREATED);
  }

  @PutMapping("/{userId}")
  @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
  @Operation(summary = "Update an existing user")
  public ResponseEntity<ApiResponseDto<UserResponse>> updateUser(
      @PathVariable UUID userId,
      @Valid @RequestBody UpdateUserRequest request) {
    UserResponse userInfo = userService.updateUser(userId, request);
    return ApiResponseUtil.success("User updated successfully", userInfo);
  }

  @DeleteMapping("/{userId}")
  @PreAuthorize("hasRole('ADMIN')")
  @Operation(summary = "Deactivate a user")
  public ResponseEntity<ApiResponseDto<Void>> deactivateUser(@PathVariable UUID userId) {
    userService.deactivateUser(userId);
    return ApiResponseUtil.success("User deactivated successfully");
  }

  @GetMapping()
  @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
  @Operation(summary = "Get users including pageable and filter options")
  public ResponseEntity<ApiResponseDto<Page<UserResponse>>> getAllUsers(
      @Valid @ModelAttribute @ParameterObject UserFilter filter) {
    Page<UserResponse> users = userService.getAllUsers(filter);
    return ApiResponseUtil.success("Users retrieved successfully", users);

  }

  @GetMapping("/{userId}")
  @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
  @Operation(description = "Get one user")
  public ResponseEntity<ApiResponseDto<UserResponse>> getUserById(@PathVariable UUID userId) {
    UserResponse user = userService.getUserById(userId);
    return ApiResponseUtil.success("User retrieved successfully", user);

  }

  @PutMapping("/reset-password")
  public ResponseEntity<ApiResponseDto<String>> forcePasswordReset(
      @Valid @RequestBody ForcePasswordResetRequest request,
      @AuthenticationPrincipal UserPrincipal userDetails) {

    UUID userId = userDetails.getId();
    String result = userService.forcePasswordReset(userId, request);
    return ApiResponseUtil.success("Password successfully", result);
  }

  @PutMapping("/{userId}/activate")
  @PreAuthorize("hasRole('ADMIN')")
  @Operation(summary = "Activate a user")
  public ResponseEntity<ApiResponseDto<String>> activateUser(
      @PathVariable UUID userId) {
    userService.activateUser(userId);
    return ApiResponseUtil.success("User activated successfully", "User has been activated");
  }

  @GetMapping("/profile")
  @Operation(summary = "Get current user's profile")
  public ResponseEntity<ApiResponseDto<UserProfile>> getCurrentUserProfile(
      @AuthenticationPrincipal UserPrincipal userDetails) {
    UserProfile profile = userService.getCurrentUserProfile(userDetails.getId());
    return ApiResponseUtil.success("User profile retrieved successfully", profile);
  }

  @PutMapping("/profile")
  public ResponseEntity<ApiResponseDto<UserProfile>> updateProfile(
      @Valid @RequestBody UpdateProfileRequest request,
      @AuthenticationPrincipal UserPrincipal userDetails) {
    UserProfile profile = userService.updateProfile(
        userDetails.getId(), request);
    return ApiResponseUtil.success("User profile updated successfully", profile);
  }

  @PostMapping("/profile/avatar")
  public ResponseEntity<ApiResponseDto<AvatarUploadResponse>> uploadAvatar(
      @RequestParam("file") MultipartFile file,
      @AuthenticationPrincipal UserPrincipal userDetails) {

    AvatarUploadResponse response = userService.uploadAvatar(
        userDetails.getId(), file);
    return ApiResponseUtil.success("User profile updated successfully", response);
  }

}
