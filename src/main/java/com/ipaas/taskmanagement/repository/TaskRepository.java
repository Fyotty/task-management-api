package com.ipaas.taskmanagement.repository;

import com.ipaas.taskmanagement.entity.Task;
import com.ipaas.taskmanagement.entity.TaskStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

/**
 * Repository for Task entity.
 */
@Repository
public interface TaskRepository extends JpaRepository<Task, UUID>, JpaSpecificationExecutor<Task> {

    /**
     * Finds tasks by user ID.
     */
    List<Task> findByUserId(UUID userId);

    /**
     * Finds tasks by status.
     */
    List<Task> findByStatus(TaskStatus status);

    /**
     * Finds tasks by user ID and status.
     */
    List<Task> findByUserIdAndStatus(UUID userId, TaskStatus status);

    /**
     * Finds tasks by user ID with pagination.
     */
    Page<Task> findByUserId(UUID userId, Pageable pageable);

    /**
     * Counts tasks by user ID.
     */
    long countByUserId(UUID userId);

    /**
     * Counts tasks by status.
     */
    long countByStatus(TaskStatus status);

    /**
     * Custom query to find tasks with subtask counts.
     */
    @Query("SELECT t FROM Task t LEFT JOIN FETCH t.subtasks WHERE t.id = :taskId")
    Task findByIdWithSubtasks(@Param("taskId") UUID taskId);
}
