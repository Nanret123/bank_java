package com.example.bank.KYC.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.bank.KYC.entity.KycProfile;
import com.example.bank.KYC.enums.KycStatus;

@Repository
public interface KycRepository extends JpaRepository<KycProfile, UUID> {
  // Find KYC by customer ID
  @Query("SELECT k FROM Kyc k WHERE k.customer.id = :customerId AND k.isDeleted = false")
  Optional<KycProfile> findByCustomerId(@Param("customerId") UUID customerId);

  boolean existsByCustomerId(UUID customerId);

  Page<KycProfile> findByStatus(KycStatus status, Pageable pageable);

  @Query("SELECT k FROM KycProfile k WHERE k.status = :status AND k.createdAt >= CURRENT_DATE")
  Page<KycProfile> findTodaySubmissionsByStatus(@Param("status") KycStatus status, Pageable pageable);

  @Query("SELECT COUNT(k) FROM KycProfile k WHERE k.status = :status")
  long countByStatus(@Param("status") KycStatus status);
}
