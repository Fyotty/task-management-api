package com.ipaas.taskmanagement.controller;

import com.ipaas.taskmanagement.dto.SubtaskDTO;
import com.ipaas.taskmanagement.entity.TaskStatus;
import com.ipaas.taskmanagement.form.CreateSubtaskForm;
import com.ipaas.taskmanagement.form.UpdateTaskStatusForm;
import com.ipaas.taskmanagement.service.SubtaskService;
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

import java.util.List;
import java.util.UUID;

/**
 * REST Controller for Subtask operations.
 */
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Tag(name = "Subtasks", description = "Operations related to subtask management")
public class SubtaskController {

    private final SubtaskService subtaskService;

    @Operation(summary = "List subtasks", description = "Lists subtasks of a task")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Subtasks returned successfully"),
        @ApiResponse(responseCode = "404", description = "Task not found")
    })
    @GetMapping("/tasks/{taskId}/subtasks")
    public ResponseEntity<List<SubtaskDTO>> listSubtasks(@PathVariable UUID taskId) {
        List<SubtaskDTO> subtasks = subtaskService.findByTaskId(taskId);
        return ResponseEntity.ok(subtasks);
    }

    @Operation(summary = "Create subtask", description = "Creates a new subtask for a task")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Subtask created successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid data"),
        @ApiResponse(responseCode = "404", description = "Task not found")
    })
    @PostMapping("/tasks/{taskId}/subtasks")
    public ResponseEntity<SubtaskDTO> createSubtask(
            @PathVariable UUID taskId,
            @Valid @RequestBody CreateSubtaskForm form) {
        SubtaskDTO subtask = subtaskService.createSubtask(taskId, form);
        return ResponseEntity.status(HttpStatus.CREATED).body(subtask);
    }

    @Operation(summary = "List subtasks with pagination", description = "Lists subtasks of a task with pagination")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Subtasks page returned successfully"),
        @ApiResponse(responseCode = "404", description = "Task not found")
    })
    @GetMapping("/tasks/{taskId}/subtasks/paginated")
    public ResponseEntity<Page<SubtaskDTO>> listSubtasksPaginated(
            @PathVariable UUID taskId,
            @Parameter(description = "Pagination information") @PageableDefault(size = 20, sort = "createdAt") Pageable pageable) {
        Page<SubtaskDTO> subtasks = subtaskService.findByTaskIdPaginated(taskId, pageable);
        return ResponseEntity.ok(subtasks);
    }

    @Operation(summary = "Find subtask by ID", description = "Finds a specific subtask by their ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Subtask found"),
        @ApiResponse(responseCode = "404", description = "Subtask not found")
    })
    @GetMapping("/subtasks/{id}")
    public ResponseEntity<SubtaskDTO> findSubtaskById(@PathVariable UUID id) {
        SubtaskDTO subtask = subtaskService.findById(id);
        return ResponseEntity.ok(subtask);
    }

    @Operation(summary = "Update subtask status", description = "Updates the status of a subtask")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Subtask status updated successfully"),
        @ApiResponse(responseCode = "404", description = "Subtask not found")
    })
    @PatchMapping("/subtasks/{id}/status")
    public ResponseEntity<SubtaskDTO> updateSubtaskStatus(
            @PathVariable UUID id,
            @Valid @RequestBody UpdateTaskStatusForm form) {
        SubtaskDTO subtask = subtaskService.updateStatus(id, form);
        return ResponseEntity.ok(subtask);
    }

    @Operation(summary = "Search subtasks", description = "Search subtasks with filters and pagination")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Subtasks page returned successfully")
    })
    @GetMapping("/subtasks/search")
    public ResponseEntity<Page<SubtaskDTO>> searchSubtasks(
            @Parameter(description = "Filter by task ID") @RequestParam(required = false) UUID taskId,
            @Parameter(description = "Filter by status") @RequestParam(required = false) TaskStatus status,
            @Parameter(description = "Filter by title") @RequestParam(required = false) String title,
            @Parameter(description = "Pagination information") @PageableDefault(size = 20, sort = "createdAt") Pageable pageable) {
        
        Page<SubtaskDTO> subtasks = subtaskService.findSubtasks(taskId, status, title, pageable);
        return ResponseEntity.ok(subtasks);
    }
}
