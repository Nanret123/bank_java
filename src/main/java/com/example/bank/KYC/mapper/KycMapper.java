package com.example.bank.KYC.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.example.bank.KYC.dtos.KycProfileResponse;
import com.example.bank.KYC.dtos.SubmitKycRequest;
import com.example.bank.KYC.entity.KycProfile;

@Mapper(componentModel = "spring")
public interface KycMapper {

  KycProfile toEntity(SubmitKycRequest request);

  @Mapping(target = "customerName", expression = "java(kyc.getCustomer().getFirstName() + \" \" + kyc.getCustomer().getLastName())")
  @Mapping(target = "customerId", source = "customer.id")
  KycProfileResponse toResponse(KycProfile kyc);

  List<KycProfileResponse> toResponseList(List<KycProfile> kycList);
}