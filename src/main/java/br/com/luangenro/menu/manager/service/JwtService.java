package br.com.luangenro.menu.manager.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import java.util.Date;
import java.util.function.Function;
import javax.crypto.SecretKey;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

/**
 * A service for handling JWT (JSON Web Token) operations, such as generation, validation, and claim
 * extraction.
 */
@Service
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
    return extractClaim(token, Claims::getSubject);
  }

  /**
   * Generates a new JWT token for a given user.
   *
   * @param userDetails The user details to build the token from.
   * @return A new JWT token string.
   */
  public String generateToken(UserDetails userDetails) {
    return Jwts.builder()
        .subject(userDetails.getUsername())
        .issuedAt(new Date(System.currentTimeMillis()))
        .expiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME_MS))
        .signWith(getSignInKey())
        .compact();
  }

  /**
   * Validates a JWT token against user details.
   *
   * @param token The token to validate.
   * @param userDetails The user to validate against.
   * @return True if the token is valid, false otherwise.
   */
  public boolean isTokenValid(String token, UserDetails userDetails) {
    final String username = extractUsername(token);
    return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
  }

  private boolean isTokenExpired(String token) {
    return extractClaim(token, Claims::getExpiration).before(new Date());
  }

  private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
    final Claims claims = extractAllClaims(token);
    return claimsResolver.apply(claims);
  }

  private Claims extractAllClaims(String token) {
    return Jwts.parser().verifyWith(getSignInKey()).build().parseSignedClaims(token).getPayload();
  }

  private SecretKey getSignInKey() {
    byte[] keyBytes = Decoders.BASE64URL.decode(secretKey);
    return Keys.hmacShaKeyFor(keyBytes);
  }
}
