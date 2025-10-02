package com.ipaas.taskmanagement.controller;

import com.ipaas.taskmanagement.dto.TaskDTO;
import com.ipaas.taskmanagement.entity.TaskStatus;
import com.ipaas.taskmanagement.form.CreateTaskForm;
import com.ipaas.taskmanagement.form.TaskFilterForm;
import com.ipaas.taskmanagement.form.UpdateTaskStatusForm;
import com.ipaas.taskmanagement.service.TaskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

/**
 * REST Controller for Task operations.
 */
@RestController
@RequestMapping("/api/tasks")
@RequiredArgsConstructor
@Tag(name = "Tasks", description = "Operations related to task management")
public class TaskController {

    private final TaskService taskService;

    @Operation(summary = "List tasks", description = "Lists tasks with optional filters and pagination")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Tasks page returned successfully")
    })
    @GetMapping
    public ResponseEntity<Page<TaskDTO>> listTasks(
            @Parameter(description = "Filter by task status") @RequestParam(required = false) TaskStatus status,
            @Parameter(description = "Filter by user ID") @RequestParam(required = false) UUID userId,
            @Parameter(description = "Filter by title") @RequestParam(required = false) String title,
            @Parameter(description = "Pagination information") @PageableDefault(size = 20, sort = "createdAt") Pageable pageable) {
        
        TaskFilterForm filters = new TaskFilterForm();
        filters.setStatus(status);
        filters.setUserId(userId);
        filters.setTitle(title);
        
        Page<TaskDTO> tasks = taskService.findTasks(filters, pageable);
        return ResponseEntity.ok(tasks);
    }

    @Operation(summary = "Create task", description = "Creates a new task for a user")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Task created successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid data"),
        @ApiResponse(responseCode = "404", description = "User not found")
    })
    @PostMapping
    public ResponseEntity<TaskDTO> createTask(@Valid @RequestBody CreateTaskForm form) {
        TaskDTO task = taskService.createTask(form);
        return ResponseEntity.status(HttpStatus.CREATED).body(task);
    }

    @Operation(summary = "Find task by ID", description = "Finds a specific task by their ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Task found"),
        @ApiResponse(responseCode = "404", description = "Task not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<TaskDTO> findTaskById(@PathVariable UUID id) {
        TaskDTO task = taskService.findById(id);
        return ResponseEntity.ok(task);
    }

    @Operation(summary = "Update task status", description = "Updates the status of a task")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Task status updated successfully"),
        @ApiResponse(responseCode = "400", description = "Business rule violation"),
        @ApiResponse(responseCode = "404", description = "Task not found")
    })
    @PatchMapping("/{id}/status")
    public ResponseEntity<TaskDTO> updateTaskStatus(
            @PathVariable UUID id,
            @Valid @RequestBody UpdateTaskStatusForm form) {
        TaskDTO task = taskService.updateStatus(id, form);
        return ResponseEntity.ok(task);
    }
}
