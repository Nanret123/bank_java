package com.example.bank.account.services;

import org.springframework.data.jpa.domain.Specification;

import com.example.bank.account.entity.Account;
import com.example.bank.account.enums.AccountStatus;
import com.example.bank.account.enums.AccountType;

public class AccountSpecification {
  public static Specification<Account> hasBranchCode(String branchCode) {
    return (root, query, cb) -> branchCode == null ? null : cb.equal(root.get("branchCode"), branchCode);
  }

  public static Specification<Account> hasStatus(AccountStatus status) {
    return (root, query, cb) -> status == null ? null : cb.equal(root.get("status"), status);
  }

  public static Specification<Account> hasAccountType(AccountType type) {
    return (root, query, cb) -> type == null ? null : cb.equal(root.get("accountType"), type);
  }

}