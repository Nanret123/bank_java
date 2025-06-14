package com.example.bank.security.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.bank.security.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> { 
  
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
    List<User> findByBranchCodeAndIsActive(String branchCode, Boolean isActive);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);
  
}
