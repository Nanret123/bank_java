package com.example.bank.account.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import com.cloudinary.provisioning.AccountConfiguration;
import com.example.bank.account.dtos.AccountConfigurationResponse;
import com.example.bank.account.dtos.UpdateAccountConfigurationRequest;



@Mapper(componentModel = "spring")
public interface AccountConfigurationMapper {

  AccountConfigurationResponse toResponse(com.example.bank.account.entity.AccountConfiguration savedConfig);

  @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
  void updateConfigurationFromRequest(UpdateAccountConfigurationRequest request, @MappingTarget com.example.bank.account.entity.AccountConfiguration config);
  
}
