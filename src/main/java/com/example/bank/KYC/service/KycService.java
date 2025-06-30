package com.example.bank.KYC.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.bank.FileStorage.Service.FileStorageService;
import com.example.bank.FileStorage.dto.FileUploadResponse;
import com.example.bank.KYC.dto.DocumentListResponseDto;
import com.example.bank.KYC.dto.DocumentUploadRequestDto;
import com.example.bank.KYC.dto.KycApprovalDto;
import com.example.bank.KYC.dto.KycDocumentDto;
import com.example.bank.KYC.dto.KycFilter;
import com.example.bank.KYC.dto.KycProfileResponseDto;
import com.example.bank.KYC.dto.KycRejectionDto;
import com.example.bank.KYC.dto.KycSubmissionDto;
import com.example.bank.KYC.dto.KycUpdateDto;
import com.example.bank.KYC.entity.KycDocument;
import com.example.bank.KYC.entity.KycProfile;
import com.example.bank.KYC.enums.KycStatus;
import com.example.bank.KYC.exceptions.InvalidKycStatusException;
import com.example.bank.KYC.exceptions.KycAlreadyExistsException;
import com.example.bank.KYC.exceptions.KycNotFoundException;
import com.example.bank.KYC.interfaces.IKycService;
import com.example.bank.KYC.mapper.KycMapper;
import com.example.bank.KYC.repository.KycDocumentRepository;
import com.example.bank.KYC.repository.KycRepository;
import com.example.bank.common.dto.PaginationRequest;
import com.example.bank.common.exception.ResourceNotFoundException;
import com.example.bank.common.util.BvnGenerator;
import com.example.bank.customer.entity.Customer;
import com.example.bank.customer.repository.CustomerRepository;

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

    String generatedBvn = BvnGenerator.generateBvn();

    KycProfile kycProfile = kycMapper.toEntity(submissionDto);
    kycProfile.setBvn(generatedBvn);
    kycProfile.setCustomer(customer);
    kycProfile.setDocuments(new ArrayList<>());

    KycProfile savedProfile = kycRepository.save(kycProfile);

    // Handle document uploads
    FileUploadResponse fileResponse = fileService.uploadFile(submissionDto.getDocument(), "kyc_documents");

    KycDocument document = KycDocument.builder()
        .documentType(submissionDto.getDocumentType())
        .fileName(fileResponse.getOriginalFileName())
        .fileType(fileResponse.getContentType())
        .fileSize(fileResponse.getSize())
        .cloudinaryUrl(fileResponse.getUrl())
        .cloudinaryPublicId(fileResponse.getPublicId())
        .uploadedAt(fileResponse.getUploadedAt())
        .kycProfile(savedProfile)
        .build();

    documentRepository.save(document);
    savedProfile.getDocuments().add(document);

    log.info("KYC submitted successfully for customer: {}", submissionDto.getCustomerId());
    return kycMapper.toResponseDto(savedProfile);
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
    if (updateDto.getDocument() != null) {
      FileUploadResponse fileResponse = fileService.uploadFile(updateDto.getDocument(), "kyc_documents");

      KycDocument document = KycDocument.builder()
          .documentType(updateDto.getDocumentType())
          .fileName(fileResponse.getOriginalFileName())
          .fileType(fileResponse.getContentType())
          .fileSize(fileResponse.getSize())
          .cloudinaryUrl(fileResponse.getUrl())
          .cloudinaryPublicId(fileResponse.getPublicId())
          .uploadedAt(fileResponse.getUploadedAt())
          .kycProfile(savedProfile)
          .build();

      // Save the document
      documentRepository.save(document);

      // Add to profile’s documents list (ensure it's initialized)
      if (savedProfile.getDocuments() == null) {
        savedProfile.setDocuments(new ArrayList<>());
      }
      savedProfile.getDocuments().add(document);
    }

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
  public Page<KycProfileResponseDto> getKycProfilesByStatus(KycFilter filter) {
    Pageable pageable = createPageable(filter);

    Specification<KycProfile> spec = KycSpecification.withFilters(filter);

    Page<KycProfile> profiles = kycRepository.findAll(spec, pageable);

    return profiles.map(kycMapper::toResponseDto);
  }

  @Override
  @Transactional(readOnly = true)
  public long getKycCountByStatus(KycStatus status) {
    return kycRepository.countByKycStatus(status);
  }

  @Override
  @Transactional(readOnly = true)
  public DocumentListResponseDto getCustomerDocuments(UUID customerId) {

    KycProfile kycProfile = getKycProfileEntity(customerId);
    List<KycDocument> documents = documentRepository.findByKycProfileId(kycProfile.getId());

    List<KycDocumentDto> documentDtos = kycMapper.toDocumentDtos(documents);

    return DocumentListResponseDto.builder()
        .documents(documentDtos)
        .totalCount(documentDtos.size())
        .build();
  }

  @Override
  @Transactional(readOnly = true)
  public KycDocumentDto getCustomerDocument(UUID customerId, UUID docId) {

    KycProfile kycProfile = getKycProfileEntity(customerId);
    KycDocument document = documentRepository.findByIdAndKycProfileId(docId, kycProfile.getId())
        .orElseThrow(() -> new ResourceNotFoundException("Document not found"));

    return kycMapper.toDocumentDto(document);
  }

  @Override
  public KycDocumentDto uploadDocument(UUID customerId, DocumentUploadRequestDto request) {
    fileService.validateFile(request.getDocument());

    KycProfile kycProfile = getKycProfileEntity(customerId);

    // Check if document type already exists for this customer
    boolean documentExists = documentRepository.existsByKycProfileIdAndDocumentType(
        kycProfile.getId(), request.getDocumentType());

    if (documentExists) {
      throw new KycAlreadyExistsException(
          "Document of type " + request.getDocumentType() + " already exists for this customer");
    }

    try {
      FileUploadResponse uploadResult = fileService.uploadFile(request.getDocument(), "kyc_documents");

      KycDocument document = KycDocument.builder()
          .documentType(request.getDocumentType())
          .fileName(uploadResult.getOriginalFileName())
          .fileType(uploadResult.getContentType())
          .fileSize(uploadResult.getSize())
          .cloudinaryUrl(uploadResult.getUrl())
          .cloudinaryPublicId(uploadResult.getPublicId())
          .uploadedAt(uploadResult.getUploadedAt())
          .kycProfile(kycProfile)
          .build();

      // Save the document
      documentRepository.save(document);

      return kycMapper.toDocumentDto(document);

    } catch (Exception e) {
      log.error("Failed to upload document for customer {}: {}", customerId, e.getMessage());
      throw new RuntimeException("Failed to upload document: " + e.getMessage());
    }
  }

  @Override
  public void deleteCustomerDocument(UUID customerId, UUID docId) {
        
        KycProfile kycProfile = getKycProfileEntity(customerId);
        KycDocument document = documentRepository.findByIdAndKycProfileId(docId, kycProfile.getId())
                .orElseThrow(() -> new KycNotFoundException("Document not found"));
        
        try {
            // Delete from cloud storage
            fileService.deleteFile(document.getCloudinaryPublicId());
            
            // Delete from database
            documentRepository.delete(document);
            log.info("Successfully deleted document {} for customer {}", docId, customerId);
            
        } catch (Exception e) {
            log.error("Failed to delete document {} for customer {}: {}", docId, customerId, e.getMessage());
            throw new RuntimeException("Failed to delete document: " + e.getMessage());
        }
    }

  private KycProfile getKycProfileEntity(UUID customerId) {
    return kycRepository.findByCustomer_Id(customerId)
        .orElseThrow(() -> new KycNotFoundException("KYC profile not found for customer"));
  }

  private Pageable createPageable(PaginationRequest filter) {
    Sort sort = createSort(filter.getSortBy(), filter.getSortDirection());
    return PageRequest.of(filter.getPage(), filter.getSize(), sort);
  }

  private Sort createSort(String sortBy, String sortDirection) {
    Sort.Direction direction = "desc".equalsIgnoreCase(sortDirection)
        ? Sort.Direction.DESC
        : Sort.Direction.ASC;

    return Sort.by(direction, sortBy);
  }

}