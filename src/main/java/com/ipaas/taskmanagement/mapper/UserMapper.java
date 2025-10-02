package com.ipaas.taskmanagement.mapper;

import com.ipaas.taskmanagement.dto.UserDTO;
import com.ipaas.taskmanagement.entity.User;
import com.ipaas.taskmanagement.form.CreateUserForm;
import org.springframework.stereotype.Component;

/**
 * Mapper for User entity conversions.
 */
@Component
public class UserMapper {

    /**
     * Converts CreateUserForm to User entity.
     */
    public User toEntity(CreateUserForm form) {
        if (form == null) {
            return null;
        }

        User user = new User();
        user.setName(form.getName());
        user.setEmail(form.getEmail());
        return user;
    }

    /**
     * Converts User entity to UserDTO.
     */
    public UserDTO toDTO(User user) {
        if (user == null) {
            return null;
        }

        UserDTO dto = new UserDTO();
        dto.setId(user.getId());
        dto.setName(user.getName());
        dto.setEmail(user.getEmail());
        dto.setCreatedAt(user.getCreatedAt());
        return dto;
    }
}
