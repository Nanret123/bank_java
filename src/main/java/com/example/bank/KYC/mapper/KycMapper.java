package com.example.bank.KYC.mapper;

import java.util.List;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

import com.example.bank.KYC.dto.KycDocumentDto;
import com.example.bank.KYC.dto.KycProfileResponseDto;
import com.example.bank.KYC.dto.KycSubmissionDto;
import com.example.bank.KYC.dto.KycUpdateDto;
import com.example.bank.KYC.entity.KycDocument;
import com.example.bank.KYC.entity.KycProfile;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface KycMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "customer", ignore = true)
    @Mapping(target = "kycStatus", constant = "PENDING")
    @Mapping(target = "documents", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "rejectionReason", ignore = true)
    @Mapping(target = "reviewedBy", ignore = true)
    @Mapping(target = "reviewedAt", ignore = true)
    KycProfile toEntity(KycSubmissionDto dto);

    @Mapping(target = "customerId", source = "customer.id")
    @Mapping(target = "customerFirstName", source = "customer.firstName")
    @Mapping(target = "customerLastName", source = "customer.lastName")
    @Mapping(target = "customerEmail", source = "customer.email")
    @Mapping(target = "customerPhone", source = "customer.phoneNumber")
    @Mapping(target = "nationality", source = "customer.nationality")
    @Mapping(target = "city", source = "customer.city")
    @Mapping(target = "state", source = "customer.state")
    @Mapping(target = "country", source = "customer.country")
    @Mapping(target = "occupation", source = "customer.occupation")
    @Mapping(target = "dateOfBirth", source = "customer.dateOfBirth")
    @Mapping(target = "status", source = "kycStatus") 
    KycProfileResponseDto toResponseDto(KycProfile entity);

    List<KycDocumentDto> toDocumentDtos(List<KycDocument> documents);

    KycDocumentDto toDocumentDto(KycDocument document);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "customer", ignore = true)
    @Mapping(target = "kycStatus", ignore = true)
    @Mapping(target = "documents", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "rejectionReason", ignore = true)
    @Mapping(target = "reviewedBy", ignore = true)
    @Mapping(target = "reviewedAt", ignore = true)
    void updateEntityFromDto(KycUpdateDto dto, @MappingTarget KycProfile entity);
}
