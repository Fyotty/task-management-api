package com.ipaas.taskmanagement.service;

import com.ipaas.taskmanagement.dto.UserDTO;
import com.ipaas.taskmanagement.entity.User;
import com.ipaas.taskmanagement.exception.EmailAlreadyExistsException;
import com.ipaas.taskmanagement.exception.UserNotFoundException;
import com.ipaas.taskmanagement.form.CreateUserForm;
import com.ipaas.taskmanagement.mapper.UserMapper;
import com.ipaas.taskmanagement.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

/**
 * Unit tests for UserService.
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("Tests for UserService")
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserService userService;

    private User user;
    private UserDTO userDTO;
    private CreateUserForm createUserForm;
    private UUID userId;

    @BeforeEach
    void setUp() {
        userId = UUID.randomUUID();
        
        user = new User();
        user.setId(userId);
        user.setName("John Doe");
        user.setEmail("john.doe@email.com");
        user.setCreatedAt(LocalDateTime.now());

        userDTO = new UserDTO();
        userDTO.setId(userId);
        userDTO.setName("John Doe");
        userDTO.setEmail("john.doe@email.com");

        createUserForm = new CreateUserForm();
        createUserForm.setName("John Doe");
        createUserForm.setEmail("john.doe@email.com");
    }

    @Test
    @DisplayName("Should create user successfully")
    void shouldCreateUserSuccessfully() {
        // Given
        when(userRepository.existsByEmail(anyString())).thenReturn(false);
        when(userMapper.toEntity(any(CreateUserForm.class))).thenReturn(user);
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(userMapper.toDTO(any(User.class))).thenReturn(userDTO);

        // When
        UserDTO result = userService.createUser(createUserForm);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo("John Doe");
        assertThat(result.getEmail()).isEqualTo("john.doe@email.com");
        assertThat(result.getId()).isEqualTo(userId);

        verify(userRepository).existsByEmail("john.doe@email.com");
        verify(userMapper).toEntity(createUserForm);
        verify(userRepository).save(user);
        verify(userMapper).toDTO(user);
    }

    @Test
    @DisplayName("Should throw exception when creating user with existing email")
    void shouldThrowExceptionWhenCreatingUserWithExistingEmail() {
        // Given
        when(userRepository.existsByEmail(anyString())).thenReturn(true);

        // When & Then
        assertThatThrownBy(() -> userService.createUser(createUserForm))
                .isInstanceOf(EmailAlreadyExistsException.class)
                .hasMessageContaining("Email already in use: john.doe@email.com");

        verify(userRepository).existsByEmail("john.doe@email.com");
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    @DisplayName("Should find user by ID successfully")
    void shouldFindUserByIdSuccessfully() {
        // Given
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(userMapper.toDTO(any(User.class))).thenReturn(userDTO);

        // When
        UserDTO result = userService.findById(userId);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(userId);

        verify(userRepository).findById(userId);
        verify(userMapper).toDTO(user);
    }

    @Test
    @DisplayName("Should throw exception when user not found by ID")
    void shouldThrowExceptionWhenUserNotFoundById() {
        // Given
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> userService.findById(userId))
                .isInstanceOf(UserNotFoundException.class)
                .hasMessageContaining("User not found with ID: " + userId);

        verify(userRepository).findById(userId);
    }
}
