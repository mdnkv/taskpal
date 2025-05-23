package dev.mednikov.taskpal.projects.services;

import dev.mednikov.taskpal.projects.domain.ProjectDto;

import java.util.List;
import java.util.Optional;

public interface ProjectService {

    ProjectDto createProject(ProjectDto projectDto);

    ProjectDto updateProject(ProjectDto projectDto);

    void deleteProject(Long projectId);

    Optional<ProjectDto> getProject(Long projectId);

    List<ProjectDto> getProjects(Long workspaceId);

}
