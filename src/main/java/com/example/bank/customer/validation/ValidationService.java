package com.example.bank.customer.validation;

import org.springframework.stereotype.Service;

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

  public ValidationResponse validateCustomer(CustomerValidationRequest request) {
    ValidationResponse response = new ValidationResponse();

     if (request.getEmail() != null) {
            response.setEmailExists(customerRepo.existsByEmail(request.getEmail()));
        }

        if (request.getPhoneNumber() != null) {
            response.setPhoneNumberExists(customerRepo.existsByPhoneNumber(request.getPhoneNumber()));
        }
    return response;
  }
}
