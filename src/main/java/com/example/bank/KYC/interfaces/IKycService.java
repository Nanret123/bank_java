package com.example.bank.KYC.interfaces;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.example.bank.KYC.dto.KycApprovalDto;
import com.example.bank.KYC.dto.KycFilter;
import com.example.bank.KYC.dto.KycProfileResponseDto;
import com.example.bank.KYC.dto.KycRejectionDto;
import com.example.bank.KYC.dto.KycSubmissionDto;
import com.example.bank.KYC.dto.KycUpdateDto;
import com.example.bank.KYC.enums.KycStatus;

public interface IKycService {
    
    KycProfileResponseDto submitKyc(KycSubmissionDto submissionDto);
    
    KycProfileResponseDto updateKyc(UUID customerId, KycUpdateDto updateDto);
    
    KycProfileResponseDto getKycProfile(UUID customerId);
    
    KycProfileResponseDto approveKyc(UUID customerId, KycApprovalDto approvalDto);
    
    KycProfileResponseDto rejectKyc(UUID customerId, KycRejectionDto rejectionDto);
    
    void deleteKycFiles(UUID customerId, List<UUID> documentIds);
    
    Page<KycProfileResponseDto> getKycProfilesByStatus(KycFilter filter);
    
    long getKycCountByStatus(KycStatus status);
}