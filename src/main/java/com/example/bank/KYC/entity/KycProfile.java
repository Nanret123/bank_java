package com.example.bank.KYC.entity;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.example.bank.KYC.enums.KycStatus;
import com.example.bank.customer.entity.Customer;
import com.example.bank.customer.enums.RiskLevel;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "kyc_profiles")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class KycProfile {
  @Id
  @GeneratedValue
  private UUID id;

  // Foreign key to Customer
  @OneToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "customer_id", nullable = false, unique = true)
  private Customer customer;

  // Identification Information
  @Column(nullable = false, unique = true)
  private String bvn;

  @Column(name = "risk_level")
  @Enumerated(EnumType.STRING)
  private RiskLevel riskLevel;

  // KYC Information
  @Column(name = "kyc_status", length = 20)
  @Enumerated(EnumType.STRING)
  @Builder.Default
  private KycStatus kycStatus = KycStatus.PENDING;

  @Column(name = "rejection_reason", columnDefinition = "TEXT")
  private String rejectionReason;

  @OneToMany(mappedBy = "kycProfile", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  private List<KycDocument> documents;

  // Audit Fields
  @CreationTimestamp
  @Column(name = "created_at", nullable = false, updatable = false)
  private LocalDateTime createdAt;

  @UpdateTimestamp
  @Column(name = "updated_at")
  private LocalDateTime updatedAt;

  @Column(name = "reviewed_by")
  private UUID reviewedBy;

  @Column(name = "reviewed_at")
  private LocalDateTime reviewedAt;
}