package com.example.bank.audit.entity;

import java.time.LocalDateTime;
import java.util.UUID;

import com.example.bank.audit.enums.OperationType;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "audit_logs")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuditLog {
  @Id
  @GeneratedValue
  private UUID id;

  @Column(name = "user_id")
  private String userId;

  @Column(name = "user_email")
  private String userEmail;

  @Column(name = "module_name")
  private String moduleName;

  @Column(name = "action_type")
  @Enumerated(EnumType.STRING)
  private OperationType operationType;


  @Column(name = "arguments", columnDefinition = "TEXT")
  private String arguments;

  @Column(name = "result", columnDefinition = "TEXT")
  private String result;

  @Column(name = "timestamp")
  private LocalDateTime timestamp;

  @Column(name = "execution_time_ms")
  private Long executionTimeMs;

  @Column(name = "success")
  private Boolean success;

  @Column(name = "error_message")
  private String errorMessage;

  @Column(name = "ip_address")
  private String ipAddress;
}
