package com.ipaas.taskmanagement.repository;

import com.ipaas.taskmanagement.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

/**
 * Repository for User entity.
 */
@Repository
public interface UserRepository extends JpaRepository<User, UUID> {

    /**
     * Finds a user by email.
     */
    Optional<User> findByEmail(String email);

    /**
     * Checks if a user exists by email.
     */
    boolean existsByEmail(String email);
}
