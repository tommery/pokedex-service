package com.sita.repository;

import com.sita.model.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Long> {

    // Needed for login
    Optional<UserEntity> findByEmail(String email);
}

