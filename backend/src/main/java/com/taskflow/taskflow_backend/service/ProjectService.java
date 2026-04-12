package com.taskflow.taskflow_backend.service;

import ch.qos.logback.core.util.StringUtil;
import com.taskflow.taskflow_backend.dto.*;
import com.taskflow.taskflow_backend.entity.Project;
import com.taskflow.taskflow_backend.entity.Task;
import com.taskflow.taskflow_backend.entity.User;
import com.taskflow.taskflow_backend.enums.TaskStatus;
import com.taskflow.taskflow_backend.exception.BadRequestException;
import com.taskflow.taskflow_backend.exception.ForbiddenException;
import com.taskflow.taskflow_backend.exception.NotFoundException;
import com.taskflow.taskflow_backend.exception.UnauthorizedException;
import com.taskflow.taskflow_backend.repository.ProjectRepository;
import com.taskflow.taskflow_backend.repository.TaskRepository;
import com.taskflow.taskflow_backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;
    private final TaskRepository taskRepository;

    public ProjectResponse createProject(ProjectRequest request) {

        User user = getCurrentUser();

        log.info("Creating project | userId={} | name={}", user.getId(), request.getName());

        Project project = new Project();
        project.setName(request.getName());
        project.setDescription(request.getDescription());
        project.setOwner(user);

        projectRepository.save(project);

        log.info("Project created successfully | projectId={} | ownerId={}",
                project.getId(), user.getId());

        return mapToResponse(project);
    }

    public List<ProjectResponse> getProjectsForCurrentUser(int page , int limit) {

        User user = getCurrentUser();

        log.info("Fetching projects for userId={}", user.getId());

        // projects owned
//        List<Project> owned = projectRepository.findByOwner(user);

        Pageable pageable = PageRequest.of(page, limit);
        Page<Project> owned = projectRepository.findByOwner(user, pageable);

        // projects where user has tasks
        List<Project> assigned = projectRepository.findProjectsByTaskAssignee(user.getId());

        List<Project> created = projectRepository.findProjectsByTaskCreator(user.getId());

        Set<Project> combined = new HashSet<>();
        combined.addAll(owned.getContent());
        combined.addAll(assigned);
        combined.addAll(created);

        log.info("Projects fetched | userId={} | total={}", user.getId(), combined.size());

        return combined.stream()
                .map(this::mapToResponse)
                .toList();
    }

    public ProjectDetailsResponse getProjectById(UUID id) {

        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Project not found"));

        User user = getCurrentUser();

        log.info("Fetching project details | projectId={} | userId={}", id, user.getId());


        boolean isOwner = project.getOwner().getId().equals(user.getId());
        boolean isParticipant = taskRepository.existsByProjectIdAndAssigneeId(id, user.getId()) || taskRepository.existsByProjectIdAndCreatedById(id, user.getId());

        if (!isOwner && !isParticipant) {
            log.warn("Access denied for project | projectId={} | userId={}", id, user.getId());
            throw new ForbiddenException("Forbidden");
        }

        List<Task> tasks = taskRepository.findByProjectId(id);

        return mapToDetailsResponse(project, tasks);
    }

    public ProjectStatsResponse getProjectStats(UUID projectId) {

        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new NotFoundException("Project not found"));

        User user = getCurrentUser();

        boolean isOwner = project.getOwner().getId().equals(user.getId());
        boolean isParticipant = taskRepository.existsByProjectIdAndAssigneeId(projectId, user.getId())
                || taskRepository.existsByProjectIdAndCreatedById(projectId, user.getId());

        if (!isOwner && !isParticipant) {
            throw new ForbiddenException("Forbidden");
        }

        List<Task> tasks = taskRepository.findByProjectId(projectId);

        Map<TaskStatus, Long> statusCount = tasks.stream()
                .collect(Collectors.groupingBy(Task::getStatus, Collectors.counting()));

        Map<UUID, Long> assigneeCount = tasks.stream()
                .filter(t -> t.getAssignee() != null)
                .collect(Collectors.groupingBy(
                        t -> t.getAssignee().getId(),
                        Collectors.counting()
                ));

        return new ProjectStatsResponse(statusCount, assigneeCount);
    }

    public ProjectResponse updateProject(UUID id, ProjectRequest request) {

        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Project not found"));

        User user = getCurrentUser();

        log.info("Updating project | projectId={} | userId={}", id, user.getId());

        if (!project.getOwner().getId().equals(user.getId())) {
            log.warn("Access denied for project updation | projectId={} | userId={}", id, user.getId());
            throw new ForbiddenException("Forbidden");
        }

        if(request.getName()!=null)
         project.setName(request.getName());

        if(request.getDescription()!=null)
         project.setDescription(request.getDescription());

        projectRepository.save(project);

        log.info("Project updated successfully | projectId={}", id);

        return mapToResponse(project);
    }

    public void deleteProject(UUID id) {

        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Project not found"));

        User user = getCurrentUser();

        log.warn("Deleting project | projectId={} | userId={}", id, user.getId());

        if (!project.getOwner().getId().equals(user.getId())) {
            log.warn("Access denied for deletion | projectId={} | userId={}", id, user.getId());
            throw new ForbiddenException("Forbidden");
        }

        projectRepository.delete(project);

        log.info("Project deleted successfully | projectId={}", id);
    }

    // ----------------- helpers -----------------

    private User getCurrentUser() {
        String email = (String) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();

        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UnauthorizedException("User not found"));
    }

    private ProjectResponse mapToResponse(Project project) {
        return new ProjectResponse(
                project.getId(),
                project.getName(),
                project.getDescription()
        );
    }

    private ProjectDetailsResponse mapToDetailsResponse(Project project, List<Task> tasks) {

        List<TaskSummaryResponse> taskResponses = tasks.stream()
                .map(task -> new TaskSummaryResponse(
                        task.getId(),
                        task.getTitle(),
                        task.getStatus(),
                        task.getPriority(),
                        task.getAssignee() != null ? task.getAssignee().getId() : null,
                        task.getCreatedBy().getId()
                ))
                .toList();

        return new ProjectDetailsResponse(
                project.getId(),
                project.getName(),
                project.getDescription(),
                taskResponses
        );
    }
}