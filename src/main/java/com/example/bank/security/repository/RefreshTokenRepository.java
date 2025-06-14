package com.example.bank.security.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.bank.security.entity.RefreshToken;
import com.example.bank.security.entity.User;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, UUID> {
  Optional<RefreshToken> findByTokenAndIsActive(String token, Boolean isActive);

  List<RefreshToken> findByUserAndIsActive(User user, Boolean isActive);

  void deleteByUserIdAndIsActive(User user, Boolean isActive);

  void deleteByToken(String token);

  @Modifying
  @Query("UPDATE RefreshToken rt SET rt.isActive = false WHERE rt.expiresAt < :now")
  void deactivateExpiredTokens(@Param("now") LocalDateTime now);
  
}
