package dev.mednikov.taskpal.tasks.services;

import dev.mednikov.taskpal.tasks.domain.TaskDto;

import java.util.List;
import java.util.Optional;

public interface TaskService {

    TaskDto createTask (TaskDto taskDto);

    TaskDto updateTask (TaskDto taskDto);

    void deleteTask (Long taskId);

    Optional<TaskDto> getTask (Long taskId);

    List<TaskDto> getTasksInWorkspace (Long workspaceId);

}
