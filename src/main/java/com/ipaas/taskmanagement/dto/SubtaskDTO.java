package com.ipaas.taskmanagement.dto;

import com.ipaas.taskmanagement.entity.TaskStatus;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Data Transfer Object for Subtask.
 */
@Data
public class SubtaskDTO {

    private UUID id;
    private String title;
    private String description;
    private TaskStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime completedAt;
    private UUID taskId;
    private String taskTitle;
}
