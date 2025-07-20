package com.example.bank.audit.aspect;

import java.time.LocalDateTime;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import com.example.bank.audit.annotations.Auditable;
import com.example.bank.audit.context.AuditContext;
import com.example.bank.audit.entity.AuditLog;
import com.example.bank.audit.services.AuditService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Aspect
@Component
@Slf4j
@RequiredArgsConstructor
public class AuditAspect {

  private final AuditService auditService;
  private final AuditContext auditContext;
  private final ObjectMapper objectMapper;
  private final HttpServletRequest request;

  @Around("@annotation(auditable)")
  public Object audit(ProceedingJoinPoint joinPoint, Auditable auditable) throws Throwable {
    long startTime = System.currentTimeMillis();
    boolean success = true;
    String errorMessage = null;
    Object result = null;

    try {
      result = joinPoint.proceed(); // Execute the method
      return result;
    } catch (Exception e) {
      success = false;
      errorMessage = e.getMessage();
      throw e;
    } finally {
      logAudit(joinPoint, auditable, result, success, errorMessage, startTime);
    }
  }

  private void logAudit(ProceedingJoinPoint joinPoint, Auditable auditable, Object result,
      boolean success, String errorMessage, long startTime) {
    try {
      String userId = auditContext.getUserId();
      String userEmail = auditContext.getUserEmail();
      String className = joinPoint.getTarget().getClass().getSimpleName();

      String ipAddress = request.getHeader("X-Forwarded-For");
      if (ipAddress == null || ipAddress.isEmpty()) {
        ipAddress = request.getRemoteAddr();
      }

      AuditLog logEntry = AuditLog.builder()
          .operationType(auditable.operation())
          .userId(userId)
          .userEmail(userEmail)
          .moduleName(auditable.module().isEmpty() ? className.toLowerCase() : auditable.module())
          .timestamp(LocalDateTime.now())
          .success(success)
          .errorMessage(errorMessage)
          .executionTimeMs(System.currentTimeMillis() - startTime)
          .ipAddress(ipAddress)
          .build();

      // Optionally capture arguments
      try {
        if (auditable.captureArgs()) {
          try {
            String argsJson = objectMapper.writeValueAsString(joinPoint.getArgs());
            logEntry.setArguments(argsJson);
          } catch (JsonProcessingException e) {
            logEntry.setArguments("Failed to serialize arguments: " + e.getMessage());
          }
        }

        if (auditable.captureResult() && result != null) {
          try {
            String resultJson = objectMapper.writeValueAsString(result);
            logEntry.setResult(resultJson);
          } catch (JsonProcessingException e) {
            logEntry.setResult("Failed to serialize result: " + e.getMessage());
          }
        }
      } catch (Exception e) {
        // Avoid crashing the AOP due to audit serialization issues
        log.error("Error while capturing audit arguments or result", e);
      }

      auditService.saveAuditLog(logEntry);

    } catch (Exception e) {
      log.warn("Audit logging failed: {}", e.getMessage());
    }
  }
}
