package dev.mednikov.taskpal.tasks.domain;

import dev.mednikov.taskpal.projects.domain.ProjectDto;
import dev.mednikov.taskpal.projects.domain.ProjectDtoMapper;
import dev.mednikov.taskpal.tasks.models.Task;

import java.util.function.Function;

public final class TaskDtoMapper implements Function<Task, TaskDto> {

    private final static ProjectDtoMapper projectMapper = new ProjectDtoMapper();

    @Override
    public TaskDto apply(Task task) {
        ProjectDto projectDto = projectMapper.apply(task.getProject());
        TaskDto result = new TaskDto();
        result.setId(task.getId().toString());
        result.setWorkspaceId(task.getWorkspace().getId().toString());
        result.setProject(projectDto);
        result.setProjectId(projectDto.getId());
        result.setTitle(task.getTitle());
        result.setDescription(task.getDescription());
        return result;
    }
}
