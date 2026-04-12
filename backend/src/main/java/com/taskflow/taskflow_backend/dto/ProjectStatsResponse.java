package com.taskflow.taskflow_backend.dto;

import com.taskflow.taskflow_backend.enums.TaskStatus;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Map;
import java.util.UUID;

@Data
@AllArgsConstructor
public class ProjectStatsResponse {

    private Map<TaskStatus, Long> statusCount;
    private Map<UUID, Long> assigneeCount;
}