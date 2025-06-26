package com.example.bank.customer.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.format.annotation.DateTimeFormat;

import com.example.bank.KYC.entity.KycProfile;
import com.example.bank.customer.enums.CustomerStatus;
import com.example.bank.customer.enums.CustomerType;
import com.example.bank.customer.enums.Gender;
import com.example.bank.customer.enums.MaritalStatus;
import com.example.bank.customer.enums.VerificationStatus;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "customers")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = false)
public class Customer {
    @Id
    @GeneratedValue
    private UUID id;

    @Column(name = "title", length = 10)
    private String title; // Mr, Mrs, Ms, Dr, etc.

    @Column(name = "first_name", nullable = false, length = 50)
    @Size(min = 2, max = 50, message = "First name must be between 2 and 50 characters")
    private String firstName;

    @Column(name = "middle_name", length = 50)
    private String middleName;

    @Column(name = "last_name", nullable = false, length = 50)
    @Size(min = 2, max = 50, message = "Last name must be between 2 and 50 characters")
    private String lastName;

    @Column(name = "date_of_birth", nullable = false)
    @Past(message = "Date of birth must be in the past")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateOfBirth;

    @Enumerated(EnumType.STRING)
    @Column(name = "gender", length = 10)
    private Gender gender;

    @Enumerated(EnumType.STRING)
    @Column(name = "marital_status", length = 20)
    private MaritalStatus maritalStatus;

    @Column(name = "nationality", length = 50)
    private String nationality;

    // Contact Information
    @Column(name = "email", unique = true, length = 100)
    private String email;

    @Column(name = "phone_number", nullable = false, length = 20)
    private String phoneNumber;

    @Column(name = "alternative_phone", length = 20)
    private String alternativePhone;

    // Address Information
    @Column(name = "residential_address", length = 255)
    private String residentialAddress;

    @Column(name = "city", length = 50)
    private String city;

    @Column(name = "state", length = 50)
    private String state;

    @Column(name = "country", length = 50)
    private String country;

    @OneToOne(mappedBy = "customer", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private KycProfile kyc;

    // Employment Information
    @Column(name = "occupation", length = 100)
    private String occupation;

    // Banking Information
    @Enumerated(EnumType.STRING)
    @Column(name = "customer_type", nullable = false, length = 20)
    private CustomerType customerType;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private CustomerStatus status;

    @Column(name = "status_change_reason")
    private String statusChangeReason;

    // Emergency Contact
    @Column(name = "emergency_contact_name", length = 100)
    private String emergencyContactName;

    @Column(name = "emergency_contact_relationship", length = 50)
    private String emergencyContactRelationship;

    @Column(name = "emergency_contact_phone", length = 20)
    private String emergencyContactPhone;

    // Branch Information
    @Column(name = "branch_code", length = 10)
    private String branchCode;

    // Audit Fields
    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @JoinColumn(name = "created_by")
    private UUID createdBy;

    @JoinColumn(name = "updated_by")
    private UUID updatedBy;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // Soft delete
    @Column(name = "is_deleted")
    @Builder.Default
    private Boolean isDeleted = false;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    @Column(name = "deleted_by", length = 50)
    private UUID deletedBy;

    @JoinColumn(name = "approved_by")
    private UUID approvedBy;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private VerificationStatus verificationStatus = VerificationStatus.PENDING;

    private LocalDateTime verifiedAt;

    // Relationships (if using JPA relationships)
    // @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, fetch =
    // FetchType.LAZY)
    // private List<Account> accounts;

    // Helper methods
    public String getFullName() {
        StringBuilder fullName = new StringBuilder();
        if (title != null && !title.isEmpty()) {
            fullName.append(title).append(" ");
        }
        fullName.append(firstName);
        if (middleName != null && !middleName.isEmpty()) {
            fullName.append(" ").append(middleName);
        }
        fullName.append(" ").append(lastName);
        return fullName.toString();
    }

    public Integer getAge() {
        if (dateOfBirth != null) {
            return Period.between(dateOfBirth, LocalDate.now()).getYears();
        }
        return null;
    }

    public boolean isActive() {
        return CustomerStatus.ACTIVE.equals(this.status);
    }
}
