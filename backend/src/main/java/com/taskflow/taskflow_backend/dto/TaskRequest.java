package com.taskflow.taskflow_backend.dto;


import com.taskflow.taskflow_backend.enums.TaskPriority;
import com.taskflow.taskflow_backend.enums.TaskStatus;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.time.LocalDate;
import java.util.UUID;

@Data
public class TaskRequest {

    @NotBlank(message = "is required")
    private String title;

    private String description;

    private TaskStatus status;

    private TaskPriority priority;

    private UUID assigneeId;

    private LocalDate dueDate;
}