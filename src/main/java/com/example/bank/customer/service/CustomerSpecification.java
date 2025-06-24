package com.example.bank.customer.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import com.example.bank.customer.dtos.CustomerFilter;
import com.example.bank.customer.entity.Customer;

import jakarta.persistence.criteria.Predicate;

public class CustomerSpecification {

  public static Specification<Customer> buildFilterSpecification(CustomerFilter filter) {
    return (root, query, criteriaBuilder) -> {
      List<Predicate> predicates = new ArrayList<>();

      // Filter by status
      if (filter.getStatus() != null) {
        predicates.add(criteriaBuilder.equal(root.get("status"), filter.getStatus()));
      }

      // Filter by customer type
      if (filter.getCustomerType() != null) {
        predicates.add(criteriaBuilder.equal(root.get("customerType"), filter.getCustomerType()));
      }

      // Filter by branch code
      if (StringUtils.hasText(filter.getBranchCode())) {
        predicates.add(criteriaBuilder.equal(root.get("branchCode"), filter.getBranchCode()));
      }

      // Filter by KYC status
      if (filter.getKycStatus() != null) {
        predicates.add(criteriaBuilder.equal(root.get("kycStatus"), filter.getKycStatus()));
      }

      // Filter by risk rating
      if (filter.getRiskRating() != null) {
        predicates.add(criteriaBuilder.equal(root.get("riskRating"), filter.getRiskRating()));
      }

      if (StringUtils.hasText(filter.getCreatedAt())) {
        try {
          LocalDate date = LocalDate.parse(filter.getCreatedAt(), DateTimeFormatter.ofPattern("yyyy-MM-dd"));
          LocalDateTime startOfDay = date.atStartOfDay();
          LocalDateTime endOfDay = date.atTime(23, 59, 59);

          predicates.add(criteriaBuilder.between(root.get("createdAt"), startOfDay, endOfDay));
        } catch (Exception e) {
          // Log the error or handle invalid date format
          System.err.println("Invalid date format: " + filter.getCreatedAt());
        }
      }

      return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
    };
  }

  public static Specification<Customer> buildTextSearchSpecification(String searchText) {
    return (root, query, criteriaBuilder) -> {
      if (!StringUtils.hasText(searchText)) {
        return criteriaBuilder.conjunction(); // Return all if no search text
      }

      String searchPattern = "%" + searchText.toLowerCase() + "%";

      List<Predicate> searchPredicates = new ArrayList<>();

      // Search in customer name
      searchPredicates.add(criteriaBuilder.like(
          criteriaBuilder.lower(root.get("customerName")), searchPattern));

      // Search in email
      searchPredicates.add(criteriaBuilder.like(
          criteriaBuilder.lower(root.get("email")), searchPattern));

      // Search in phone number
      searchPredicates.add(criteriaBuilder.like(
          criteriaBuilder.lower(root.get("phoneNumber")), searchPattern));

      // Search in branch code
      searchPredicates.add(criteriaBuilder.like(
          criteriaBuilder.lower(root.get("branchCode")), searchPattern));

      // Use OR condition for text search (match any field)
      return criteriaBuilder.or(searchPredicates.toArray(new Predicate[0]));
    };
  }
}
