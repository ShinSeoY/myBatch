package com.my.batch.common.security;

import com.my.batch.domain.Member;
import com.my.batch.exception.error.InvalidTokenException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class AuthenticationTokenProvider {

    private static final String JWT_TYPE = "JWT";
    private static final String ALGORITHM = "HS256";

    @Value("${security.jwt.key-value}")
    String jwtKey;
    Key key;

    @PostConstruct
    protected void init() {
        this.key = Keys.hmacShaKeyFor(jwtKey.getBytes(StandardCharsets.UTF_8));
    }


    public String findToken(final String headerValue) {
        String bearerName = "Bearer ";
        if (headerValue.startsWith(bearerName)) {
            return headerValue.substring(bearerName.length());
        }
        return null;
    }

    public String generateJwtToken(final Member member) {
        Map<String, Object> payload = new HashMap<>();
        payload.put("email", member.getEmail());
        return generateJwtToken(payload);
    }


    private String generateJwtToken(Map<String, Object> payload) {
        final long now = System.currentTimeMillis();
        final long expiration = now + 1000 * 60 * 24 * 7;
        final String jwtId = UUID.randomUUID().toString();

        Map<String, Object> headers = new HashMap<>();
        headers.put("typ", JWT_TYPE);
        headers.put("alg", ALGORITHM);

        return Jwts.builder()
                .setId(jwtId)
                .setAudience("myBatch")
                .setHeader(headers)
                .setIssuedAt(new Date(now))
                .setExpiration(new Date(expiration))
                .addClaims(payload)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public String getEmail(final String jwtString) {
        Map<String, Object> payload = getPayload(jwtString);
        return (String) payload.get("email");
    }

    private Map<String, Object> getPayload(final String jwtString) {
        try {
            Claims claims = (Claims) (Jwts.parserBuilder().setSigningKey(key).build().parse(jwtString).getBody());

            if (isNonExpired(claims.getExpiration())) {
                Map<String, Object> payload = new HashMap<>();
                for (String key : claims.keySet()) {
                    payload.put(key, claims.get(key));
                }
                return payload;
            } else {
                throw new InvalidTokenException();
            }
        } catch (InvalidTokenException e) {
            throw e;
        } catch (Exception e) {
            throw new InvalidTokenException();
        }
    }

    private boolean isNonExpired(Date date) {
        return date != null && date.after(new Date());
    }

}
