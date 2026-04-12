package com.taskflow.taskflow_backend.repository;

import com.taskflow.taskflow_backend.entity.Project;
import com.taskflow.taskflow_backend.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ProjectRepository extends JpaRepository<Project,UUID> {

    Page<Project> findByOwner(User owner, Pageable pageable);

    @Query("""
    SELECT DISTINCT p FROM Project p
    JOIN Task t ON t.project.id = p.id
    WHERE t.assignee.id = :userId
""")
    List<Project> findProjectsByTaskAssignee(UUID userId);

    @Query("""
    SELECT DISTINCT p FROM Project p
    JOIN Task t ON t.project.id = p.id
    WHERE t.createdBy.id = :userId
""")
    List<Project> findProjectsByTaskCreator(UUID userId);

}
