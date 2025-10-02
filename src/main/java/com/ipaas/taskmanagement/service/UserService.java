package com.ipaas.taskmanagement.service;

import com.ipaas.taskmanagement.dto.UserDTO;
import com.ipaas.taskmanagement.entity.User;
import com.ipaas.taskmanagement.exception.EmailAlreadyExistsException;
import com.ipaas.taskmanagement.exception.UserNotFoundException;
import com.ipaas.taskmanagement.form.CreateUserForm;
import com.ipaas.taskmanagement.mapper.UserMapper;
import com.ipaas.taskmanagement.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

/**
 * Service for User operations.
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    /**
     * Creates a new user.
     */
    public UserDTO createUser(CreateUserForm form) {
        log.info("Creating user with email: {}", form.getEmail());

        if (userRepository.existsByEmail(form.getEmail())) {
            throw new EmailAlreadyExistsException("Email already in use: " + form.getEmail());
        }

        User user = userMapper.toEntity(form);
        User savedUser = userRepository.save(user);

        log.info("User created successfully. ID: {}", savedUser.getId());
        return userMapper.toDTO(savedUser);
    }

    /**
     * Finds a user by ID.
     */
    @Transactional(readOnly = true)
    public UserDTO findById(UUID id) {
        log.info("Finding user by ID: {}", id);

        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found with ID: " + id));

        return userMapper.toDTO(user);
    }

    /**
     * Finds a user by email.
     */
    @Transactional(readOnly = true)
    public UserDTO findByEmail(String email) {
        log.info("Finding user by email: {}", email);

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found with email: " + email));

        return userMapper.toDTO(user);
    }

    /**
     * Gets user entity by ID (for internal use).
     */
    @Transactional(readOnly = true)
    public User getUserEntity(UUID id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found with ID: " + id));
    }
}
