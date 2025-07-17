package com.example.bank.transactions.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.domain.Specification;

import com.example.bank.transactions.dtos.TransactionFilter;
import com.example.bank.transactions.entity.Transaction;

import jakarta.persistence.criteria.Predicate;

public class TransactionSpecification {
  public static Specification<Transaction> buildSpecification(TransactionFilter filter) {
    return (root, query, criteriaBuilder) -> {
      List<Predicate> predicates = new ArrayList<>();

      if (filter.getTransactionType() != null) {
        predicates.add(criteriaBuilder.equal(root.get("transactionType"), filter.getTransactionType()));
      }

      if (filter.getSourceAccountNumber() != null) {
        predicates.add(criteriaBuilder.equal(root.get("fromAccountNumber"), filter.getSourceAccountNumber()));
      }

      if (filter.getDestinationAccountNumber() != null) {
        predicates
            .add(criteriaBuilder.equal(root.get("toAccountNumber"), filter.getDestinationAccountNumber()));
      }

      if (filter.getStatus() != null) {
        predicates.add(criteriaBuilder.equal(root.get("status"), filter.getStatus()));
      }

      if (filter.getInitiatedBy() != null) {
        predicates.add(criteriaBuilder.equal(root.get("initiatedBy"), filter.getInitiatedBy()));
      }

      if (filter.getApprovedBy() != null) {
        predicates.add(criteriaBuilder.equal(root.get("approvedBy"), filter.getApprovedBy()));
      }

      if (filter.getFromDateTime() != null) {
        predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("transactionDate"), filter.getFromDateTime()));
      }

      if (filter.getToDateTime() != null) {
        predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("transactionDate"), filter.getToDateTime()));
      }

      if (filter.getMinAmount() != null) {
        predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("amount"), filter.getMinAmount()));
      }

      if (filter.getMaxAmount() != null) {
        predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("amount"), filter.getMaxAmount()));
      }

      if (filter.getTransactionReference() != null) {
        predicates.add(criteriaBuilder.like(
            criteriaBuilder.lower(root.get("transactionReference")),
            "%" + filter.getTransactionReference().toLowerCase() + "%"));
      }

      return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
    };
  }

}
