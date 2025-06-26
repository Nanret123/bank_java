package com.example.bank.customer.mappers;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import com.example.bank.customer.dtos.CreateCustomerRequest;
import com.example.bank.customer.dtos.CustomerResponse;
import com.example.bank.customer.dtos.CustomerSummaryResponse;
import com.example.bank.customer.dtos.UpdateCustomerRequest;
import com.example.bank.customer.entity.Customer;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE, uses = {
    DateMapper.class })
public interface CustomerMapper {
  CustomerMapper INSTANCE = Mappers.getMapper(CustomerMapper.class);

  @Mapping(target = "fullName", expression = "java(customer.getFullName())")
  @Mapping(target = "age", expression = "java(customer.getAge())")
  CustomerResponse toResponse(Customer customer);

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "status", constant = "ACTIVE")
  @Mapping(target = "verificationStatus", constant = "PENDING")
  @Mapping(target = "createdAt", ignore = true)
  @Mapping(target = "updatedAt", ignore = true)
  @Mapping(target = "createdBy", ignore = true)
  @Mapping(target = "updatedBy", ignore = true)
  @Mapping(target = "isDeleted", constant = "false")
  @Mapping(target = "deletedAt", ignore = true)
  @Mapping(target = "deletedBy", ignore = true)
  Customer toEntity(CreateCustomerRequest request);

  @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
  void updateEntityFromRequest(UpdateCustomerRequest request, @MappingTarget Customer customer);

  @Mapping(target = "age", expression = "java(customer.getAge())")
  @Mapping(target = "fullName", expression = "java(customer.getFullName())")
  CustomerSummaryResponse toSummaryResponse(Customer customer);

}
