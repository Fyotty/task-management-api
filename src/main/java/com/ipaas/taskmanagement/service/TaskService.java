package com.ipaas.taskmanagement.service;

import com.ipaas.taskmanagement.dto.TaskDTO;
import com.ipaas.taskmanagement.entity.Task;
import com.ipaas.taskmanagement.entity.TaskStatus;
import com.ipaas.taskmanagement.entity.User;
import com.ipaas.taskmanagement.exception.BusinessRuleException;
import com.ipaas.taskmanagement.exception.TaskNotFoundException;
import com.ipaas.taskmanagement.form.CreateTaskForm;
import com.ipaas.taskmanagement.form.TaskFilterForm;
import com.ipaas.taskmanagement.form.UpdateTaskStatusForm;
import com.ipaas.taskmanagement.mapper.TaskMapper;
import com.ipaas.taskmanagement.repository.SubtaskRepository;
import com.ipaas.taskmanagement.repository.TaskRepository;
import com.ipaas.taskmanagement.specification.TaskSpecification;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Service for Task operations.
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class TaskService {

    private final TaskRepository taskRepository;
    private final SubtaskRepository subtaskRepository;
    private final UserService userService;
    private final TaskMapper taskMapper;

    /**
     * Creates a new task.
     */
    public TaskDTO createTask(CreateTaskForm form) {
        log.info("Creating task for user ID: {}", form.getUserId());

        User user = userService.getUserEntity(form.getUserId());
        Task task = taskMapper.toEntity(form, user);
        Task savedTask = taskRepository.save(task);

        long totalSubtasks = subtaskRepository.countByTaskId(savedTask.getId());
        long completedSubtasks = subtaskRepository.countByTaskIdAndStatus(savedTask.getId(), TaskStatus.COMPLETED);

        log.info("Task created successfully. ID: {}", savedTask.getId());
        return taskMapper.toDTO(savedTask, totalSubtasks, completedSubtasks);
    }

    /**
     * Finds a task by ID.
     */
    @Transactional(readOnly = true)
    public TaskDTO findById(UUID id) {
        log.info("Finding task by ID: {}", id);

        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException("Task not found with ID: " + id));

        long totalSubtasks = subtaskRepository.countByTaskId(id);
        long completedSubtasks = subtaskRepository.countByTaskIdAndStatus(id, TaskStatus.COMPLETED);

        return taskMapper.toDTO(task, totalSubtasks, completedSubtasks);
    }

    /**
     * Lists tasks with filters and pagination.
     */
    @Transactional(readOnly = true)
    public Page<TaskDTO> findTasks(TaskFilterForm filters, Pageable pageable) {
        log.info("Finding tasks with filters: {}", filters);

        Specification<Task> spec = TaskSpecification.withFilters(filters);
        Page<Task> tasks = taskRepository.findAll(spec, pageable);

        return tasks.map(task -> {
            long totalSubtasks = subtaskRepository.countByTaskId(task.getId());
            long completedSubtasks = subtaskRepository.countByTaskIdAndStatus(task.getId(), TaskStatus.COMPLETED);
            return taskMapper.toDTO(task, totalSubtasks, completedSubtasks);
        });
    }

    /**
     * Updates task status.
     */
    public TaskDTO updateStatus(UUID id, UpdateTaskStatusForm form) {
        log.info("Updating task status. ID: {} to {}", id, form.getStatus());

        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException("Task not found with ID: " + id));

        // Business rule: Cannot complete task if there are pending subtasks
        if (form.getStatus() == TaskStatus.COMPLETED) {
            boolean allSubtasksCompleted = subtaskRepository.areAllSubtasksCompleted(id);
            if (!allSubtasksCompleted) {
                throw new BusinessRuleException("Cannot complete task. There are pending subtasks.");
            }
            task.setCompletedAt(LocalDateTime.now());
        } else if (task.getStatus() == TaskStatus.COMPLETED && form.getStatus() != TaskStatus.COMPLETED) {
            task.setCompletedAt(null);
        }

        task.setStatus(form.getStatus());
        Task savedTask = taskRepository.save(task);

        long totalSubtasks = subtaskRepository.countByTaskId(id);
        long completedSubtasks = subtaskRepository.countByTaskIdAndStatus(id, TaskStatus.COMPLETED);

        log.info("Task status updated successfully");
        return taskMapper.toDTO(savedTask, totalSubtasks, completedSubtasks);
    }

    /**
     * Gets task entity by ID (for internal use).
     */
    @Transactional(readOnly = true)
    public Task getTaskEntity(UUID id) {
        return taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException("Task not found with ID: " + id));
    }
}
