package com.taskflow.taskflow_backend.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@Getter
public class ProjectDetailsResponse {
    private UUID id;
    private String name;
    private String description;
    private List<TaskSummaryResponse> tasks;
}