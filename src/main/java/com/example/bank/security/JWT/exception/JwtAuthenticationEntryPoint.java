package com.example.bank.security.JWT.exception;

import java.io.PrintWriter;
import java.io.IOException;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {
  @Override
public void commence(HttpServletRequest request,
    HttpServletResponse response,
    AuthenticationException authException) throws IOException {
  log.error("Unauthorized error: {}", authException.getMessage());
  response.setContentType("application/json");
  response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

  PrintWriter writer = response.getWriter();
  writer.println("{\"error\": \"Unauthorized\", \"message\": \"" + authException.getMessage() + "\"}");
}
}