package com.ipaas.taskmanagement.form;

import com.ipaas.taskmanagement.entity.TaskStatus;
import lombok.Data;

import java.util.UUID;

/**
 * Form for filtering tasks.
 */
@Data
public class TaskFilterForm {

    private TaskStatus status;
    private UUID userId;
    private String title;
}
