package com.ipaas.taskmanagement.dto;

import com.ipaas.taskmanagement.entity.TaskStatus;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Data Transfer Object for Task.
 */
@Data
public class TaskDTO {

    private UUID id;
    private String title;
    private String description;
    private TaskStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime completedAt;
    private UUID userId;
    private String userName;
    private int totalSubtasks;
    private int completedSubtasks;
}
