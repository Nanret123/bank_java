package com.example.bank.audit.context;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;

import com.example.bank.security.service.UserPrincipal;

import jakarta.servlet.http.HttpServletRequest;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Component
@RequestScope
@Getter
@Slf4j
public class AuditContext {

  private final String userId;
  private final String userEmail;
  private final String ipAddress;
  private final String userAgent;

  public AuditContext(HttpServletRequest request) {
    // Extract from security context
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

    if (authentication != null && authentication.isAuthenticated()
        && authentication.getPrincipal() instanceof UserPrincipal user) {
      this.userId = user.getId().toString();
      this.userEmail = user.getEmail();
    } else {
      this.userId = "anonymous";
      this.userEmail = "anonymous";
    }

    this.ipAddress = extractClientIp(request);
    this.userAgent = request.getHeader("User-Agent");
  }

  private String extractClientIp(HttpServletRequest request) {
    String xfHeader = request.getHeader("X-Forwarded-For");
    return xfHeader != null ? xfHeader.split(",")[0] : request.getRemoteAddr();
  }
}