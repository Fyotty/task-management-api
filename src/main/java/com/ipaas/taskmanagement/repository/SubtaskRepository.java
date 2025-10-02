package com.ipaas.taskmanagement.repository;

import com.ipaas.taskmanagement.entity.Subtask;
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
 * Repository for Subtask entity.
 */
@Repository
public interface SubtaskRepository extends JpaRepository<Subtask, UUID>, JpaSpecificationExecutor<Subtask> {

    /**
     * Finds subtasks by task ID.
     */
    List<Subtask> findByTaskId(UUID taskId);

    /**
     * Finds subtasks by task ID with pagination.
     */
    Page<Subtask> findByTaskId(UUID taskId, Pageable pageable);

    /**
     * Counts subtasks by task ID.
     */
    long countByTaskId(UUID taskId);

    /**
     * Counts subtasks by task ID and status.
     */
    long countByTaskIdAndStatus(UUID taskId, TaskStatus status);

    /**
     * Checks if all subtasks of a task are completed.
     */
    @Query("SELECT CASE WHEN COUNT(s) = 0 THEN true ELSE false END " +
           "FROM Subtask s WHERE s.task.id = :taskId AND s.status != 'COMPLETED'")
    boolean areAllSubtasksCompleted(@Param("taskId") UUID taskId);

}
