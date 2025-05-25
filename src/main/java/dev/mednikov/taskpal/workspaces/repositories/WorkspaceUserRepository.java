package dev.mednikov.taskpal.workspaces.repositories;

import dev.mednikov.taskpal.workspaces.models.WorkspaceUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WorkspaceUserRepository extends JpaRepository<WorkspaceUser, Long> {

    Optional<WorkspaceUser> findByWorkspaceIdAndUserId(Long workspaceId, Long userId);

    @Query("SELECT WorkspaceUser wu FROM WorkspaceUser WHERE wu.user.id = :userId AND wu.active = true")
    Optional<WorkspaceUser> findActive(Long userId);

    List<WorkspaceUser> findAllByUserId(Long userId);

}
