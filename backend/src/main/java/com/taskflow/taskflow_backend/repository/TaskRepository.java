package com.taskflow.taskflow_backend.repository;

import com.taskflow.taskflow_backend.entity.Task;
import com.taskflow.taskflow_backend.enums.TaskStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface TaskRepository extends JpaRepository<Task, UUID> {

    List<Task> findByProjectId(UUID projectId);

    boolean existsByProjectIdAndAssigneeId(UUID projectId, UUID assigneeId);

    Page<Task> findByProjectId(UUID projectId, Pageable pageable);

    Page<Task> findByProjectIdAndStatus(UUID projectId, TaskStatus status, Pageable pageable);

    Page<Task> findByProjectIdAndAssigneeId(UUID projectId, UUID assigneeId, Pageable pageable);

    Page<Task> findByProjectIdAndStatusAndAssigneeId(
            UUID projectId,
            TaskStatus status,
            UUID assigneeId,
            Pageable pageable
    );

    boolean existsByProjectIdAndCreatedById (UUID projectId, UUID assigneeId);
}