package com.ipaas.taskmanagement.specification;

import com.ipaas.taskmanagement.entity.Task;
import com.ipaas.taskmanagement.entity.TaskStatus;
import com.ipaas.taskmanagement.form.TaskFilterForm;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Specifications for Task entity filtering.
 */
public class TaskSpecification {

    /**
     * Creates a specification based on the filter form.
     */
    public static Specification<Task> withFilters(TaskFilterForm filters) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (filters.getStatus() != null) {
                predicates.add(criteriaBuilder.equal(root.get("status"), filters.getStatus()));
            }

            if (filters.getUserId() != null) {
                predicates.add(criteriaBuilder.equal(root.get("user").get("id"), filters.getUserId()));
            }

            if (filters.getTitle() != null && !filters.getTitle().trim().isEmpty()) {
                predicates.add(criteriaBuilder.like(
                    criteriaBuilder.lower(root.get("title")),
                    "%" + filters.getTitle().toLowerCase() + "%"
                ));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }

    /**
     * Filter by status.
     */
    public static Specification<Task> hasStatus(TaskStatus status) {
        return (root, query, criteriaBuilder) -> 
            status == null ? null : criteriaBuilder.equal(root.get("status"), status);
    }

    /**
     * Filter by user ID.
     */
    public static Specification<Task> belongsToUser(UUID userId) {
        return (root, query, criteriaBuilder) -> 
            userId == null ? null : criteriaBuilder.equal(root.get("user").get("id"), userId);
    }

    /**
     * Filter by title containing text (case insensitive).
     */
    public static Specification<Task> titleContains(String title) {
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
}
