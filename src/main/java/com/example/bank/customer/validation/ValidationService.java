package com.example.bank.customer.validation;

import org.springframework.stereotype.Service;

import com.example.bank.common.exception.ValidationException;
import com.example.bank.customer.repository.CustomerRepository;
import com.example.bank.customer.validation.dtos.CustomerValidationRequest;
import com.example.bank.customer.validation.dtos.ValidationResponse;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class ValidationService {

  private final CustomerRepository customerRepo;

  public void validateCustomer(CustomerValidationRequest request) {
    if (request.getEmail() != null && customerRepo.existsByEmail(request.getEmail())) {
      throw new ValidationException("Email already exists");
    }

    if (request.getPhoneNumber() != null && customerRepo.existsByPhoneNumber(request.getPhoneNumber())) {
      throw new ValidationException("Phone number already exists");
    }
  }
}
