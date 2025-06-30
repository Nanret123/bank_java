package com.example.bank.KYC.Controller;

import java.util.List;
import java.util.UUID;

import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.bank.KYC.dto.DocumentListResponseDto;
import com.example.bank.KYC.dto.DocumentUploadRequestDto;
import com.example.bank.KYC.dto.KycApprovalDto;
import com.example.bank.KYC.dto.KycDocumentDto;
import com.example.bank.KYC.dto.KycFilter;
import com.example.bank.KYC.dto.KycProfileResponseDto;
import com.example.bank.KYC.dto.KycRejectionDto;
import com.example.bank.KYC.dto.KycSubmissionDto;
import com.example.bank.KYC.dto.KycUpdateDto;
import com.example.bank.KYC.enums.KycStatus;
import com.example.bank.KYC.interfaces.IKycService;
import com.example.bank.common.dto.ApiResponseDto;
import com.example.bank.common.util.ApiResponseUtil;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/kyc")
@RequiredArgsConstructor
@Tag(name = "KYC Management")
public class KycController {

  private final IKycService kycService;

  @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  @Operation(summary = "Submit KYC for a customer")
  public ResponseEntity<ApiResponseDto<KycProfileResponseDto>> submitKyc(
      @ModelAttribute KycSubmissionDto submissionDto) {
    KycProfileResponseDto response = kycService.submitKyc(submissionDto);
    return ApiResponseUtil.success("KYC submitted successfully", response, HttpStatus.CREATED);
  }

  @PutMapping(value = "/{customerId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  @Operation(summary = "Update KYC for a customer")
  public ResponseEntity<ApiResponseDto<KycProfileResponseDto>> updateKyc(
      @PathVariable UUID customerId,
      @ModelAttribute KycUpdateDto updateDto) {
    KycProfileResponseDto response = kycService.updateKyc(customerId, updateDto);
    return ApiResponseUtil.success("KYC updated successfully", response);
  }

  @GetMapping("/{customerId}")
  @Operation(summary = "Get KYC profile for a customer")
  public ResponseEntity<ApiResponseDto<KycProfileResponseDto>> getKycProfile(
      @PathVariable UUID customerId) {
    KycProfileResponseDto response = kycService.getKycProfile(customerId);
    return ApiResponseUtil.success("KYC profile retrieved successfully", response);
  }

  @PostMapping("/{customerId}/approve")
  @Operation(summary = "Approve KYC for a customer")
  public ResponseEntity<ApiResponseDto<KycProfileResponseDto>> approveKyc(
      @PathVariable UUID customerId,
      @RequestBody @Valid KycApprovalDto approvalDto) {
    KycProfileResponseDto response = kycService.approveKyc(customerId, approvalDto);
    return ApiResponseUtil.success("KYC approved successfully", response);
  }

  @PostMapping("/{customerId}/reject")
  @Operation(summary = "Reject KYC for a customer")
  public ResponseEntity<ApiResponseDto<KycProfileResponseDto>> rejectKyc(
      @PathVariable UUID customerId,
      @RequestBody @Valid KycRejectionDto rejectionDto) {
    KycProfileResponseDto response = kycService.rejectKyc(customerId, rejectionDto);
    return ApiResponseUtil.success("KYC rejected successfully", response);
  }

  @DeleteMapping("/{customerId}/documents")
  @Operation(summary = "Delete KYC documents for a customer")
  public ResponseEntity<ApiResponseDto<Void>> deleteKycFiles(
      @PathVariable UUID customerId,
      @RequestBody List<UUID> documentIds) {
    kycService.deleteKycFiles(customerId, documentIds);
    return ApiResponseUtil.success("KYC documents deleted successfully", null);
  }

  @GetMapping()
  @Operation(summary = "Get paginated KYC profiles and also filter by status")
  public ResponseEntity<ApiResponseDto<Page<KycProfileResponseDto>>> getKycProfilesByStatus(
      @Valid @ModelAttribute @ParameterObject KycFilter filter) {
    Page<KycProfileResponseDto> response = kycService.getKycProfilesByStatus(filter);
    return ApiResponseUtil.success("KYC profiles fetched successfully", response);
  }

  @GetMapping("/count")
  @Operation(summary = "Get count of KYC profiles by status")
  public ResponseEntity<ApiResponseDto<Long>> getKycCountByStatus(@RequestParam KycStatus status) {
    long count = kycService.getKycCountByStatus(status);
    return ApiResponseUtil.success("KYC count fetched successfully", count);
  }

  @GetMapping("/{customerId}/document")
   @Operation(
      summary = "Get all KYC documents for a customer")
  public ResponseEntity<ApiResponseDto<DocumentListResponseDto>> getCustomerDocuments(
      @PathVariable UUID customerId) {

    DocumentListResponseDto documents = kycService.getCustomerDocuments(customerId);
    return ResponseEntity.ok(ApiResponseDto.success("Documents retrieved successfully", documents));
  }

  @GetMapping("/{customerId}/document/{docId}")
   @Operation(
      summary = "Get a specific KYC document for a customer" )
  public ResponseEntity<ApiResponseDto<KycDocumentDto>> getCustomerDocument(
      @PathVariable UUID customerId,
      @PathVariable UUID docId) {

    KycDocumentDto document = kycService.getCustomerDocument(customerId, docId);
    return ResponseEntity.ok(ApiResponseDto.success( "Document retrieved successfully", document));
  }

  @PostMapping(value="/{customerId}/document", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
   @Operation(
      summary = "Upload a document for a customer")
  public ResponseEntity<ApiResponseDto<KycDocumentDto>> uploadDocument(
      @PathVariable UUID customerId,
       @ModelAttribute @Valid DocumentUploadRequestDto request) {


    KycDocumentDto document = kycService.uploadDocument(customerId, request);
    return ResponseEntity.status(HttpStatus.CREATED)
        .body(ApiResponseDto.success("Document uploaded successfully", document));
  }

  @DeleteMapping("/{customerId}/document/{docId}")
  @Operation(
      summary = "Delete a customer's document")
  public ResponseEntity<ApiResponseDto<Void>> deleteCustomerDocument(
      @PathVariable UUID customerId,
      @PathVariable UUID docId) {


    kycService.deleteCustomerDocument(customerId, docId);
    return ResponseEntity.ok(ApiResponseDto.success("Document deleted successfully", null));
  }
}
