package dev.mednikov.taskpal.tasks.domain;

import dev.mednikov.taskpal.priorities.domain.PriorityDto;
import dev.mednikov.taskpal.priorities.domain.PriorityDtoMapper;
import dev.mednikov.taskpal.projects.domain.ProjectDto;
import dev.mednikov.taskpal.projects.domain.ProjectDtoMapper;
import dev.mednikov.taskpal.statuses.domain.StatusDto;
import dev.mednikov.taskpal.statuses.domain.StatusDtoMapper;
import dev.mednikov.taskpal.tasks.models.Task;

import java.util.function.Function;

public final class TaskDtoMapper implements Function<Task, TaskDto> {

    private final static StatusDtoMapper statusMapper = new StatusDtoMapper();
    private final static PriorityDtoMapper priorityMapper = new PriorityDtoMapper();
    private final static ProjectDtoMapper projectMapper = new ProjectDtoMapper();

    @Override
    public TaskDto apply(Task task) {
        ProjectDto projectDto = projectMapper.apply(task.getProject());
        StatusDto statusDto = task.getStatus().map(statusMapper).orElse(null);
        PriorityDto priorityDto = task.getPriority().map(priorityMapper).orElse(null);
        TaskDto result = new TaskDto();
        result.setId(task.getId().toString());
        result.setWorkspaceId(task.getWorkspace().getId().toString());
        result.setProject(projectDto);
        result.setProjectId(projectDto.getId());
        result.setTitle(task.getTitle());
        result.setDescription(task.getDescription());
        result.setStatus(statusDto);
        result.setPriority(priorityDto);
        return result;
    }
}
