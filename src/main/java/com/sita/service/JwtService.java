package com.sita.service;

import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {

    private static final String SECRET = "VeryLongSecretKeyForJwtThatIsEnoughLength12345";

    public String generate(Long userId) {
        return Jwts.builder()
                .setSubject(String.valueOf(userId))
                .signWith(Keys.hmacShaKeyFor(SECRET.getBytes()), SignatureAlgorithm.HS256)
                .compact();
    }
   
    public Long validate(String token) {
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(Keys.hmacShaKeyFor(SECRET.getBytes()))
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            String sub = claims.getSubject();

            if ("guest".equals(sub)) return 0L; // guest userId = 0

            return Long.parseLong(sub);

        } catch (Exception e) {
            return null;
        }
    }

    
    public String generateGuestToken() {
        return Jwts.builder()
                .setSubject("guest")
                .signWith(Keys.hmacShaKeyFor(SECRET.getBytes()), SignatureAlgorithm.HS256)
                .compact();
    }

}

