package com.sita.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.Field;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.sita.dto.AuthRequest;
import com.sita.model.User;
import com.sita.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

	@Mock
    private UserRepository userRepository;

	@Mock
	private PasswordService passwordService;
	 
    @InjectMocks
    private AuthService authService;
    
    // -------------------------------
    // register success
    // -------------------------------
    @Test
    void testRegisterCreatesUser() {
    	AuthRequest req = new AuthRequest();
        req.setEmail("test@test.com");
        req.setPassword("1234");
        req.setNickname("Ash");

        when(userRepository.findByEmail("test@test.com"))
                .thenReturn(Optional.empty());

        when(passwordService.hashPassword("1234"))
                .thenReturn("HASHED");

        authService.register(req);

        verify(userRepository).save(any());
    }

    // -------------------------------
    // register failed – email already exists
    // -------------------------------
    @Test
    void testRegisterFailsWhenEmailExists() {
    	AuthRequest req = new AuthRequest();
        req.setEmail("test@test.com");
        req.setPassword("1234");
        req.setNickname("Ash");

        User existing = new User();
        existing.setEmail("test@test.com");

        when(userRepository.findByEmail("test@test.com"))
                .thenReturn(Optional.of(existing));

        boolean result = authService.register(req);
        assertFalse(result);
        verify(userRepository, never()).save(any());
  
    }
    
    // -------------------------------
    // login success
    // -------------------------------
    @Test
    void testLoginSuccess() {
    	AuthRequest req = new AuthRequest();
        req.setEmail("test@test.com");
        req.setPassword("1234");
        req.setNickname("Ash");

        User user = new User();
        user.setEmail("test@test.com");
        user.setPasswordHash("HASHED");  // stored password

        Field idField = null;
		try {
			idField = User.class.getDeclaredField("id");
			idField.setAccessible(true);
	        idField.set(user, 10L);
		} catch (Exception e) {
			e.printStackTrace();
		} 
        
        
        when(userRepository.findByEmail("test@test.com"))
                .thenReturn(Optional.of(user));

        when(passwordService.hashPassword("1234"))
                .thenReturn("HASHED"); // matches!

        Long userId = authService.login(req);

        assertNotNull(userId);
        assertTrue(userId > 0);
    }
    
    // -------------------------------
    // login failed – wrong password
    // -------------------------------
    @Test
    void testLoginWrongPassword() {
    	AuthRequest req = new AuthRequest();
        req.setEmail("test@test.com");
        req.setPassword("1234");
        req.setNickname("Ash");

        User user = new User();
        user.setEmail("test@test.com");
        user.setPasswordHash("CORRECT_HASH");

        when(userRepository.findByEmail("test@test.com"))
                .thenReturn(Optional.of(user));

        when(passwordService.hashPassword("1234"))
                .thenReturn("WRONG_HASH");

        assertNull(authService.login(req));

    }
}
