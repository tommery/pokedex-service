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
	
	public String generateToken(Long userId) {
		if (userId == null) {
			return null;
		}
        return Jwts.builder()
                .setSubject(userId.toString())
                .setIssuedAt(new Date())
                .setExpiration(Date.from(Instant.now().plus(24, ChronoUnit.HOURS)))
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();
    }
	
	public String extractId(String token) {
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


	    } catch (JwtException | IllegalArgumentException e) {
	    	System.err.println("Error occured while trying to parse the token");
	    	e.printStackTrace();
	        return false;
	    }
	}
    
	public String generateGuestToken() {
	    return Jwts.builder()
	            .setSubject("0") // guest will get 0 as id
	            .signWith(secretKey, SignatureAlgorithm.HS256)
	            .compact();
	}


}

