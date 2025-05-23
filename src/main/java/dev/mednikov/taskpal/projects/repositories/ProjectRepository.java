package dev.mednikov.taskpal.projects.repositories;

import dev.mednikov.taskpal.projects.models.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {

    List<Project> findAllByWorkspaceId(Long workspaceId);

}
