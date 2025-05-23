package dev.mednikov.taskpal.projects.services;

import cn.hutool.core.lang.generator.SnowflakeGenerator;
import dev.mednikov.taskpal.projects.domain.ProjectDto;
import dev.mednikov.taskpal.projects.domain.ProjectDtoMapper;
import dev.mednikov.taskpal.projects.exceptions.ProjectNotFoundException;
import dev.mednikov.taskpal.projects.models.Project;
import dev.mednikov.taskpal.projects.repositories.ProjectRepository;
import dev.mednikov.taskpal.workspaces.models.Workspace;
import dev.mednikov.taskpal.workspaces.repositories.WorkspaceRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class ProjectServiceImpl implements ProjectService{

    private final static SnowflakeGenerator snowflakeGenerator = new SnowflakeGenerator();
    private final static ProjectDtoMapper projectDtoMapper = new ProjectDtoMapper();

    private final ProjectRepository projectRepository;
    private final WorkspaceRepository workspaceRepository;

    public ProjectServiceImpl(ProjectRepository projectRepository, WorkspaceRepository workspaceRepository) {
        this.projectRepository = projectRepository;
        this.workspaceRepository = workspaceRepository;
    }

    @Override
    public ProjectDto createProject(ProjectDto projectDto) {
        Long workspaceId = Long.parseLong(projectDto.getWorkspaceId());
        Workspace workspace = this.workspaceRepository.getReferenceById(workspaceId);
        Project project = new Project();
        project.setId(snowflakeGenerator.next());
        project.setWorkspace(workspace);
        project.setName(projectDto.getName());

        Project result = this.projectRepository.save(project);
        return projectDtoMapper.apply(result);
    }

    @Override
    public ProjectDto updateProject(ProjectDto projectDto) {
        Objects.requireNonNull(projectDto.getId());
        Project project = this.projectRepository.findById(Long.parseLong(projectDto.getId())).orElseThrow(ProjectNotFoundException::new);
        project.setName(projectDto.getName());
        Project result = this.projectRepository.save(project);
        return projectDtoMapper.apply(result);
    }

    @Override
    public void deleteProject(Long projectId) {
        this.projectRepository.deleteById(projectId);
    }

    @Override
    public Optional<ProjectDto> getProject(Long projectId) {
        return this.projectRepository.findById(projectId).map(projectDtoMapper);
    }

    @Override
    public List<ProjectDto> getProjects(Long workspaceId) {
        return this.projectRepository.findAllByWorkspaceId(workspaceId)
                .stream().map(projectDtoMapper).toList();
    }

}
