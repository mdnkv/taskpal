package dev.mednikov.taskpal.statuses.repositories;

import dev.mednikov.taskpal.statuses.models.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StatusRepository extends JpaRepository<Status, Long> {

    List<Status> findAllByWorkspaceId(Long workspaceId);

}
