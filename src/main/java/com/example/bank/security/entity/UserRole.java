package com.example.bank.security.entity;

public enum UserRole {
  ADMIN("ADMIN", "System Administrator"),
  MANAGER("MANAGER", "Branch Manager"),
  TELLER("TELLER", "Bank Teller");

   private final String code;
    private final String description;

    UserRole(String code, String description) {
        this.code = code;
        this.description = description;
    }

    public String getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }
}
