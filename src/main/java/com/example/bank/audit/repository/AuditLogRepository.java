package com.example.bank.audit.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.bank.audit.entity.AuditLog;
import com.example.bank.audit.enums.OperationType;

@Repository
public interface AuditLogRepository extends JpaRepository<AuditLog, UUID> {
    List<AuditLog> findByUserIdAndTimestampBetween(String userId, LocalDateTime start, LocalDateTime end);
    List<AuditLog> findByEntityTypeAndEntityId(String entityType, String entityId);
    List<AuditLog> findByModuleNameAndActionType(String moduleName, OperationType actionType);
    
    @Query("SELECT a FROM AuditLog a WHERE a.userId = :userId AND a.actionType IN :actionTypes ORDER BY a.timestamp DESC")
    List<AuditLog> findUserActions(@Param("userId") String userId, @Param("actionTypes") List<OperationType> actionTypes);
}