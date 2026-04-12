package com.taskflow.taskflow_backend.service;

import com.taskflow.taskflow_backend.dto.TaskRequest;
import com.taskflow.taskflow_backend.dto.TaskResponse;
import com.taskflow.taskflow_backend.entity.Project;
import com.taskflow.taskflow_backend.entity.Task;
import com.taskflow.taskflow_backend.entity.User;
import com.taskflow.taskflow_backend.enums.TaskPriority;
import com.taskflow.taskflow_backend.enums.TaskStatus;
import com.taskflow.taskflow_backend.exception.BadRequestException;
import com.taskflow.taskflow_backend.exception.ForbiddenException;
import com.taskflow.taskflow_backend.exception.NotFoundException;
import com.taskflow.taskflow_backend.exception.UnauthorizedException;
import com.taskflow.taskflow_backend.repository.ProjectRepository;
import com.taskflow.taskflow_backend.repository.TaskRepository;
import com.taskflow.taskflow_backend.repository.UserRepository;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class TaskService {

    private final TaskRepository taskRepository;
    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;

    // ---------------- CREATE ----------------

    public TaskResponse createTask(UUID projectId, TaskRequest request) {

        User currentUser = getCurrentUser();

        log.info("Creating task | projectId={} | userId={}",
                projectId, currentUser.getId());

        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new NotFoundException("Project not found"));

        boolean isOwner = project.getOwner().getId().equals(currentUser.getId());

        boolean isParticipant = taskRepository.existsByProjectIdAndAssigneeId(projectId, currentUser.getId())
                || taskRepository.existsByProjectIdAndCreatedById(projectId, currentUser.getId());

        if (!isOwner && !isParticipant) {
            log.warn("Access denied for task creation | userId={} | projectId={}",
                    currentUser.getId(), projectId);
            throw new ForbiddenException("Forbidden");
        }

        Task task = new Task();
        task.setTitle(request.getTitle());
        task.setDescription(request.getDescription());
        task.setProject(project);
        task.setCreatedBy(currentUser);

        task.setStatus(
                request.getStatus() != null ? request.getStatus() : TaskStatus.todo
        );

        task.setPriority(
                request.getPriority() != null ? request.getPriority() : TaskPriority.medium
        );


        if (request.getDueDate() != null) {
            task.setDueDate(request.getDueDate());
        }

        // assignee
        if (request.getAssigneeId() != null) {
            User assignee = userRepository.findById(request.getAssigneeId())
                    .orElseThrow(() -> new NotFoundException("Assignee User not found"));
            task.setAssignee(assignee);
        }

        taskRepository.save(task);

        log.info("Task created successfully | taskId={} | projectId={} | createdBy={}",
                task.getId(), projectId, currentUser.getId());

        return mapToResponse(task);
    }

    // ---------------- GET ----------------

    public List<TaskResponse> getTasks(UUID projectId, TaskStatus status, UUID assigneeId ,int page,
                                       int limit) {

        User currentUser = getCurrentUser();

        log.info("Fetching tasks | projectId={} | userId={} | filters: status={}, assigneeId={}",
                projectId, currentUser.getId(), status, assigneeId);

        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new NotFoundException("Project not found"));

        boolean isOwner = project.getOwner().getId().equals(currentUser.getId());
        boolean isParticipant = taskRepository.existsByProjectIdAndAssigneeId(projectId, currentUser.getId())
                || taskRepository.existsByProjectIdAndCreatedById(projectId, currentUser.getId());

        if (!isOwner && !isParticipant) {
            log.warn("Access denied for fetching task | userId={} | projectId={}",
                    currentUser.getId(), projectId);
            throw new ForbiddenException("Forbidden");
        }

        Pageable pageable = PageRequest.of(page, limit);

        Page<Task> tasks;

        if (status != null && assigneeId != null) {
            tasks = taskRepository.findByProjectIdAndStatusAndAssigneeId(projectId, status, assigneeId,pageable);
        } else if (status != null) {
            tasks = taskRepository.findByProjectIdAndStatus(projectId, status,pageable);
        } else if (assigneeId != null) {
            tasks = taskRepository.findByProjectIdAndAssigneeId(projectId, assigneeId,pageable);
        } else {
            tasks = taskRepository.findByProjectId(projectId,pageable);
        }

        return tasks.getContent().stream().map(this::mapToResponse).toList();
    }

    // ---------------- UPDATE ----------------

    public TaskResponse updateTask(UUID taskId, TaskRequest request) {

        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new NotFoundException("Task not found"));

        User currentUser = getCurrentUser();

        log.info("Updating task | taskId={} | userId={}", taskId, currentUser.getId());

        boolean isOwner = task.getProject().getOwner().getId().equals(currentUser.getId());
        boolean isCreator = task.getCreatedBy().getId().equals(currentUser.getId());

        if (!isOwner && !isCreator) {

            log.warn("Access denied for task updation | userId={} | taskID={}",
                    currentUser.getId(),task.getId());
            throw new ForbiddenException("Forbidden");
        }

        if (request.getTitle() != null) task.setTitle(request.getTitle());
        if (request.getDescription() != null) task.setDescription(request.getDescription());
        if (request.getStatus() != null) task.setStatus(request.getStatus());
        if (request.getPriority() != null) task.setPriority(request.getPriority());
        if (request.getDueDate() != null) task.setDueDate(request.getDueDate());

        if (request.getAssigneeId() != null) {
            User assignee = userRepository.findById(request.getAssigneeId())
                    .orElseThrow(() -> new NotFoundException("Assignee User not found"));
            task.setAssignee(assignee);
        }

        task.setUpdatedAt(LocalDateTime.now());

        taskRepository.save(task);

        log.info("Task updated successfully | taskId={}", taskId);

        return mapToResponse(task);
    }

    // ---------------- DELETE ----------------

    public void deleteTask(UUID taskId) {

        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new NotFoundException("Task not found"));

        User currentUser = getCurrentUser();

        log.warn("Deleting task | taskId={} | userId={}",
                taskId, currentUser.getId());

        boolean isOwner = task.getProject().getOwner().getId().equals(currentUser.getId());
        boolean isCreator = task.getCreatedBy().getId().equals(currentUser.getId());

        if (!isOwner && !isCreator) {
            log.warn("Access denied for task deletion | userId={} | taskID={}",
                    currentUser.getId(),task.getId());
            throw new ForbiddenException("Forbidden");
        }

        taskRepository.delete(task);
    }

    // ---------------- HELPERS ----------------

    private User getCurrentUser() {
        String email = (String) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();

        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UnauthorizedException("User not found"));
    }

    private TaskResponse mapToResponse(Task task) {
        return new TaskResponse(
                task.getId(),
                task.getTitle(),
                task.getDescription(),
                task.getStatus(),
                task.getPriority(),
                task.getAssignee() != null ? task.getAssignee().getId() : null,
                task.getCreatedBy().getId(),
                task.getDueDate()
        );
    }
}