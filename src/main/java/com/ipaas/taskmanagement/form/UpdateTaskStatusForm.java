package com.ipaas.taskmanagement.form;

import com.ipaas.taskmanagement.entity.TaskStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * Form for updating task or subtask status.
 */
@Data
public class UpdateTaskStatusForm {

    @NotNull(message = "Status is required")
    private TaskStatus status;
}
