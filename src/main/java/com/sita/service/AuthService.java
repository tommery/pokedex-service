package com.sita.service;

import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.security.MessageDigest;
import java.util.Base64;

import org.springframework.stereotype.Service;

import com.sita.model.UserEntity;
import com.sita.repository.UserRepository;

@Service
public class AuthService {

    private final UserRepository userRepo;

    public AuthService(UserRepository userRepo) {
        this.userRepo = userRepo;
    }

    public void register(String username, String email, String password) {
        String hash = hashPassword(password);
        UserEntity user = new UserEntity(username, email, hash);
        userRepo.save(user);
    }

    public boolean login(String email, String password) {
        String hashed = hashPassword(password);

        return userRepo.findByEmail(email)
                .map(u -> u.getPasswordHash().equals(hashed))
                .orElse(false);
    }


    
    public String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] digest = md.digest(password.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(digest);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
}

   


