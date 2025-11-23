package com.sita.service;

import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.security.MessageDigest;
import java.util.Base64;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import com.sita.dto.AuthRequest;
import com.sita.model.User;
import com.sita.repository.UserRepository;

@Service
public class AuthService {

    private final UserRepository userRepo;

    public AuthService(UserRepository userRepo) {
        this.userRepo = userRepo;
    }

    public boolean register(AuthRequest req) {
    	if (userRepo.findByEmail(req.getEmail()).isPresent()) {
    	    return false;
    	}

        User user = new User();
        user.setEmail(req.getEmail());
        user.setUsername(req.getUsername());
        user.setPasswordHash(hashPassword(req.getPassword()));
        try {
            userRepo.save(user);
        } catch (DataIntegrityViolationException e) {
        	System.err.println("Error occured while trying to save user "+req.getEmail());
        	e.printStackTrace();
            return false;
        }

        return true;
    }
     
    public Long login(AuthRequest req) {
        return userRepo.findByEmail(req.getEmail())
                .filter(u -> u.getPasswordHash().equals(hashPassword(req.getPassword())))
                .map(User::getId)
                .orElse(null);
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

   


