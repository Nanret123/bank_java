package com.example.bank.security.service;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.example.bank.security.entity.User;
import com.example.bank.security.entity.UserRole;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserPrincipal implements UserDetails {
  private UUID id;
  private String username;
  private String password;
  private String email;
  private String fullName;
  private UserRole role;
  private String branchCode;
  private Boolean isActive;
  private Collection<? extends GrantedAuthority> authorities;

  public static UserPrincipal create(User user) {
    List<GrantedAuthority> authorities = Collections.singletonList(
        new SimpleGrantedAuthority(user.getRole().getCode()));

    return new UserPrincipal(
        user.getId(),
        user.getUsername(),
        user.getPassword(),
        user.getEmail(),
        user.getFullName(),
        user.getRole(),
        user.getBranchCode(),
        user.isActive(),
        authorities);
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return authorities;
  }

  @Override
  public String getPassword() {
    return password;
  }

  @Override
  public String getUsername() {
    return username;
  }

  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    return isActive;
  }
}
