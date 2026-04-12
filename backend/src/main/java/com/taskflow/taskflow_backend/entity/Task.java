package com.taskflow.taskflow_backend.entity;

import com.taskflow.taskflow_backend.enums.TaskPriority;
import com.taskflow.taskflow_backend.enums.TaskStatus;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Entity
@Table(name = "tasks")
public class Task {

    @Id
    @GeneratedValue
    @org.hibernate.annotations.UuidGenerator
    private UUID id;

    @Column(nullable = false)
    private String title;

    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TaskStatus status = TaskStatus.todo;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TaskPriority priority = TaskPriority.medium;

    // 🔥 RELATIONSHIP (NOT UUID)
    @ManyToOne
    @JoinColumn(name = "project_id", nullable = false)
    private Project project;

    // nullable
    @ManyToOne
    @JoinColumn(name = "assignee_id")
    private User assignee;

    private LocalDate dueDate;

    @ManyToOne
    @JoinColumn(name = "created_by", nullable = false)
    private User createdBy;

    @Column(name = "created_at", insertable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", insertable = false, updatable = true)
    private LocalDateTime updatedAt;
}
