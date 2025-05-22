package dev.mednikov.taskpal.workspaces.repositories;

import dev.mednikov.taskpal.workspaces.models.Workspace;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WorkspaceRepository extends JpaRepository<Workspace, Long> {
}
