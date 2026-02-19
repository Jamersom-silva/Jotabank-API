package com.jamersom.jotabank.auth;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.time.Instant;
import java.util.Date;

public final class JwtService {

    private final JwtProperties props;
    private final Key key;

    public JwtService(JwtProperties props) {
        this.props = props;
        this.key = Keys.hmacShaKeyFor(props.secret().getBytes(StandardCharsets.UTF_8));
    }

    public String generateToken(String subjectEmail) {
        Instant now = Instant.now();
        Instant exp = now.plusSeconds(props.expirationMinutes() * 60);

        return Jwts.builder()
                .issuer(props.issuer())
                .subject(subjectEmail)
                .issuedAt(Date.from(now))
                .expiration(Date.from(exp))
                .signWith(key)
                .compact();
    }

    public String extractSubject(String token) {
        return parseClaims(token).getSubject();
    }

    public boolean isValid(String token) {
        parseClaims(token); // se lançar exception, é inválido
        return true;
    }

    private Claims parseClaims(String token) {
        return Jwts.parser()
                .verifyWith((javax.crypto.SecretKey) key)
                .requireIssuer(props.issuer())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}
