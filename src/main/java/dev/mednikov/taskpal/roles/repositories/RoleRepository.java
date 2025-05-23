package dev.mednikov.taskpal.roles.repositories;

import dev.mednikov.taskpal.roles.models.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

    Optional<Role> findByWorkspaceIdAndName(Long workspaceId, String name);

    List<Role> findAllByWorkspaceId(Long workspaceId);

}
