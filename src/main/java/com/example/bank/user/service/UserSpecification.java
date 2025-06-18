package com.example.bank.user.service;

import org.springframework.data.jpa.domain.Specification;

import com.example.bank.security.entity.User;
import com.example.bank.security.entity.UserRole;

public class UserSpecification {

  public static Specification<User> hasRole(UserRole role) {
    return (root, query, cb) -> {
      if (role == null)
        return cb.conjunction();
      return cb.equal(root.get("role"), role);
    };
  }

  public static Specification<User> hasBranchCode(String branchCode) {
    return (root, query, cb) -> {
      if (branchCode == null || branchCode.trim().isEmpty())
        return cb.conjunction();
      return cb.equal(root.get("branchCode"), branchCode);
    };
  }

  public static Specification<User> isActive(Boolean active) {
    return (root, query, cb) -> {
      if (active == null)
        return cb.conjunction();
      return cb.equal(root.get("isActive"), active);
    };
  }

  public static Specification<User> nameContains(String keyword) {
    return (root, query, cb) -> {
      if (keyword == null || keyword.trim().isEmpty())
        return cb.conjunction();
      return cb.like(cb.lower(root.get("fullName")), "%" + keyword.toLowerCase() + "%");
    };
  }
}
