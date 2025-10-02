package com.ipaas.taskmanagement.mapper;

import com.ipaas.taskmanagement.dto.TaskDTO;
import com.ipaas.taskmanagement.entity.Task;
import com.ipaas.taskmanagement.entity.User;
import com.ipaas.taskmanagement.form.CreateTaskForm;
import org.springframework.stereotype.Component;

/**
 * Mapper for Task entity conversions.
 */
@Component
public class TaskMapper {

    /**
     * Converts CreateTaskForm to Task entity.
     */
    public Task toEntity(CreateTaskForm form, User user) {
        if (form == null) {
            return null;
        }

        Task task = new Task();
        task.setTitle(form.getTitle());
        task.setDescription(form.getDescription());
        task.setUser(user);
        return task;
    }

    /**
     * Converts Task entity to TaskDTO.
     */
    public TaskDTO toDTO(Task task, long totalSubtasks, long completedSubtasks) {
        if (task == null) {
            return null;
        }

        TaskDTO dto = new TaskDTO();
        dto.setId(task.getId());
        dto.setTitle(task.getTitle());
        dto.setDescription(task.getDescription());
        dto.setStatus(task.getStatus());
        dto.setCreatedAt(task.getCreatedAt());
        dto.setCompletedAt(task.getCompletedAt());
        dto.setUserId(task.getUser().getId());
        dto.setUserName(task.getUser().getName());
        dto.setTotalSubtasks((int) totalSubtasks);
        dto.setCompletedSubtasks((int) completedSubtasks);
        return dto;
    }
}
