package com.ipaas.taskmanagement.service;

import com.ipaas.taskmanagement.dto.SubtaskDTO;
import com.ipaas.taskmanagement.entity.Subtask;
import com.ipaas.taskmanagement.entity.Task;
import com.ipaas.taskmanagement.entity.TaskStatus;
import com.ipaas.taskmanagement.exception.SubtaskNotFoundException;
import com.ipaas.taskmanagement.form.CreateSubtaskForm;
import com.ipaas.taskmanagement.form.UpdateTaskStatusForm;
import com.ipaas.taskmanagement.mapper.SubtaskMapper;
import com.ipaas.taskmanagement.repository.SubtaskRepository;
import com.ipaas.taskmanagement.specification.SubtaskSpecification;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Service for Subtask operations.
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class SubtaskService {

    private final SubtaskRepository subtaskRepository;
    private final TaskService taskService;
    private final SubtaskMapper subtaskMapper;

    /**
     * Creates a new subtask.
     */
    public SubtaskDTO createSubtask(UUID taskId, CreateSubtaskForm form) {
        log.info("Creating subtask for task ID: {}", taskId);

        Task task = taskService.getTaskEntity(taskId);
        Subtask subtask = subtaskMapper.toEntity(form, task);
        Subtask savedSubtask = subtaskRepository.save(subtask);

        log.info("Subtask created successfully. ID: {}", savedSubtask.getId());
        return subtaskMapper.toDTO(savedSubtask);
    }

    /**
     * Finds a subtask by ID.
     */
    @Transactional(readOnly = true)
    public SubtaskDTO findById(UUID id) {
        log.info("Finding subtask by ID: {}", id);

        Subtask subtask = subtaskRepository.findById(id)
                .orElseThrow(() -> new SubtaskNotFoundException("Subtask not found with ID: " + id));

        return subtaskMapper.toDTO(subtask);
    }

    /**
     * Lists subtasks by task ID.
     */
    @Transactional(readOnly = true)
    public List<SubtaskDTO> findByTaskId(UUID taskId) {
        log.info("Finding subtasks by task ID: {}", taskId);

        // Verify task exists
        taskService.getTaskEntity(taskId);

        List<Subtask> subtasks = subtaskRepository.findByTaskId(taskId);
        return subtasks.stream()
                .map(subtaskMapper::toDTO)
                .collect(Collectors.toList());
    }

    /**
     * Lists subtasks by task ID with pagination.
     */
    @Transactional(readOnly = true)
    public Page<SubtaskDTO> findByTaskIdPaginated(UUID taskId, Pageable pageable) {
        log.info("Finding subtasks by task ID with pagination: {}", taskId);

        // Verify task exists
        taskService.getTaskEntity(taskId);

        Page<Subtask> subtasks = subtaskRepository.findByTaskId(taskId, pageable);
        return subtasks.map(subtaskMapper::toDTO);
    }

    /**
     * Lists subtasks with filters.
     */
    @Transactional(readOnly = true)
    public Page<SubtaskDTO> findSubtasks(UUID taskId, TaskStatus status, String title, Pageable pageable) {
        log.info("Finding subtasks with filters - Task ID: {}, Status: {}, Title: {}", taskId, status, title);

        Specification<Subtask> spec = SubtaskSpecification.withFilters(taskId, status, title);
        Page<Subtask> subtasks = subtaskRepository.findAll(spec, pageable);

        return subtasks.map(subtaskMapper::toDTO);
    }

    /**
     * Updates subtask status.
     */
    public SubtaskDTO updateStatus(UUID id, UpdateTaskStatusForm form) {
        log.info("Updating subtask status. ID: {} to {}", id, form.getStatus());

        Subtask subtask = subtaskRepository.findById(id)
                .orElseThrow(() -> new SubtaskNotFoundException("Subtask not found with ID: " + id));

        if (form.getStatus() == TaskStatus.COMPLETED) {
            subtask.setCompletedAt(LocalDateTime.now());
        } else if (subtask.getStatus() == TaskStatus.COMPLETED && form.getStatus() != TaskStatus.COMPLETED) {
            subtask.setCompletedAt(null);
        }

        subtask.setStatus(form.getStatus());
        Subtask savedSubtask = subtaskRepository.save(subtask);

        log.info("Subtask status updated successfully");
        return subtaskMapper.toDTO(savedSubtask);
    }
}
