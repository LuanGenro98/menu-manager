package br.com.luangenro.menu.manager.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import java.util.Date;
import java.util.function.Function;
import javax.crypto.SecretKey;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

/**
 * A service for handling JWT (JSON Web Token) operations, such as generation, validation, and claim
 * extraction.
 */
@Service
@Slf4j
public class JwtService {

  @Value("${jwt.secret-key}")
  private String secretKey;

  private static final long EXPIRATION_TIME_MS = 1000 * 60 * 60 * 24; // 24 hours

  /**
   * Extracts the username from a JWT token.
   *
   * @param token The JWT token.
   * @return The username (subject) from the token.
   */
  public String extractUsername(String token) {
    log.info("Attempting to extract username from token.");
    return extractClaim(token, Claims::getSubject);
  }

  /**
   * Generates a new JWT token for a given user.
   *
   * @param userDetails The user details to build the token from.
   * @return A new JWT token string.
   */
  public String generateToken(UserDetails userDetails) {
    log.info("Generating new JWT token for user: {}", userDetails.getUsername());
    String token =
        Jwts.builder()
            .subject(userDetails.getUsername())
            .issuedAt(new Date(System.currentTimeMillis()))
            .expiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME_MS))
            .signWith(getSignInKey())
            .compact();
    log.info("Token generated successfully for user: {}", userDetails.getUsername());
    return token;
  }

  /**
   * Validates a JWT token against user details.
   *
   * @param token The token to validate.
   * @param userDetails The user to validate against.
   * @return True if the token is valid, false otherwise.
   */
  public boolean isTokenValid(String token, UserDetails userDetails) {
    log.info("Validating token for user: {}", userDetails.getUsername());
    try {
      final String username = extractUsername(token);
      boolean isTokenValid = username.equals(userDetails.getUsername()) && !isTokenExpired(token);
      if (!isTokenValid) {
        log.warn(
            "Token validation failed for user '{}'. Username match: {}, Token expired: {}.",
            userDetails.getUsername(),
            username.equals(userDetails.getUsername()),
            isTokenExpired(token));
      }
      return isTokenValid;
    } catch (Exception e) {
      return false;
    }
  }

  private boolean isTokenExpired(String token) {
    boolean isExpired = extractClaim(token, Claims::getExpiration).before(new Date());
    if (isExpired) {
      log.warn("Token has expired. Expiration: {}", extractClaim(token, Claims::getExpiration));
    }
    return isExpired;
  }

  private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
    final Claims claims = extractAllClaims(token);
    return claimsResolver.apply(claims);
  }

  private Claims extractAllClaims(String token) {
    try {
      return Jwts.parser().verifyWith(getSignInKey()).build().parseSignedClaims(token).getPayload();
    } catch (MalformedJwtException e) {
      log.warn("Invalid JWT token format: {}", e.getMessage());
    } catch (ExpiredJwtException e) {
      log.warn("JWT token has expired: {}", e.getMessage());
    } catch (UnsupportedJwtException e) {
      log.warn("Unsupported JWT token: {}", e.getMessage());
    } catch (IllegalArgumentException e) {
      log.warn("JWT claims string is empty or null: {}", e.getMessage());
    } catch (SignatureException e) {
      log.warn("JWT signature validation failed: {}", e.getMessage());
    }
    throw new IllegalStateException("Invalid JWT token");
  }

  private SecretKey getSignInKey() {
    byte[] keyBytes = Decoders.BASE64URL.decode(secretKey);
    return Keys.hmacShaKeyFor(keyBytes);
  }
}
