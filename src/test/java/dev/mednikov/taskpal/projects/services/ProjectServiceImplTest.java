package dev.mednikov.taskpal.projects.services;

import cn.hutool.core.lang.generator.SnowflakeGenerator;
import dev.mednikov.taskpal.projects.domain.ProjectDto;
import dev.mednikov.taskpal.projects.exceptions.ProjectNotFoundException;
import dev.mednikov.taskpal.projects.models.Project;
import dev.mednikov.taskpal.projects.repositories.ProjectRepository;
import dev.mednikov.taskpal.workspaces.models.Workspace;
import dev.mednikov.taskpal.workspaces.repositories.WorkspaceRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class ProjectServiceImplTest {

    @Mock private WorkspaceRepository workspaceRepository;
    @Mock private ProjectRepository projectRepository;
    @InjectMocks private ProjectServiceImpl projectService;

    private final static SnowflakeGenerator snowflakeGenerator = new SnowflakeGenerator();

    @Test
    void createProjectTest(){
        Long workspaceId = snowflakeGenerator.next();
        Long projectId = snowflakeGenerator.next();

        Workspace workspace = new Workspace();
        workspace.setId(workspaceId);
        workspace.setName("Heinrich Menzel GmbH & Co. KG");
        workspace.setPersonal(false);

        Project project = new Project();
        project.setId(projectId);
        project.setName("New project");
        project.setWorkspace(workspace);

        ProjectDto payload = new ProjectDto();
        payload.setName("New project");
        payload.setWorkspaceId(workspaceId.toString());

        Mockito.when(workspaceRepository.getReferenceById(workspaceId)).thenReturn(workspace);
        Mockito.when(projectRepository.save(project)).thenReturn(project);

        ProjectDto result = projectService.createProject(payload);
        Assertions.assertThat(result).isNotNull();

    }

    @Test
    void updateProject_notFoundTest(){
        Long projectId = snowflakeGenerator.next();
        Long workspaceId = snowflakeGenerator.next();
        ProjectDto payload = new ProjectDto();
        payload.setId(projectId.toString());
        payload.setName("New project");
        payload.setWorkspaceId(workspaceId.toString());

        Mockito.when(projectRepository.findById(projectId)).thenReturn(Optional.empty());

        Assertions
                .assertThatThrownBy(() -> projectService.updateProject(payload))
                .isInstanceOf(ProjectNotFoundException.class);
    }

    @Test
    void updateProject_successTest(){
        Long workspaceId = snowflakeGenerator.next();
        Long projectId = snowflakeGenerator.next();

        Workspace workspace = new Workspace();
        workspace.setId(workspaceId);
        workspace.setName("Metzger AG");
        workspace.setPersonal(false);

        Project project = new Project();
        project.setId(projectId);
        project.setName("New project");
        project.setWorkspace(workspace);

        ProjectDto payload = new ProjectDto();
        payload.setName("New project");
        payload.setId(projectId.toString());
        payload.setWorkspaceId(workspaceId.toString());

        Mockito.when(projectRepository.findById(projectId)).thenReturn(Optional.of(project));
        Mockito.when(projectRepository.save(project)).thenReturn(project);

        ProjectDto result = projectService.updateProject(payload);
        Assertions.assertThat(result).isNotNull();
    }

    @Test
    void getProject_existsTest(){
        Long projectId = snowflakeGenerator.next();
        Workspace workspace = new Workspace();
        workspace.setId(snowflakeGenerator.next());
        workspace.setName("Braun Lorenz GmbH & Co. KGaA");
        workspace.setPersonal(false);

        Project project = new Project();
        project.setId(projectId);
        project.setName("New project");
        project.setWorkspace(workspace);

        Mockito.when(projectRepository.findById(projectId)).thenReturn(Optional.of(project));
        Optional<ProjectDto> result = projectService.getProject(projectId);
        Assertions.assertThat(result).isPresent();
    }

    @Test
    void getProject_doesNotExistTest(){
        Long projectId = snowflakeGenerator.next();

        Mockito.when(projectRepository.findById(projectId)).thenReturn(Optional.empty());

        Optional<ProjectDto> result = projectService.getProject(projectId);
        Assertions.assertThat(result).isEmpty();
    }

    @Test
    void getProjectsTest(){
        Long workspaceId = snowflakeGenerator.next();
        Workspace workspace = new Workspace();
        workspace.setId(workspaceId);
        workspace.setName("Janßen Baum AG & Co. KGaA");
        workspace.setPersonal(false);

        List<Project> projects = new ArrayList<>();
        Project project = new Project();
        project.setId(snowflakeGenerator.next());
        project.setName("New project");
        project.setWorkspace(workspace);

        projects.add(project);

        Mockito.when(projectRepository.findAllByWorkspaceId(workspaceId)).thenReturn(projects);

        List<ProjectDto> result = projectService.getProjects(workspaceId);
        Assertions.assertThat(result).isNotNull();
        Assertions.assertThat(result).hasSize(1);
    }

}
