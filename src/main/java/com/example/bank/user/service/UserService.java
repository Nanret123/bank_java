package com.example.bank.user.service;

import java.security.SecureRandom;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.bank.FileStorage.Service.CloudinaryFileStorageService;
import com.example.bank.FileStorage.dto.FileUploadResponse;
import com.example.bank.common.exception.InvalidFileException;
import com.example.bank.common.exception.ResourceNotFoundException;
import com.example.bank.common.exception.ValidationException;
import com.example.bank.security.entity.User;
import com.example.bank.security.repository.UserRepository;
import com.example.bank.user.DTO.AvatarUploadResponse;
import com.example.bank.user.DTO.CreateUserRequest;
import com.example.bank.user.DTO.UpdateProfileRequest;
import com.example.bank.user.DTO.UpdateUserRequest;
import com.example.bank.user.DTO.UserCreationResponse;
import com.example.bank.user.DTO.UserFilter;
import com.example.bank.user.DTO.UserProfile;
import com.example.bank.user.DTO.UserResponse;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

  private static final String TEMP_PASSWORD_CHARS = "ABCDEFGHJKLMNPQRSTUVWXYZabcdefghijkmnpqrstuvwxyz23456789";
  private static final int TEMP_PASSWORD_LENGTH = 12;
  private static final List<String> ALLOWED_IMAGE_TYPES = Arrays.asList(
      "image/jpeg", "image/jpg", "image/png", "image/gif");
  private static final long MAX_FILE_SIZE = 10 * 1024 * 1024; // 10MB

  private final UserRepository userRepo;
  private final PasswordEncoder passwordEncoder;
  private final CloudinaryFileStorageService fileStorageService;

  public UserCreationResponse createUser(CreateUserRequest request) {
    // validate unique username and email
    if (userRepo.existsByUsername(request.getUsername())) {
      throw new ValidationException("Username already exists");
    }
    if (userRepo.existsByEmail(request.getEmail())) {
      throw new ValidationException("Email already exists");
    }

    // Generate temporary password
    String temporaryPassword = generateSecureTemporaryPassword();

    // create user
    User user = User.builder()
        .username(request.getUsername())
        .password(passwordEncoder.encode(temporaryPassword)) // Store encoded temporary password
        .email(request.getEmail())
        .fullName(request.getFullName())
        .role(request.getRole())
        .branchCode(request.getBranchCode())
        .isActive(true)
        .build();

    User savedUser = userRepo.save(user);

    return UserCreationResponse.builder()
        .id(savedUser.getId())
        .fullname(savedUser.getFullName())
        .email(savedUser.getEmail())
        .username(savedUser.getUsername())
        .branchCode(savedUser.getBranchCode())
        .temporaryPassword(temporaryPassword)
        .requiresPasswordChange(true)
        .build();

  }

  public UserResponse updateUser(UUID userId, UpdateUserRequest request) {
    User user = findUserEntity(userId);

    // Update fields
    if (request.getFullName() != null) {
      user.setFullName(request.getFullName());
    }
    if (request.getEmail() != null) {
      if (userRepo.existsByEmail(request.getEmail()) &&
          !user.getEmail().equals(request.getEmail())) {
        throw new ValidationException("Email already exists");
      }
      user.setEmail(request.getEmail());
    }
    if (request.getRole() != null) {
      user.setRole(request.getRole());
    }
    if (request.getBranchCode() != null) {
      user.setBranchCode(request.getBranchCode());
    }
    if (request.getIsActive() != null) {
      user.setActive(request.getIsActive());
    }

    User updatedUser = userRepo.save(user);

    return mapToUserResponse(updatedUser);
  }

  public Page<UserResponse> getAllUsers(UserFilter filter) {
    Specification<User> spec = buildSpecification(filter);

    Pageable pageable = createPageable(filter);

    Page<User> users = userRepo.findAll(spec, pageable);
    return users.map(this::mapToUserResponse);
  }

  public UserResponse getUserById(UUID userId) {
    User user = userRepo.findById(userId)
        .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    return mapToUserResponse(user);
  }

  public void deactivateUser(UUID userId) {
    User user = findUserEntity(userId);
    user.setActive(false);
    userRepo.save(user);
  }

  public void activateUser(UUID userId) {
    userRepo.updateUserStatus(userId, false);
  }

  public long getTotalUsers() {
    return userRepo.count();
  }

  public long getActiveUsersCount() {
    return userRepo.findByIsActive(true).size();
  }

  public UserProfile getCurrentUserProfile(UUID userId) {
    User user = findUserEntity(userId);
    return UserProfile.builder()
        .userId(user.getId().toString())
        .username(user.getUsername())
        .email(user.getEmail())
        .fullName(user.getFullName())
        .bio(user.getBio())
        .build();
  }

  public UserProfile updateProfile(UUID userId, UpdateProfileRequest profile) {
    User user = findUserEntity(userId);

    // Update fields
    if (profile.getFullName() != null) {
      user.setFullName(profile.getFullName());
    }
    if (profile.getBio() != null) {
      user.setBio(profile.getBio());
    }

    User updatedUser = userRepo.save(user);

    return UserProfile.builder()
        .userId(updatedUser.getId().toString())
        .username(updatedUser.getUsername())
        .email(updatedUser.getEmail())
        .fullName(updatedUser.getFullName())
        .bio(updatedUser.getBio())
        .build();
  }

  public AvatarUploadResponse uploadAvatar(UUID userId, MultipartFile file) {
    User user = findUserEntity(userId);

    try {
      // Delete old avatar if exists
      if (user.getAvatarUrl() != null) {
        fileStorageService.deleteFile(user.getAvatarUrl());
      }

      // Upload new avatar
      FileUploadResponse avatarUrl = fileStorageService.uploadFile(file, "avatars/" + user.getUsername());
      user.setAvatarUrl(avatarUrl.getUrl());
      userRepo.save(user);

      log.info("Avatar uploaded successfully for user: {}", user.getUsername());

      

      return AvatarUploadResponse.builder()
          .avatarUrl(avatarUrl.getUrl())
          .message("Avatar uploaded successfully")
          .success(true)
          .build();

    } catch (Exception e) {
      log.error("Error uploading avatar", e);
      return AvatarUploadResponse.builder()
          .message("Failed to upload avatar: " + e.getMessage())
          .success(false)
          .build();
    }
  }

   public void removeProfilePicture (UUID userId) {
    User user = findUserEntity(userId);

    if (user.getAvatarUrl() != null) {
      try {
        fileStorageService.deleteFile(user.getAvatarUrl());
        user.setAvatarUrl(null);
        userRepo.save(user);
        log.info("Profile picture removed successfully for user: {}", user.getUsername());
      } catch (Exception e) {
        log.error("Error removing profile picture", e);
        throw new RuntimeException("Failed to remove profile picture: " + e.getMessage());
      }
    } else {
      throw new ResourceNotFoundException("No profile picture found for user");
    }
   }

  private UserResponse mapToUserResponse(User user) {
    return UserResponse.builder()
        .id(user.getId())
        .username(user.getUsername())
        .email(user.getEmail())
        .fullName(user.getFullName())
        .role(user.getRole())
        .branchCode(user.getBranchCode())
        .isActive(user.isActive())
        .createdAt(user.getCreatedAt())
        .updatedAt(user.getUpdatedAt())
        .lastLogin(user.getLastLogin())
        .build();
  }

  private Specification<User> buildSpecification(UserFilter filter) {
    Specification<User> spec = UserSpecification.hasRole(filter.getRole())
        .and(UserSpecification.hasBranchCode(filter.getBranchCode()))
        .and(UserSpecification.isActive(filter.getActive()))
        .and(UserSpecification.nameContains(filter.getSearchTerm()));
    return spec;
  }

  private Pageable createPageable(UserFilter filterDto) {
    Sort.Direction direction = "desc".equalsIgnoreCase(filterDto.getSortDirection())
        ? Sort.Direction.DESC
        : Sort.Direction.ASC;

    Sort sort = Sort.by(direction, filterDto.getSortBy());

    return PageRequest.of(filterDto.getPage(), filterDto.getSize(), sort);
  }

  private User findUserEntity(UUID userId) {
    return userRepo.findById(userId)
        .orElseThrow(() -> new ResourceNotFoundException("User not found"));
  }

  private String generateSecureTemporaryPassword() {
    SecureRandom random = new SecureRandom();
    StringBuilder password = new StringBuilder();

    for (int i = 0; i < TEMP_PASSWORD_LENGTH; i++) {
      password.append(TEMP_PASSWORD_CHARS.charAt(random.nextInt(TEMP_PASSWORD_CHARS.length())));
    }

    return password.toString();
  }
}