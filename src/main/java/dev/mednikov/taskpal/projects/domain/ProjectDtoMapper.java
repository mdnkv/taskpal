package dev.mednikov.taskpal.projects.domain;

import dev.mednikov.taskpal.projects.models.Project;
import dev.mednikov.taskpal.statuses.domain.StatusDto;
import dev.mednikov.taskpal.statuses.domain.StatusDtoMapper;
import dev.mednikov.taskpal.statuses.models.Status;

import java.util.function.Function;

public final class ProjectDtoMapper implements Function<Project, ProjectDto> {

    private final static StatusDtoMapper statusMapper = new StatusDtoMapper();

    @Override
    public ProjectDto apply(Project project) {
        ProjectDto result = new ProjectDto();
        if (project.getStatus().isPresent()) {
            Status status = project.getStatus().get();
            StatusDto statusDto =  statusMapper.apply(status);
            result.setStatusId(statusDto.getId());
            result.setStatus(statusDto);
        }
        result.setId(project.getId().toString());
        result.setWorkspaceId(project.getWorkspace().getId().toString());
        result.setName(project.getName());
        return result;
    }

}
