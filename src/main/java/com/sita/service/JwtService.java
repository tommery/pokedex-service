package com.sita.service;

import java.security.Key;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {

	private final Key secretKey = Keys.hmacShaKeyFor(
            "THIS_IS_A_VERY_LONG_SECRET_KEY_FOR_HS256_32_BYTES_MINIMUM".getBytes()
    );
	
	public String generateToken(String email) {
        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(new Date())
                .setExpiration(Date.from(Instant.now().plus(24, ChronoUnit.HOURS)))
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();
    }
	
	public String extractEmail(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }
  
	public boolean validate(String token) {
	    try {
	        Claims claims = Jwts.parserBuilder()
	                .setSigningKey(secretKey)  
	                .build()
	                .parseClaimsJws(token)
	                .getBody();

	        String sub = claims.getSubject();

	        if (sub == null) {
	        	System.err.println("Invalid Token:" + token);
	            return false;
	        }

	        return true;
//	        // guest user → return 0
//	        if (sub.equals("guest")) {
//	            return 0L;
//	        }
//
//	        return Long.parseLong(sub);

	    } catch (JwtException | IllegalArgumentException e) {
	        // token invalid / expired / malformed
	        return false;
	    }
	}
    
	public String generateGuestToken() {
	    return Jwts.builder()
	            .setSubject("guest")
	            .signWith(secretKey, SignatureAlgorithm.HS256)
	            .compact();
	}


}

