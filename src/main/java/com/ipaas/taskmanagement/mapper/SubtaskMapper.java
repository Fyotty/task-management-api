package com.ipaas.taskmanagement.mapper;

import com.ipaas.taskmanagement.dto.SubtaskDTO;
import com.ipaas.taskmanagement.entity.Subtask;
import com.ipaas.taskmanagement.entity.Task;
import com.ipaas.taskmanagement.form.CreateSubtaskForm;
import org.springframework.stereotype.Component;

/**
 * Mapper for Subtask entity conversions.
 */
@Component
public class SubtaskMapper {

    /**
     * Converts CreateSubtaskForm to Subtask entity.
     */
    public Subtask toEntity(CreateSubtaskForm form, Task task) {
        if (form == null) {
            return null;
        }

        Subtask subtask = new Subtask();
        subtask.setTitle(form.getTitle());
        subtask.setDescription(form.getDescription());
        subtask.setTask(task);
        return subtask;
    }

    /**
     * Converts Subtask entity to SubtaskDTO.
     */
    public SubtaskDTO toDTO(Subtask subtask) {
        if (subtask == null) {
            return null;
        }

        SubtaskDTO dto = new SubtaskDTO();
        dto.setId(subtask.getId());
        dto.setTitle(subtask.getTitle());
        dto.setDescription(subtask.getDescription());
        dto.setStatus(subtask.getStatus());
        dto.setCreatedAt(subtask.getCreatedAt());
        dto.setCompletedAt(subtask.getCompletedAt());
        dto.setTaskId(subtask.getTask().getId());
        dto.setTaskTitle(subtask.getTask().getTitle());
        return dto;
    }
}
