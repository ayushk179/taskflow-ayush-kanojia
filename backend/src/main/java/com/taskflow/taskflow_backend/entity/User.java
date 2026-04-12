package com.taskflow.taskflow_backend.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "users")
@Data
public class User {

    @Id
    @GeneratedValue
    private UUID id;

    private String name;

    @Column(unique = true)
    private String email;

    private String password;

    @Column(name = "created_at", insertable = false, updatable = false)
    private LocalDateTime createdAt;
}