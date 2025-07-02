package com.example.bank.account.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import com.example.bank.account.dtos.AccountResponse;
import com.example.bank.account.dtos.AccountSummaryResponse;
import com.example.bank.account.dtos.CreateAccountRequest;
import com.example.bank.account.dtos.UpdateAccountRequest;
import com.example.bank.account.entity.Account;

@Mapper(componentModel = "spring")
public interface AccountMapper {

  @Mapping(target = "customerId", source = "customer.id")
  AccountResponse toResponse(Account account);

  AccountSummaryResponse toSummaryResponse(Account account);

  Account toEntity(CreateAccountRequest request);

  @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
  void updateAccountFromRequest(UpdateAccountRequest request, @MappingTarget Account account);
}
