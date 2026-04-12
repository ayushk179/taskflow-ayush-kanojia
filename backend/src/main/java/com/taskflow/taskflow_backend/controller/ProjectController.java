package com.taskflow.taskflow_backend.controller;

import com.taskflow.taskflow_backend.dto.ProjectDetailsResponse;
import com.taskflow.taskflow_backend.dto.ProjectRequest;
import com.taskflow.taskflow_backend.dto.ProjectResponse;
import com.taskflow.taskflow_backend.dto.ProjectStatsResponse;
import com.taskflow.taskflow_backend.service.ProjectService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/projects")
@RequiredArgsConstructor
@Validated
public class ProjectController {

    private final ProjectService projectService;

    @PostMapping
    public ResponseEntity<ProjectResponse> createProject(
            @RequestBody @Valid ProjectRequest request) {
        return ResponseEntity.ok(projectService.createProject(request));
    }

    @GetMapping
    public ResponseEntity<List<ProjectResponse>> getProjects(@RequestParam(defaultValue = "0") int page,
                                                             @RequestParam(defaultValue = "10") int limit) {
        return ResponseEntity.ok(projectService.getProjectsForCurrentUser(page,limit));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProjectDetailsResponse> getProject(@PathVariable UUID id) {
        System.out.println("lol");
        return ResponseEntity.ok(projectService.getProjectById(id));
    }

    @GetMapping("/{id}/stats")
    public ProjectStatsResponse getStats(@PathVariable UUID id) {
        return projectService.getProjectStats(id);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ProjectResponse> updateProject(
            @PathVariable UUID id,
            @RequestBody ProjectRequest request) {
        return ResponseEntity.ok(projectService.updateProject(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProject(@PathVariable UUID id) {
        projectService.deleteProject(id);
        return ResponseEntity.noContent().build();
    }
}