package dev.mednikov.taskpal.priorities.repositories;

import dev.mednikov.taskpal.priorities.models.Priority;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PriorityRepository extends JpaRepository<Priority, Long> {

    List<Priority> findAllByWorkspaceId(Long workspaceId);

}
