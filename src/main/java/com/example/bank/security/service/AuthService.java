package com.example.bank.security.service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.bank.security.DTO.LoginRequest;
import com.example.bank.security.DTO.LoginResponse;
import com.example.bank.security.DTO.TokenResponse;
import com.example.bank.security.DTO.UserInfo;
import com.example.bank.security.JWT.service.JwtService;
import com.example.bank.security.entity.RefreshToken;
import com.example.bank.security.entity.User;
import com.example.bank.security.repository.RefreshTokenRepository;
import com.example.bank.security.repository.UserRepository;
import com.example.bank.user.DTO.ForcePasswordResetRequest;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class AuthService {
  private final UserRepository userRepo;
  private final RefreshTokenRepository refreshTokenRepo;
  private final PasswordEncoder passwordEncoder;
  private final JwtService jwtService;

  @Value("${app.jwt.refresh-token-expiration}")
  private Long refreshTokenExpiration;

  @Value("${app.jwt.access-token-expiration}")
  private Long accessTokenExpiration;

  public LoginResponse login(LoginRequest request, String ipAddress) {
    User user = userRepo.findByUsername(request.getUsername())
        .orElseThrow(() -> new BadCredentialsException("Invalid username or password"));

    if (!user.isActive()) {
      throw new DisabledException("Account is inactive");
    }

    if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
      throw new BadCredentialsException("Invalid credentials");
    }

    // generate tokens
    String accessToken = jwtService.generateAccessToken(user);
    String refreshTokenString = jwtService.generateRefreshToken();

    // save refresh token
    RefreshToken refreshToken = RefreshToken.builder()
        .token(refreshTokenString)
        .user(user)
        .ipAddress(ipAddress)
        .expiresAt(LocalDateTime.now().plus(refreshTokenExpiration, ChronoUnit.MILLIS))
        .isActive(true)
        .build();

    // Save the refresh token in the database
    refreshTokenRepo.save(refreshToken);

    // update last login
    user.setLastLogin(LocalDateTime.now());
    userRepo.save(user);

    // build the response
    UserInfo userInfo = UserInfo.builder()
        .id(user.getId())
        .username(user.getUsername())
        .email(user.getEmail())
        .fullName(user.getFullName())
        .isPasswordChanged(user.isPasswordChanged())
        .role(user.getRole())
        .branchCode(user.getBranchCode())
        .lastLogin(user.getLastLogin())
        .build();

    return LoginResponse.builder()
        .accessToken(accessToken)
        .refreshToken(refreshTokenString)
        .expiresIn(accessTokenExpiration / 1000) // Convert milliseconds to seconds
        .user(userInfo)
        .build();
  }

   public String forcePasswordReset(UUID userId, ForcePasswordResetRequest request) {
    User user = userRepo.findById(userId)
        .orElseThrow(() -> new UsernameNotFoundException("User not found"));

    if (!passwordEncoder.matches(request.getOldPassword(), user.getPassword())) {
        throw new BadCredentialsException("Old password is incorrect");
    }
    user.setPassword(passwordEncoder.encode(request.getNewPassword()));
    user.setPasswordChanged(true); // Mark password as changed
    userRepo.save(user);

    return "Password reset successfully";
  }


  public TokenResponse refreshAccessToken(String refreshTokenString) {

    RefreshToken refreshToken = refreshTokenRepo.findByTokenAndIsActive(refreshTokenString, true)
        .orElseThrow(() -> new SecurityException("Invalid refresh token"));

    if (refreshToken.getExpiresAt().isBefore(LocalDateTime.now())) {
      throw new SecurityException("Refresh token has expired");
    }

    String newAccessToken = jwtService.generateAccessToken(refreshToken.getUser());

    return TokenResponse.builder()
        .accessToken(newAccessToken)
        .expiresIn(accessTokenExpiration / 1000) // Convert milliseconds to seconds
        .build();
  }

  public void logout(String refreshTokenString) {

 refreshTokenRepo.findByTokenAndIsActive(refreshTokenString, true)
       .ifPresent(token -> {
        token.setActive(false);
        refreshTokenRepo.save(token);
       });
  }

  public void logoutAllDevices(String username) {
    User user = userRepo.findByUsername(username)
        .orElseThrow(() -> new UsernameNotFoundException("User not found"));

    List<RefreshToken> activeTokens = refreshTokenRepo.findByUserAndIsActive(user, true);
        activeTokens.forEach(token ->  token.setActive(false)
        );
    refreshTokenRepo.saveAll(activeTokens);
  }

@Scheduled(fixedRate = 3600000) // Run every hour
    public void cleanupExpiredTokens() {
        refreshTokenRepo.deactivateExpiredTokens(LocalDateTime.now());
        log.info("Cleaned up expired refresh tokens");
    }
}