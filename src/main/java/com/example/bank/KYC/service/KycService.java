package com.example.bank.KYC.service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.stereotype.Service;

import com.example.bank.FileStorage.Service.FileStorageService;
import com.example.bank.KYC.dtos.KycProfileResponse;
import com.example.bank.KYC.dtos.RejectKycRequest;
import com.example.bank.KYC.dtos.SubmitKycRequest;
import com.example.bank.KYC.dtos.KycProfileRequest;
import com.example.bank.KYC.dtos.UpdateKycRequest;
import com.example.bank.KYC.entity.Kyc;
import com.example.bank.KYC.enums.KycStatus;
import com.example.bank.KYC.exception.KycAlreadyExistsException;
import com.example.bank.KYC.mapper.KycMapper;
import com.example.bank.KYC.repository.KycRepository;
import com.example.bank.common.exception.ResourceNotFoundException;
import com.example.bank.customer.entity.Customer;
import com.example.bank.customer.exception.CustomerNotFoundException;
import com.example.bank.customer.repository.CustomerRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class KycService {

  private final KycRepository kycRepository;
  private final CustomerRepository customerRepository;
  private final KycMapper kycMapper;

  public KycProfileResponse submitKyc(UUID customerId, SubmitKycRequest request) {
    Customer customer = customerRepository.findById(customerId)
        .orElseThrow(() -> new ResourceNotFoundException("Customer not found"));

    if (customer.getKyc() != null) {
      throw new IllegalStateException("KYC already submitted. Use update endpoint.");
    }

    Kyc profile = kycMapper.toEntity(request);
    profile.setCustomer(customer);
    profile.setKycStatus(KycStatus.PENDING);

    Kyc saved = kycRepository.save(profile);
    return kycMapper.toResponse(saved);
  }

   public KycProfileResponse updateKyc(UUID customerId, UpdateKycRequest request) {
        findKycByCustomerId(UUID customerId)
        deleteOldCloudinaryFiles(profile, request);

        KycProfile updated = kycMapper.toEntity(request);
        updated.setId(profile.getId());
        updated.setCustomer(profile.getCustomer());
        updated.setKycStatus(KycStatus.PENDING);
        updated.setVerificationReason(null);
        updated.setSubmittedAt(LocalDateTime.now());

        return kycMapper.toResponse(kycProfileRepository.save(updated));
    }

    private Kyc findKycByCustomerId(UUID customerId){
       Kyc profile = kycRepository.findByCustomerId(customerId)
                .orElseThrow(() -> new ResourceNotFoundException("KYC not found"));
        return profile;

    }
}