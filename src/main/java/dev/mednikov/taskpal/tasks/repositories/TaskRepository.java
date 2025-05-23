package dev.mednikov.taskpal.tasks.repositories;

import dev.mednikov.taskpal.tasks.models.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

    List<Task> findAllByProjectId(Long projectId);

    List<Task> findAllByWorkspaceId(Long workspaceId);

}
