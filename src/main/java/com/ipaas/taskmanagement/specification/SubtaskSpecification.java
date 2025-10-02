package com.ipaas.taskmanagement.specification;

import com.ipaas.taskmanagement.entity.Subtask;
import com.ipaas.taskmanagement.entity.TaskStatus;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Specifications for Subtask entity filtering.
 */
public class SubtaskSpecification {

    /**
     * Filter by task ID.
     */
    public static Specification<Subtask> belongsToTask(UUID taskId) {
        return (root, query, criteriaBuilder) -> 
            taskId == null ? null : criteriaBuilder.equal(root.get("task").get("id"), taskId);
    }

    /**
     * Filter by status.
     */
    public static Specification<Subtask> hasStatus(TaskStatus status) {
        return (root, query, criteriaBuilder) -> 
            status == null ? null : criteriaBuilder.equal(root.get("status"), status);
    }

    /**
     * Filter by title containing text (case insensitive).
     */
    public static Specification<Subtask> titleContains(String title) {
        return (root, query, criteriaBuilder) -> {
            if (title == null || title.trim().isEmpty()) {
                return null;
            }
            return criteriaBuilder.like(
                criteriaBuilder.lower(root.get("title")),
                "%" + title.toLowerCase() + "%"
            );
        };
    }

    /**
     * Combines multiple filters.
     */
    public static Specification<Subtask> withFilters(UUID taskId, TaskStatus status, String title) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (taskId != null) {
                predicates.add(criteriaBuilder.equal(root.get("task").get("id"), taskId));
            }

            if (status != null) {
                predicates.add(criteriaBuilder.equal(root.get("status"), status));
            }

            if (title != null && !title.trim().isEmpty()) {
                predicates.add(criteriaBuilder.like(
                    criteriaBuilder.lower(root.get("title")),
                    "%" + title.toLowerCase() + "%"
                ));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
