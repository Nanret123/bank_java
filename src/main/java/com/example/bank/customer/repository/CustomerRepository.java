package com.example.bank.customer.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.example.bank.KYC.enums.KycStatus;
import com.example.bank.customer.entity.Customer;
import com.example.bank.customer.enums.CustomerStatus;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, UUID>, JpaSpecificationExecutor<Customer> {
  boolean existsByEmail(String email);

  boolean existsByPhoneNumber(String phoneNumber);

  Optional<Customer> findByIdAndIsDeletedFalse(UUID customerId);

  Page<Customer> findAll(Specification<Customer> spec, Pageable pageable);

  Page<Customer> findAllByIsDeletedFalse(Specification<Customer> spec, Pageable pageable);

  long countByIsDeletedFalse();

  long countByStatusAndIsDeletedFalse(CustomerStatus status);

  // Count KYC completed customers that are not soft-deleted
  long countByKycStatusAndIsDeletedFalse(KycStatus kycStatus);
}