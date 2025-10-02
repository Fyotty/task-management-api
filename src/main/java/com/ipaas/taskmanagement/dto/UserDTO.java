package com.ipaas.taskmanagement.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Data Transfer Object for User.
 */
@Data
public class UserDTO {

    private UUID id;
    private String name;
    private String email;
    private LocalDateTime createdAt;
}
