package com.sita.service;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import com.sita.dto.AuthRequest;
import com.sita.logging.AppLogger;
import com.sita.logging.Log;
import com.sita.model.User;
import com.sita.repository.UserRepository;

@Service
public class AuthService {

	private static final AppLogger log = Log.get(AuthService.class);
    private final UserRepository userRepo;
    private final PasswordService passwordService;
    public AuthService(UserRepository userRepo,
    		PasswordService passwordService) {
        this.userRepo = userRepo;
        this.passwordService = passwordService;
    }

    public boolean register(AuthRequest req) {
    	if (userRepo.findByEmail(req.getEmail()).isPresent()) {
    	    return false;
    	}

        User user = new User();
        user.setEmail(req.getEmail());
        user.setNickname(req.getNickname());
        user.setPasswordHash(passwordService.hashPassword(req.getPassword()));
        try {
            userRepo.save(user);
        } catch (DataIntegrityViolationException e) {
        	log.error("Failed to add to save user {}",req.getEmail());
            return false;
        }

        return true;
    }
     
    public Long login(AuthRequest req) {
        return userRepo.findByEmail(req.getEmail())
                .filter(u -> u.getPasswordHash().equals(passwordService.hashPassword(req.getPassword())))
                .map(User::getId)
                .orElse(null);
    }
    
   
}

   


