package com.example.bank.KYC.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.bank.KYC.entity.KycProfile;

@Repository
public interface KycRepository extends JpaRepository<KycProfile, UUID> {
  // Find KYC by customer ID
  @Query("SELECT k FROM Kyc k WHERE k.customer.id = :customerId AND k.isDeleted = false")
  Optional<KycProfile> findByCustomerId(@Param("customerId") UUID customerId);
}


  // // Find KYC by customer ID including deleted records
  // @Query("SELECT k FROM Kyc k WHERE k.customer.id = :customerId")
  // Optional<Kyc> findByCustomerIdIncludingDeleted(@Param("customerId") UUID customerId);

  // // Find by BVN
  // @Query("SELECT k FROM Kyc k WHERE k.bvn = :bvn AND k.isDeleted = false")
  // Optional<Kyc> findByBvn(@Param("bvn") String bvn);

  // // Find by KYC Status
  // @Query("SELECT k FROM Kyc k WHERE k.kycStatus = :status AND k.isDeleted = false")
  // List<Kyc> findByKycStatus(@Param("status") KycStatus status);

  // // Find by KYC Status with pagination
  // @Query("SELECT k FROM Kyc k WHERE k.kycStatus = :status AND k.isDeleted = false")
  // Page<Kyc> findByKycStatus(@Param("status") KycStatus status, Pageable pageable);

  // // Find by Risk Level
  // @Query("SELECT k FROM Kyc k WHERE k.riskLevel = :riskLevel AND k.isDeleted = false")
  // List<Kyc> findByRiskLevel(@Param("riskLevel") RiskLevel riskLevel);

  // // Find pending KYCs
  // @Query("SELECT k FROM Kyc k WHERE k.kycStatus = 'PENDING' AND k.isDeleted = false ORDER BY k.createdAt ASC")
  // List<Kyc> findPendingKycs();

  // // Find pending KYCs with pagination
  // @Query("SELECT k FROM Kyc k WHERE k.kycStatus = 'PENDING' AND k.isDeleted = false ORDER BY k.createdAt ASC")
  // Page<Kyc> findPendingKycs(Pageable pageable);

  // // Find KYCs with expired IDs
  // @Query("SELECT k FROM Kyc k WHERE k.idExpiryDate < CURRENT_DATE AND k.kycStatus = 'APPROVED' AND k.isDeleted = false")
  // List<Kyc> findKycsWithExpiredIds();

  // // Find by ID type and number
  // @Query("SELECT k FROM Kyc k WHERE k.idType = :idType AND k.idNumber = :idNumber AND k.isDeleted = false")
  // Optional<Kyc> findByIdTypeAndIdNumber(@Param("idType") IdType idType, @Param("idNumber") String idNumber);

  // // Find by customer email (joining with Customer entity)
  // @Query("SELECT k FROM Kyc k JOIN k.customer c WHERE c.email = :email AND k.isDeleted = false")
  // Optional<Kyc> findByCustomerEmail(@Param("email") String email);

  // // Find by customer phone (joining with Customer entity)
  // @Query("SELECT k FROM Kyc k JOIN k.customer c WHERE c.phoneNumber = :phoneNumber AND k.isDeleted = false")
  // Optional<Kyc> findByCustomerPhone(@Param("phoneNumber") String phoneNumber);

  // // Find KYCs created between dates
  // @Query("SELECT k FROM Kyc k WHERE k.createdAt BETWEEN :startDate AND :endDate AND k.isDeleted = false")
  // List<Kyc> findByCreatedAtBetween(@Param("startDate") LocalDateTime startDate,
  //     @Param("endDate") LocalDateTime endDate);

  // // Find KYCs by verification status
  // @Query("SELECT k FROM Kyc k WHERE k.isBvnVerified = :bvnVerified AND k.isIdVerified = :idVerified AND k.isDeleted = false")
  // List<Kyc> findByVerificationStatus(@Param("bvnVerified") Boolean bvnVerified,
  //     @Param("idVerified") Boolean idVerified);

  // // Find unverified KYCs
  // @Query("SELECT k FROM Kyc k WHERE (k.isBvnVerified = false OR k.isIdVerified = false) AND k.kycStatus = 'PENDING' AND k.isDeleted = false")
  // List<Kyc> findUnverifiedKycs();

  // // Count KYCs by status
  // @Query("SELECT COUNT(k) FROM Kyc k WHERE k.kycStatus = :status AND k.isDeleted = false")
  // Long countByKycStatus(@Param("status") KycStatus status);

  // // Count total active KYCs
  // @Query("SELECT COUNT(k) FROM Kyc k WHERE k.isDeleted = false")
  // Long countActiveKycs();

  // // Get KYC status summary
  // @Query("SELECT k.kycStatus as status, COUNT(k) as count FROM Kyc k WHERE k.isDeleted = false GROUP BY k.kycStatus")
  // List<KycStatusSummary> getKycStatusSummary();

  // // Get risk level summary
  // @Query("SELECT k.riskLevel as riskLevel, COUNT(k) as count FROM Kyc k WHERE k.riskLevel IS NOT NULL AND k.isDeleted = false GROUP BY k.riskLevel")
  // List<RiskLevelSummary> getRiskLevelSummary();

  // // Find KYCs approved by specific user
  // @Query("SELECT k FROM Kyc k WHERE k.approvedBy = :approvedBy AND k.kycStatus = 'APPROVED' AND k.isDeleted = false")
  // List<Kyc> findByApprovedBy(@Param("approvedBy") UUID approvedBy);

  // // Search KYCs by customer name (using LIKE)
  // @Query("SELECT k FROM Kyc k JOIN k.customer c WHERE " +
  //     "(LOWER(c.firstName) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
  //     "LOWER(c.lastName) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
  //     "LOWER(CONCAT(c.firstName, ' ', c.lastName)) LIKE LOWER(CONCAT('%', :searchTerm, '%'))) " +
  //     "AND k.isDeleted = false")
  // List<Kyc> searchByCustomerName(@Param("searchTerm") String searchTerm);

  // // Search KYCs by customer name with pagination
  // @Query("SELECT k FROM Kyc k JOIN k.customer c WHERE " +
  //     "(LOWER(c.firstName) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
  //     "LOWER(c.lastName) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
  //     "LOWER(CONCAT(c.firstName, ' ', c.lastName)) LIKE LOWER(CONCAT('%', :searchTerm, '%'))) " +
  //     "AND k.isDeleted = false")
  // Page<Kyc> searchByCustomerName(@Param("searchTerm") String searchTerm, Pageable pageable);

  // // Complex search with multiple criteria
  // @Query("SELECT k FROM Kyc k JOIN k.customer c WHERE " +
  //     "(:status IS NULL OR k.kycStatus = :status) AND " +
  //     "(:riskLevel IS NULL OR k.riskLevel = :riskLevel) AND " +
  //     "(:bvnVerified IS NULL OR k.isBvnVerified = :bvnVerified) AND " +
  //     "(:idVerified IS NULL OR k.isIdVerified = :idVerified) AND " +
  //     "(:searchTerm IS NULL OR LOWER(c.firstName) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
  //     "LOWER(c.lastName) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
  //     "LOWER(c.email) LIKE LOWER(CONCAT('%', :searchTerm, '%'))) AND " +
  //     "k.isDeleted = false")
  // Page<Kyc> findByCriteria(@Param("status") KycStatus status,
  //     @Param("riskLevel") RiskLevel riskLevel,
  //     @Param("bvnVerified") Boolean bvnVerified,
  //     @Param("idVerified") Boolean idVerified,
  //     @Param("searchTerm") String searchTerm,
  //     Pageable pageable);

  // // Soft delete KYC
  // @Modifying
  // @Query("UPDATE Kyc k SET k.isDeleted = true, k.deletedAt = CURRENT_TIMESTAMP, k.deletedBy = :deletedBy WHERE k.id = :id")
  // int softDeleteById(@Param("id") UUID id, @Param("deletedBy") UUID deletedBy);

  // // Restore soft deleted KYC
  // @Modifying
  // @Query("UPDATE Kyc k SET k.isDeleted = false, k.deletedAt = null, k.deletedBy = null WHERE k.id = :id")
  // int restoreById(@Param("id") UUID id);

  // // Bulk update KYC status
  // @Modifying
  // @Query("UPDATE Kyc k SET k.kycStatus = :newStatus, k.verificationReason = :reason, k.updatedBy = :updatedBy WHERE k.id IN :ids")
  // int bulkUpdateStatus(@Param("ids") List<UUID> ids,
  //     @Param("newStatus") KycStatus newStatus,
  //     @Param("reason") String reason,
  //     @Param("updatedBy") UUID updatedBy);

  // // Check if BVN exists (excluding current KYC)
  // @Query("SELECT COUNT(k) > 0 FROM Kyc k WHERE k.bvn = :bvn AND k.id != :excludeId AND k.isDeleted = false")
  // boolean existsByBvnExcludingId(@Param("bvn") String bvn, @Param("excludeId") UUID excludeId);

  // // Check if ID number exists (excluding current KYC)
  // @Query("SELECT COUNT(k) > 0 FROM Kyc k WHERE k.idType = :idType AND k.idNumber = :idNumber AND k.id != :excludeId AND k.isDeleted = false")
  // boolean existsByIdTypeAndIdNumberExcludingId(@Param("idType") IdType idType,
  //     @Param("idNumber") String idNumber,
  //     @Param("excludeId") UUID excludeId);
