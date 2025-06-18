package com.example.bank.security.entity;

import java.time.LocalDateTime;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "users")
@Builder
public class User {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private UUID id;

  @Column(unique = true, nullable = false)
  private String username;

  @Column(nullable = false)
  private String password;

  @Column(nullable = false, unique = true)
  private String email;

  @Column(name="full_name")
  private String fullName;

@Enumerated(EnumType.STRING)
  private UserRole role;

  @Column(name="branch_code")
  private String branchCode;

  @Column(name="is_active")
  @Builder.Default
  private boolean isActive = true;

  @Column(name="avatar_url")
  private String avatarUrl;

  @Column(name="phone_number")
  private String phoneNumber;

  private String address;

  @Column(name="is_password_changed")
  private boolean isPasswordChanged;

  private String bio;

  @CreationTimestamp
  @Column(name="created_at")
  private LocalDateTime createdAt;

  @UpdateTimestamp
  @Column(name="updated_at")
  private LocalDateTime updatedAt;

  
  @Column(name = "last_login")
  private LocalDateTime lastLogin;

}
