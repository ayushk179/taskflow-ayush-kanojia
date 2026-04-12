package com.taskflow.taskflow_backend.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ProjectRequest {
    @NotBlank(message = "is required")
    private String name;
    private String description;
}