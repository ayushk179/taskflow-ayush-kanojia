package com.taskflow.taskflow_backend.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Entity
@Table(name = "projects")
public class Project {

    @Id
    @GeneratedValue
    private UUID id;

    private String name;

    private String description;

    @ManyToOne
    @JoinColumn(name = "owner_id", nullable = false)
    private User owner;

    @Column(name = "created_at", insertable = false, updatable = false)
    private LocalDateTime createdAt;
}
