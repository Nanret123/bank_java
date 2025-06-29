package com.example.bank.KYC.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.domain.Specification;

import com.example.bank.KYC.dto.KycFilter;
import com.example.bank.KYC.entity.KycProfile;

import jakarta.persistence.criteria.Predicate;

public class KycSpecification {

  public static Specification<KycProfile> withFilters(KycFilter filter) {
    return (root, query, cb) -> {
      List<Predicate> predicates = new ArrayList<>();

      if (filter.getStatus() != null) {
        predicates.add(cb.equal(root.get("kycStatus"), filter.getStatus()));
      }

      if (filter.getCreatedAt() != null) {
        // Compare only the date part of createdAt
        LocalDateTime startOfDay = filter.getCreatedAt().atStartOfDay();
        LocalDateTime endOfDay = startOfDay.plusDays(1).minusSeconds(1);

        predicates.add(cb.between(root.get("createdAt"), startOfDay, endOfDay));
      }

      // You can add more filters here later (e.g., customer name, date range)

      return cb.and(predicates.toArray(new Predicate[0]));
    };
  }
}