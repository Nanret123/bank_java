package com.example.bank.customer.service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.bank.KYC.enums.KycStatus;
import com.example.bank.audit.annotations.Auditable;
import com.example.bank.audit.enums.OperationType;
import com.example.bank.common.util.BuildPageable;
import com.example.bank.customer.dtos.CreateCustomerRequest;
import com.example.bank.customer.dtos.CustomerFilter;
import com.example.bank.customer.dtos.CustomerResponse;
import com.example.bank.customer.dtos.CustomerSearchRequest;
import com.example.bank.customer.dtos.CustomerStatisticsResponse;
import com.example.bank.customer.dtos.CustomerStatusUpdateRequest;
import com.example.bank.customer.dtos.CustomerSummaryResponse;
import com.example.bank.customer.dtos.CustomerVerificationRequest;
import com.example.bank.customer.dtos.UpdateCustomerRequest;
import com.example.bank.customer.entity.Customer;
import com.example.bank.customer.enums.CustomerStatus;
import com.example.bank.customer.exception.CustomerNotFoundException;
import com.example.bank.customer.interfaces.ICustomer;
import com.example.bank.customer.mappers.CustomerMapper;
import com.example.bank.customer.repository.CustomerRepository;
import com.example.bank.customer.validation.ValidationService;
import com.example.bank.customer.validation.dtos.CustomerValidationRequest;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class CustomerService implements ICustomer {
  private final CustomerRepository customerRepository;
  private final CustomerMapper customerMapper;
  private final ValidationService validationService;

  @Override
  @Auditable(operation = OperationType.CREATE, module = "customer", entityType = "CUSTOMER", captureArgs = true, captureResult = true)
  public CustomerResponse createCustomer(CreateCustomerRequest request, UUID userId) {
    // Validate request
    validationService.validateCustomer(
        CustomerValidationRequest.builder()
            .email(request.getEmail())
            .phoneNumber(request.getPhoneNumber())
            .build());

    Customer customer = customerMapper.toEntity(request);
    customer.setCreatedBy(userId);

    // Save customer
    Customer savedCustomer = customerRepository.save(customer);

    return customerMapper.toResponse(savedCustomer);

  }

  @Override
  @Transactional(readOnly = true)
  public Optional<CustomerResponse> getCustomerById(UUID customerId) {

    return customerRepository.findByIdAndIsDeletedFalse(customerId)
        .map(customerMapper::toResponse);
  }

  @Override
  @Auditable(operation = OperationType.UPDATE, module = "customer", entityType = "CUSTOMER", captureArgs = true, captureResult = true)
  public CustomerResponse updateCustomer(UUID customerId, UpdateCustomerRequest request, UUID userId) {

    Customer customer = getCustomerEntityById(customerId);
    // Customer originalCustomer = customer.clone(); // For audit trail

    // Update entity
    customerMapper.updateEntityFromRequest(request, customer);
    customer.setUpdatedBy(userId);
    customer.setUpdatedAt(LocalDateTime.now());

    // Save updated customer
    Customer updatedCustomer = customerRepository.save(customer);
    return customerMapper.toResponse(updatedCustomer);
  }

  private Customer getCustomerEntityById(UUID customerId) {
    return customerRepository.findByIdAndIsDeletedFalse(customerId)
        .orElseThrow(() -> new CustomerNotFoundException("Customer not found"));
  }

  @Override
  @Auditable(operation = OperationType.DELETE, module = "customer", entityType = "CUSTOMER", captureArgs = true, captureResult = false)
  public void deleteCustomer(UUID customerId, UUID userId) {

    Customer customer = getCustomerEntityById(customerId);

    // Soft delete
    customer.setIsDeleted(true);
    customer.setDeletedAt(LocalDateTime.now());
    customer.setDeletedBy(userId);
    customer.setStatus(CustomerStatus.CLOSED);

    customerRepository.save(customer);
  }

  @Override
  @Auditable(operation = OperationType.UPDATE, module = "customer", entityType = "CUSTOMER", captureArgs = true, captureResult = false)
  public void restoreCustomer(UUID customerId, UUID userId) {

    Customer customer = customerRepository.findById(customerId)
        .orElseThrow(() -> new CustomerNotFoundException("Customer not found"));

    // Soft delete
    customer.setIsDeleted(false);
    customer.setDeletedAt(LocalDateTime.now());
    customer.setDeletedBy(null);
    customer.setStatus(CustomerStatus.ACTIVE);

    customerRepository.save(customer);
  }

  @Override
  @Transactional(readOnly = true)
  public Page<CustomerSummaryResponse> getAllActiveCustomers(CustomerFilter filter) {
    log.info("Getting all customers with filters: {}", filter);

    // Build specification with filters (status, type, branch, etc.)
    Specification<Customer> specification = CustomerSpecification.buildFilterSpecification(filter);

    // Create pageable
    Pageable pageable = BuildPageable.createPageable(filter);

    // Execute query with filters
    Page<Customer> customers = customerRepository.findAll(specification, pageable);

    return customers.map(customerMapper::toSummaryResponse);
  }

  @Override
  @Transactional(readOnly = true)
  public Page<CustomerSummaryResponse> searchActiveCustomers(CustomerSearchRequest searchRequest) {

    // Build specification for text search
    Specification<Customer> specification = CustomerSpecification
        .buildTextSearchSpecification(searchRequest.getSearchText());

    // Create pageable
    Pageable pageable = BuildPageable.createPageable(searchRequest);

    // Execute text search
    Page<Customer> customers = customerRepository.findAllByIsDeletedFalse(specification, pageable);

    return customers.map(customerMapper::toSummaryResponse);
  }

  @Override
  @Transactional(readOnly = true)
  public Page<CustomerSummaryResponse> getAllCustomers(CustomerFilter filter) {
    log.info("Getting all customers with filters: {}", filter);

    // Build specification with filters (status, type, branch, etc.)
    Specification<Customer> specification = CustomerSpecification.buildFilterSpecification(filter);

    // Create pageable
    Pageable pageable = BuildPageable.createPageable(filter);

    // Execute query with filters
    Page<Customer> customers = customerRepository.findAll(specification, pageable);

    return customers.map(customerMapper::toSummaryResponse);
  }

  @Override
  @Transactional(readOnly = true)
  public Page<CustomerSummaryResponse> searchCustomers(CustomerSearchRequest searchRequest) {

    // Build specification for text search
    Specification<Customer> specification = CustomerSpecification
        .buildTextSearchSpecification(searchRequest.getSearchText());

    // Create pageable
    Pageable pageable = BuildPageable.createPageable(searchRequest);

    // Execute text search
    Page<Customer> customers = customerRepository.findAll(specification, pageable);

    return customers.map(customerMapper::toSummaryResponse);
  }

  @Override
  @Auditable(operation = OperationType.UPDATE, module = "customer", entityType = "CUSTOMER", captureArgs = true, captureResult = true)
  public void verifyCustomer(CustomerVerificationRequest request, UUID userId) {
    Customer customer = getCustomerEntityById(request.getCustomerId());

    customer.setVerificationStatus(request.getVerificationStatus());
    customer.setApprovedBy(userId);
    customer.setVerifiedAt(LocalDateTime.now());

    customerRepository.save(customer);
  }

  @Override
  @Auditable(operation = OperationType.UPDATE, module = "customer", entityType = "CUSTOMER", captureArgs = true, captureResult = true)
  public CustomerResponse updateCustomerStatus(UUID customerId, CustomerStatusUpdateRequest request, UUID userId) {
    Customer customer = getCustomerEntityById(customerId);
    CustomerStatus oldStatus = customer.getStatus();

    customer.setStatus(request.getStatus());
    customer.setUpdatedBy(userId);
    customer.setUpdatedAt(LocalDateTime.now());
    customer.setStatusChangeReason(request.getReason());

    Customer updatedCustomer = customerRepository.save(customer);

    return customerMapper.toResponse(updatedCustomer);
  }

  @Override
  @Transactional(readOnly = true)
  public CustomerStatisticsResponse getCustomerStatistics() {
    // TODO: Implement statistics calculation
    return CustomerStatisticsResponse.builder()
        .totalCustomers(customerRepository.countByIsDeletedFalse())
        .activeCustomers(customerRepository.countByStatusAndIsDeletedFalse(CustomerStatus.ACTIVE))
        .inactiveCustomers(customerRepository.countByStatusAndIsDeletedFalse(CustomerStatus.INACTIVE))
        .suspendedCustomers(customerRepository.countByStatusAndIsDeletedFalse(CustomerStatus.SUSPENDED))
        .kycPendingCustomers(customerRepository.countByKyc_KycStatusAndIsDeletedFalse(KycStatus.PENDING))
        .kycCompletedCustomers(customerRepository.countByKyc_KycStatusAndIsDeletedFalse(KycStatus.APPROVED))
        .build();
  }

}
