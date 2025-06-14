package com.example.bank.security.JWT;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.example.bank.security.entity.User;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.Claims;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class JwtService {

  @Value("${app.jwt.secret}")
  private String jwtSecret;

  @Value("${app.jwt.access-token-expiration}")
  private Long accessTokenExpiration;

  @Value("${app.jwt.refresh-token-expiration}")
  private Long refreshTokenExpiration;

  public String generateAccessToken(User user){
    Map<String, Object> claims = new HashMap<>();
    claims.put("role", user.getRole().getCode());
    claims.put("branchCode", user.getBranchCode());
    claims.put("fullName", user.getFullName());
    claims.put("type", "ACCESS");

    return createToken(claims, user.getUsername(), accessTokenExpiration);
  }

  public String generateRefreshToken(){
    return UUID.randomUUID().toString().replace("-", "");
  }

  private Key getSigningKey() {
    byte[] keyBytes = Decoders.BASE64.decode(jwtSecret); // import io.jsonwebtoken.io.Decoders;
    return Keys.hmacShaKeyFor(keyBytes);
}

  private String createToken(Map<String, Object> claims, String subject, Long expiration) {
     return Jwts.builder()
        .setClaims(claims)
        .setSubject(subject)
        .setIssuedAt(new Date(System.currentTimeMillis()))
        .setExpiration(new Date(System.currentTimeMillis() + expiration))
        .signWith(getSigningKey(),SignatureAlgorithm.HS512)
        .compact();
  }

  public String getUsernameFromToken(String token) {
   return getClaimFromToken(token, Claims::getSubject);
  }

  public Date getExpirationDateFromToken(String token) {
    return getClaimFromToken(token, Claims::getExpiration);
  }

  public String getRoleFromToken(String token){
    return (String) getAllClaimsFromToken(token).get("role");
  }

  public String getBranchCodeFromToken(String token){
    return (String) getAllClaimsFromToken(token).get("branchCode");
  }

  public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
    final Claims claims = getAllClaimsFromToken(token);
    return claimsResolver.apply(claims);
  }

  private Claims getAllClaimsFromToken(String token) {
    return Jwts.parserBuilder()
        .setSigningKey(getSigningKey())
        .build()
        .parseClaimsJws(token)
        .getBody();
  }

  public boolean isTokenExpired(String token) {
    final Date expiration = getExpirationDateFromToken(token);
    return expiration.before(new Date());
  }
  public boolean validateToken(String token, String username) {
    final String tokenUsername = getUsernameFromToken(token);
    return (tokenUsername.equals(username) && !isTokenExpired(token));
  }
}
