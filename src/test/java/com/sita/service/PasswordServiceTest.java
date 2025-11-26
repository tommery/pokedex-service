package com.sita.service;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class PasswordServiceTest {

    private PasswordService passwordService;

    @BeforeEach
    void setup() {
        passwordService = new PasswordService();
    }

    // ------------------------------------------------------------
    // Basic hashing tests
    // ------------------------------------------------------------

    @Test
    void testHashPasswordReturnsNonNull() {
        String hash = passwordService.hashPassword("abc123");
        assertNotNull(hash);
        assertFalse(hash.isEmpty());
    }

    @Test
    void testHashPasswordIsDeterministic() {
        String h1 = passwordService.hashPassword("mypassword");
        String h2 = passwordService.hashPassword("mypassword");

        assertEquals(h1, h2);
    }

    @Test
    void testHashPasswordDifferentPasswordsProduceDifferentHashes() {
        String h1 = passwordService.hashPassword("password1");
        String h2 = passwordService.hashPassword("password2");

        assertNotEquals(h1, h2);
    }

    @Test
    void testKnownHashValue() {
 
        String expected = "LPJNul+wow4m6DsqxbninhsWHlwfp0JecwQzYpOLmCQ=";

        String hash = passwordService.hashPassword("hello");

        assertEquals(expected, hash);
    }


    // ------------------------------------------------------------
    // Optional: simulate NoSuchAlgorithmException
    // ------------------------------------------------------------
    // This requires temporarily mocking MessageDigest
    // Only needed if you want 100% branch coverage.
}
