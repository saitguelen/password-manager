package com.sait.password_manager;
//JWT - JSON Web Token)
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JwtUtil {

    // Wir holen den geheimen Schlüssel und die Gültigkeitsdauer aus application.properties.
    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration.ms}")
    private long jwtExpirationMs;

    private SecretKey getSigningKey() {
        // Wir konvertieren den geheimen Schlüssel in ein sicheres Format.
        return Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    // Erstellt ein JWT mit den Benutzerinformationen.

    // Extrahiert den Benutzernamen aus dem Token
    public String extractUsername(String token) {
        return extractAllClaims(token).getSubject();
    }

    // Überprüft die Gültigkeit des Tokens
    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        // Sowohl der Benutzername muss übereinstimmen als auch das Token darf nicht abgelaufen sein
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    private boolean isTokenExpired(String token) {
        // Vergleicht das Ablaufdatum des Tokens mit dem heutigen Datum
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractAllClaims(token).getExpiration();
    }

    private Claims extractAllClaims(String token) {
        // Parst das Token und extrahiert alle darin enthaltenen Informationen (Claims)
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public String generateToken(Authentication authentication) {
        String username = authentication.getName();
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtExpirationMs);

        return Jwts.builder()
                .subject(username) // Der Inhaber des Tokens (Benutzername)
                .issuedAt(now) // Erstellungszeitpunkt des Tokens
                .expiration(expiryDate) // Ablaufzeitpunkt des Tokens
                .signWith(getSigningKey()) // Wir signieren es mit unserem geheimen Schlüssel
                .compact(); // Wir wandeln es in einen String um.


    }
}