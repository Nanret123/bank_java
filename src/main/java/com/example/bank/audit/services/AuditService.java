package com.example.bank.audit.services;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.example.bank.audit.entity.AuditLog;
import com.example.bank.audit.repository.AuditLogRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class AuditService {
  private final AuditLogRepository auditLogRepository;

  public AuditService(AuditLogRepository auditLogRepository) {
    this.auditLogRepository = auditLogRepository;
  }

  @Async
  public void saveAuditLog(AuditLog auditLog) {
    try {
      auditLogRepository.save(auditLog);
      log.info("Audit log saved for user: {}, action: {}, entity: {}",
          auditLog.getUserId(), auditLog.getOperationType(), auditLog.getEntityType());
    } catch (Exception e) {
      log.error("Failed to save audit log: {}", e.getMessage(), e);
    }
  }
}
