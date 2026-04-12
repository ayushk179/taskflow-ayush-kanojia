package com.taskflow.taskflow_backend.dto;


import com.taskflow.taskflow_backend.enums.TaskPriority;
import com.taskflow.taskflow_backend.enums.TaskStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@AllArgsConstructor
@Getter
public class TaskSummaryResponse {

    private UUID id;
    private String title;
    private TaskStatus status;
    private TaskPriority priority;
    private UUID assigneeId;
    private UUID createdBy;
}