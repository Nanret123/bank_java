package com.example.bank.KYC.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.example.bank.FileStorage.Service.FileStorageService;
import com.example.bank.FileStorage.dto.FileUploadResponse;
import com.example.bank.KYC.dto.KycApprovalDto;
import com.example.bank.KYC.dto.KycProfileResponseDto;
import com.example.bank.KYC.dto.KycRejectionDto;
import com.example.bank.KYC.dto.KycSubmissionDto;
import com.example.bank.KYC.dto.KycUpdateDto;
import com.example.bank.KYC.entity.KycDocument;
import com.example.bank.KYC.entity.KycProfile;
import com.example.bank.KYC.enums.DocumentType;
import com.example.bank.KYC.enums.KycStatus;
import com.example.bank.KYC.exceptions.InvalidKycStatusException;
import com.example.bank.KYC.exceptions.KycAlreadyExistsException;
import com.example.bank.KYC.exceptions.KycNotFoundException;
import com.example.bank.KYC.interfaces.IKycService;
import com.example.bank.KYC.mapper.KycMapper;
import com.example.bank.KYC.repository.KycDocumentRepository;
import com.example.bank.KYC.repository.KycRepository;
import com.example.bank.customer.entity.Customer;
import com.example.bank.customer.repository.CustomerRepository;
import com.example.bank.FileStorage.exceptions.FileUploadException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class KycService implements IKycService {

  private final KycRepository kycRepository;
  private final KycDocumentRepository documentRepository;
  private final CustomerRepository customerRepository;
  private final FileStorageService fileService;
  private final KycMapper kycMapper;

  @Override
  public KycProfileResponseDto submitKyc(KycSubmissionDto submissionDto) {
    if (kycRepository.existsByCustomerId(submissionDto.getCustomerId())) {
      throw new KycAlreadyExistsException("KYC profile already exists for customer");
    }

    Customer customer = customerRepository.findById(submissionDto.getCustomerId())
        .orElseThrow(() -> new KycNotFoundException("Customer not found"));

    KycProfile kycProfile = kycMapper.toEntity(submissionDto);
    kycProfile.setCustomer(customer);
    kycProfile.setDocuments(new ArrayList<>());

    KycProfile savedProfile = kycRepository.save(kycProfile);

    // Handle document uploads
    if (submissionDto.getDocuments() != null && !submissionDto.getDocuments().isEmpty()) {
      List<KycDocument> documents = uploadDocuments(savedProfile, submissionDto.getDocuments());
      savedProfile.setDocuments(documents);
    }

    log.info("KYC submitted successfully for customer: {}", submissionDto.getCustomerId());
    return kycMapper.toResponseDto(savedProfile);
  }

  private List<KycDocument> uploadDocuments(KycProfile kycProfile, Map<DocumentType, MultipartFile> documents) {
    return documents.entrySet().stream()
        .map(entry -> {
          DocumentType docType = entry.getKey();
          MultipartFile file = entry.getValue();

          try {
            FileUploadResponse uploadResponse = fileService.uploadFile(file, "kyc/" + kycProfile.getCustomer().getId());

            return KycDocument.builder()
                .fileName(uploadResponse.getOriginalFileName())
                .cloudinaryUrl(uploadResponse.getUrl())
                .cloudinaryPublicId(uploadResponse.getPublicId())
                .fileType(uploadResponse.getContentType())
                .fileSize(uploadResponse.getSize())
                .documentType(docType)
                .kycProfile(kycProfile)
                .build();

          } catch (Exception e) {
            log.error("Failed to upload document for customer: {}, document type: {}",
                kycProfile.getCustomer().getId(), docType, e);
            throw new FileUploadException("Failed to upload document: " + file.getOriginalFilename(), e);
          }
        })
        .collect(Collectors.toList());
  }

  @Override
  public KycProfileResponseDto updateKyc(UUID customerId, KycUpdateDto updateDto) {

    KycProfile kycProfile = getKycProfileEntity(customerId);

    // Update profile fields using MapStruct
    kycMapper.updateEntityFromDto(updateDto, kycProfile);
    kycProfile.setKycStatus(KycStatus.RESUBMITTED);
    kycProfile.setRejectionReason(null);
    kycProfile.setReviewedBy(null);
    kycProfile.setReviewedAt(null);

    KycProfile savedProfile = kycRepository.save(kycProfile);

    // Handle new document uploads
    if (updateDto.getDocuments() != null && !updateDto.getDocuments().isEmpty()) {
      List<KycDocument> newDocuments = uploadDocuments(savedProfile, updateDto.getDocuments());
      savedProfile.getDocuments().addAll(newDocuments);
    }

    log.info("KYC updated successfully for customer: {}", customerId);
    return kycMapper.toResponseDto(savedProfile);
  }

  @Override
  @Transactional(readOnly = true)
  public KycProfileResponseDto getKycProfile(UUID customerId) {

    KycProfile kycProfile = getKycProfileEntity(customerId);

    return kycMapper.toResponseDto(kycProfile);
  }

  @Override
  public KycProfileResponseDto approveKyc(UUID customerId, KycApprovalDto approvalDto) {

    KycProfile kycProfile = getKycProfileEntity(customerId);

    if (kycProfile.getKycStatus() == KycStatus.APPROVED) {
      throw new InvalidKycStatusException("KYC is already approved for customer");
    }

    kycProfile.setKycStatus(KycStatus.APPROVED);
    kycProfile.setReviewedBy(approvalDto.getReviewerId());
    kycProfile.setReviewedAt(LocalDateTime.now());
    kycProfile.setRejectionReason(null);

    KycProfile savedProfile = kycRepository.save(kycProfile);

    log.info("KYC approved successfully for customer: {}", customerId);
    return kycMapper.toResponseDto(savedProfile);
  }

  @Override
  public KycProfileResponseDto rejectKyc(UUID customerId, KycRejectionDto rejectionDto) {

    KycProfile kycProfile = getKycProfileEntity(customerId);

    if (kycProfile.getKycStatus() == KycStatus.APPROVED) {
      throw new InvalidKycStatusException("Cannot reject an already approved KYC for customer");
    }

    kycProfile.setKycStatus(KycStatus.REJECTED);
    kycProfile.setRejectionReason(rejectionDto.getRejectionReason());
    kycProfile.setReviewedBy(rejectionDto.getReviewerId());
    kycProfile.setReviewedAt(LocalDateTime.now());

    KycProfile savedProfile = kycRepository.save(kycProfile);

    log.info("KYC rejected successfully for customer: {}", customerId);
    return kycMapper.toResponseDto(savedProfile);
  }

  @Override
  public void deleteKycFiles(UUID customerId, List<UUID> documentIds) {

    List<KycDocument> documentsToDelete = documentRepository.findAllById(documentIds);

    // Delete files from Cloudinary
    documentsToDelete.forEach(doc -> {
      try {
        fileService.deleteFile(doc.getCloudinaryPublicId());
      } catch (Exception e) {
        log.error("Failed to delete file from Cloudinary: {}", doc.getCloudinaryPublicId(), e);
      }
    });

    // Delete from database
    documentRepository.deleteByIdIn(documentIds);

    log.info("Deleted {} KYC files for customer: {}", documentIds.size(), customerId);
  }

  @Override
  @Transactional(readOnly = true)
  public Page<KycProfileResponseDto> getKycProfilesByStatus(KycStatus status, Pageable pageable) {
    Page<KycProfile> profiles = kycRepository.findByStatus(status, pageable);
    return profiles.map(kycMapper::toResponseDto);
  }

  @Override
  @Transactional(readOnly = true)
  public long getKycCountByStatus(KycStatus status) {
    return kycRepository.countByStatus(status);
  }

  private KycProfile getKycProfileEntity(UUID customerId) {
    return kycRepository.findByCustomerId(customerId)
        .orElseThrow(() -> new KycNotFoundException("KYC profile not found for customer"));
  }

}