package dev.mednikov.taskpal.projects.domain;

import dev.mednikov.taskpal.projects.models.Project;

import java.util.function.Function;

public final class ProjectDtoMapper implements Function<Project, ProjectDto> {

    @Override
    public ProjectDto apply(Project project) {
        ProjectDto result = new ProjectDto();
        result.setId(project.getId().toString());
        result.setWorkspaceId(project.getWorkspace().getId().toString());
        result.setName(project.getName());
        return result;
    }

}
