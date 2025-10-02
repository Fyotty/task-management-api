package com.ipaas.taskmanagement.service;

import com.ipaas.taskmanagement.dto.TaskDTO;
import com.ipaas.taskmanagement.entity.Task;
import com.ipaas.taskmanagement.entity.TaskStatus;
import com.ipaas.taskmanagement.entity.User;
import com.ipaas.taskmanagement.exception.BusinessRuleException;
import com.ipaas.taskmanagement.exception.TaskNotFoundException;
import com.ipaas.taskmanagement.form.CreateTaskForm;
import com.ipaas.taskmanagement.form.UpdateTaskStatusForm;
import com.ipaas.taskmanagement.mapper.TaskMapper;
import com.ipaas.taskmanagement.repository.SubtaskRepository;
import com.ipaas.taskmanagement.repository.TaskRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Tests for TaskService")
class TaskServiceTest {

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private SubtaskRepository subtaskRepository;

    @Mock
    private UserService userService;

    @Mock
    private TaskMapper taskMapper;

    @InjectMocks
    private TaskService taskService;

    private User user;
    private Task task;
    private TaskDTO taskDTO;
    private CreateTaskForm createTaskForm;
    private UUID userId;
    private UUID taskId;

    @BeforeEach
    void setUp() {
        userId = UUID.randomUUID();
        taskId = UUID.randomUUID();

        user = new User();
        user.setId(userId);
        user.setName("John Doe");

        task = new Task();
        task.setId(taskId);
        task.setTitle("Test Task");
        task.setUser(user);
        task.setStatus(TaskStatus.PENDING);

        taskDTO = new TaskDTO();
        taskDTO.setId(taskId);
        taskDTO.setTitle("Test Task");
        taskDTO.setUserId(userId);

        createTaskForm = new CreateTaskForm();
        createTaskForm.setTitle("Test Task");
        createTaskForm.setUserId(userId);
    }

    @Test
    @DisplayName("Should create task successfully")
    void shouldCreateTaskSuccessfully() {
        when(userService.getUserEntity(userId)).thenReturn(user);
        when(taskMapper.toEntity(any(CreateTaskForm.class), any(User.class))).thenReturn(task);
        when(taskRepository.save(any(Task.class))).thenReturn(task);
        when(taskMapper.toDTO(any(Task.class), anyLong(), anyLong())).thenReturn(taskDTO);

        TaskDTO result = taskService.createTask(createTaskForm);

        assertThat(result).isNotNull();
        assertThat(result.getTitle()).isEqualTo("Test Task");
        verify(taskRepository).save(task);
    }

    @Test
    @DisplayName("Should find task by ID successfully")
    void shouldFindTaskByIdSuccessfully() {
        when(taskRepository.findById(taskId)).thenReturn(Optional.of(task));
        when(taskMapper.toDTO(any(Task.class), anyLong(), anyLong())).thenReturn(taskDTO);

        TaskDTO result = taskService.findById(taskId);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(taskId);
    }

    @Test
    @DisplayName("Should throw exception when task not found by ID")
    void shouldThrowExceptionWhenTaskNotFoundById() {
        when(taskRepository.findById(taskId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> taskService.findById(taskId))
                .isInstanceOf(TaskNotFoundException.class)
                .hasMessageContaining("Task not found with ID: " + taskId);
    }

    @Test
    @DisplayName("Should update task status successfully")
    void shouldUpdateTaskStatusSuccessfully() {
        UpdateTaskStatusForm form = new UpdateTaskStatusForm();
        form.setStatus(TaskStatus.IN_PROGRESS);

        when(taskRepository.findById(taskId)).thenReturn(Optional.of(task));
        when(taskRepository.save(any(Task.class))).thenReturn(task);
        when(taskMapper.toDTO(any(Task.class), anyLong(), anyLong())).thenReturn(taskDTO);

        TaskDTO result = taskService.updateStatus(taskId, form);

        assertThat(result).isNotNull();
        verify(taskRepository).save(task);
    }

    @Test
    @DisplayName("Should throw exception when completing task with pending subtasks")
    void shouldThrowExceptionWhenCompletingTaskWithPendingSubtasks() {
        UpdateTaskStatusForm form = new UpdateTaskStatusForm();
        form.setStatus(TaskStatus.COMPLETED);

        when(taskRepository.findById(taskId)).thenReturn(Optional.of(task));
        when(subtaskRepository.areAllSubtasksCompleted(taskId)).thenReturn(false);

        assertThatThrownBy(() -> taskService.updateStatus(taskId, form))
                .isInstanceOf(BusinessRuleException.class)
                .hasMessageContaining("Cannot complete task. There are pending subtasks.");

        verify(taskRepository, never()).save(any(Task.class));
    }
}
