package com.example.bank.security.controller;

import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.bank.common.dto.ApiResponseDto;
import com.example.bank.common.dto.ErrorResponse;
import com.example.bank.common.util.ApiResponseUtil;
import com.example.bank.security.DTO.LoginRequest;
import com.example.bank.security.DTO.LoginResponse;
import com.example.bank.security.DTO.RefreshTokenRequest;
import com.example.bank.security.DTO.TokenResponse;
import com.example.bank.security.service.AuthService;
import com.example.bank.security.service.UserPrincipal;
import com.example.bank.user.DTO.ForcePasswordResetRequest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Authentication", description = "User authentication and token management endpoints")
public class AuthController {

  private final AuthService authService;

  @Operation(summary = "User login", description = "Authenticate user and return JWT access token with refresh token", responses = {
      @ApiResponse(responseCode = "200", description = "Login successful", content = @Content(mediaType = "application/json", schema = @Schema(implementation = LoginResponse.class))),
      @ApiResponse(responseCode = "401", description = "Invalid credentials", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
      @ApiResponse(responseCode = "403", description = "Account disabled", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
  })
  @PostMapping("/login")
  public ResponseEntity<LoginResponse> login(
      @Valid @RequestBody  LoginRequest request,  HttpServletRequest httpRequest) {

    String ipAddress = getClientIP(httpRequest);
    LoginResponse response = authService.login(request, ipAddress);

    log.info("User {} logged in from IP: {}", request.getUsername(), ipAddress);
    return ResponseEntity.ok(response);
  }

  @Operation(summary = "Refresh access token", description = "Generate new access token using valid refresh token", responses = {
      @ApiResponse(responseCode = "200", description = "Token refreshed successfully"),
      @ApiResponse(responseCode = "401", description = "Invalid or expired refresh token")
  })
  @PostMapping("/refresh")
  public ResponseEntity<TokenResponse> refreshToken(
      @Valid @RequestBody RefreshTokenRequest request) {

    TokenResponse response = authService.refreshAccessToken(request.getRefreshToken());
    return ResponseEntity.ok(response);
  }

  @Operation(summary = "User logout", description = "Invalidate refresh token and logout user from current device", security = @SecurityRequirement(name = "Bearer Authentication"))
  @PostMapping("/logout")
  public ResponseEntity<ApiResponseDto<Void>> logout(@RequestBody RefreshTokenRequest request) {
    authService.logout(request.getRefreshToken());
    ApiResponseDto<Void> response = ApiResponseDto.<Void>builder()
        .success(true)
        .message("Logged out successfully")
        .build();
    return ResponseEntity.ok(response);
  }

  @PutMapping("/reset-password")
  @Operation(summary = "Force password reset", description = "Force a user to reset their password", security = @SecurityRequirement(name = "Bearer Authentication"))
  public ResponseEntity<ApiResponseDto<String>> forcePasswordReset(
      @Valid @RequestBody ForcePasswordResetRequest request,
      @AuthenticationPrincipal UserPrincipal userDetails) {

    UUID userId = userDetails.getId();
    String result = authService.forcePasswordReset(userId, request);
    return ApiResponseUtil.success("Password successfully", result);
  }

  @Operation(summary = "Logout all devices", description = "Invalidate all refresh tokens for user across all devices", security = @SecurityRequirement(name = "Bearer Authentication"))
  @PostMapping("/logout-all")
  @PreAuthorize("hasAuthority('ADMIN') or authentication.name == #username")
  public ResponseEntity<ApiResponseDto<Void>> logoutAllDevices(
      @Parameter(description = "Username to logout from all devices") @RequestParam String username) {
    authService.logoutAllDevices(username);
    ApiResponseDto<Void> response = ApiResponseDto.<Void>builder()
        .success(true)
        .message("Logged out from all devices successfully")
        .build();
    return ResponseEntity.ok(response);
  }

  private String getClientIP(HttpServletRequest request) {
    String xfHeader = request.getHeader("X-Forwarded-For");
    if (xfHeader == null) {
      return request.getRemoteAddr();
    }
    return xfHeader.split(",")[0];
  }
}