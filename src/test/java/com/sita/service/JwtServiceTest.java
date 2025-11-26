package com.sita.service;

import static org.junit.jupiter.api.Assertions.*;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

public class JwtServiceTest {

    private JwtService jwtService;

    @BeforeEach
    void setup() {
        jwtService = new JwtService();
    }

    // ------------------------------------------------------------
    // generateToken()
    // ------------------------------------------------------------

    @Test
    void testGenerateTokenReturnsNullIfUserIdNull() {
        String token = jwtService.generateToken(null);
        assertNull(token);
    }

    @Test
    void testGenerateTokenCreatesValidJwt() {
        Long userId = 123L;

        String token = jwtService.generateToken(userId);

        assertNotNull(token);
        assertFalse(token.isEmpty());

        // validate it
        boolean valid = jwtService.validate(token);
        assertTrue(valid);

        // check subject
        String extractedId = jwtService.extractId(token);
        assertEquals("123", extractedId);
    }

    // ------------------------------------------------------------
    // extractId()
    // ------------------------------------------------------------

    @Test
    void testExtractId() {
        String token = jwtService.generateToken(77L);

        String id = jwtService.extractId(token);

        assertEquals("77", id);
    }

    // ------------------------------------------------------------
    // validate() – valid token
    // ------------------------------------------------------------

    @Test
    void testValidateValidToken() {
        String token = jwtService.generateToken(55L);

        assertTrue(jwtService.validate(token));
    }

    // ------------------------------------------------------------
    // validate() – invalid token (malformed)
    // ------------------------------------------------------------

    @Test
    void testValidateInvalidTokenMalformed() {
        String invalid = "abc.def.ghi"; // not signed correctly

        assertFalse(jwtService.validate(invalid));
    }

    // ------------------------------------------------------------
    // validate() – expired token
    // ------------------------------------------------------------

    @Test
    void testValidateExpiredToken() throws Exception {
        // Build an already-expired token using the SAME secret key.
        // Access private key through reflection.
        var secretKeyField = JwtService.class.getDeclaredField("secretKey");
        secretKeyField.setAccessible(true);
        var key = secretKeyField.get(jwtService);

        String expiredToken = Jwts.builder()
                .setSubject("100")
                .setIssuedAt(new Date())
                .setExpiration(Date.from(Instant.now().minus(1, ChronoUnit.HOURS)))
                .signWith((java.security.Key) key, SignatureAlgorithm.HS256)
                .compact();

        assertFalse(jwtService.validate(expiredToken));
    }

    // ------------------------------------------------------------
    // validate() – subject missing -> false
    // ------------------------------------------------------------

    @Test
    void testValidateTokenWithoutSubject() throws Exception {
        var secretKeyField = JwtService.class.getDeclaredField("secretKey");
        secretKeyField.setAccessible(true);
        var key = secretKeyField.get(jwtService);

        String token = Jwts.builder()
                .setExpiration(Date.from(Instant.now().plus(1, ChronoUnit.HOURS)))
                .signWith((java.security.Key) key, SignatureAlgorithm.HS256)
                .compact();

        assertFalse(jwtService.validate(token));
    }
}
