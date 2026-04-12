package com.taskflow.taskflow_backend.controller;

import com.taskflow.taskflow_backend.dto.TaskRequest;
import com.taskflow.taskflow_backend.dto.TaskResponse;
import com.taskflow.taskflow_backend.enums.TaskStatus;
import com.taskflow.taskflow_backend.service.TaskService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;

    @GetMapping("/projects/{id}/tasks")
    public ResponseEntity<List<TaskResponse>> getTasks(
            @PathVariable UUID id,
            @RequestParam(required = false) TaskStatus status,
            @RequestParam(required = false) UUID assignee,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int limit
    ) {
        return ResponseEntity.ok(taskService.getTasks(id, status, assignee,page,limit));
    }

    @PostMapping("/projects/{id}/tasks")
    public ResponseEntity<TaskResponse> createTask(
            @PathVariable UUID id,
            @Valid @RequestBody TaskRequest request
    ) {
        return ResponseEntity.ok(taskService.createTask(id, request));
    }

    @PatchMapping("/tasks/{id}")
    public ResponseEntity<TaskResponse> updateTask(
            @PathVariable UUID id,
            @RequestBody TaskRequest request
    ) {
        return ResponseEntity.ok(taskService.updateTask(id, request));
    }

    @DeleteMapping("/tasks/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable UUID id) {
        taskService.deleteTask(id);
        return ResponseEntity.noContent().build();
    }
}