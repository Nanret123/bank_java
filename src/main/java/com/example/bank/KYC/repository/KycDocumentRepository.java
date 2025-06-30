package com.example.bank.KYC.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.bank.KYC.entity.KycDocument;
import com.example.bank.KYC.enums.DocumentType;

@Repository
public interface KycDocumentRepository extends JpaRepository<KycDocument, UUID> {
    
    List<KycDocument> findByKycProfileId(UUID kycProfileId);

    boolean existsByKycProfileIdAndDocumentType(UUID kycProfileId, DocumentType documentType);

    Optional<KycDocument> findByIdAndKycProfileId(UUID id, UUID kycProfileId);
    
    @Modifying
    @Query("DELETE FROM KycDocument d WHERE d.id IN :documentIds")
    void deleteByIdIn(@Param("documentIds") List<UUID> documentIds);
    
    @Modifying
    @Query("DELETE FROM KycDocument d WHERE d.kycProfile.id = :kycProfileId")
    void deleteByKycProfileId(@Param("kycProfileId") UUID kycProfileId);
}
