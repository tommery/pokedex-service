package com.sita.repository;

import com.sita.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    // Needed for login
    Optional<User> findByEmail(String email);
}

