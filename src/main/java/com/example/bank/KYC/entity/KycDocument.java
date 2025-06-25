package com.example.bank.KYC.entity;

import java.time.LocalDateTime;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;

import com.example.bank.KYC.enums.DocumentType;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "kyc_documents")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class KycDocument {
  @Id
  @GeneratedValue
  private UUID id;

  @Column(nullable = false)
  private String fileName;

  @Column(nullable = false)
  private String cloudinaryUrl;

  @Column(nullable = false)
  private String cloudinaryPublicId;

  private String fileType;

  private Long fileSize;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private DocumentType documentType;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "kyc_profile_id")
  private KycProfile kycProfile;

  @CreationTimestamp
  private LocalDateTime uploadedAt;
}