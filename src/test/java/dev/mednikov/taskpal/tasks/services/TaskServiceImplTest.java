package dev.mednikov.taskpal.tasks.services;

import cn.hutool.core.lang.generator.SnowflakeGenerator;
import dev.mednikov.taskpal.priorities.models.Priority;
import dev.mednikov.taskpal.priorities.repositories.PriorityRepository;
import dev.mednikov.taskpal.projects.models.Project;
import dev.mednikov.taskpal.projects.repositories.ProjectRepository;
import dev.mednikov.taskpal.statuses.models.Status;
import dev.mednikov.taskpal.statuses.repositories.StatusRepository;
import dev.mednikov.taskpal.tasks.domain.TaskDto;
import dev.mednikov.taskpal.tasks.exceptions.TaskNotFoundException;
import dev.mednikov.taskpal.tasks.models.Task;
import dev.mednikov.taskpal.tasks.repositories.TaskRepository;
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
class TaskServiceImplTest {

    private final static SnowflakeGenerator snowflakeGenerator = new SnowflakeGenerator();

    @Mock private WorkspaceRepository workspaceRepository;
    @Mock private ProjectRepository projectRepository;
    @Mock private TaskRepository taskRepository;
    @Mock private PriorityRepository priorityRepository;
    @Mock private StatusRepository statusRepository;

    @InjectMocks private TaskServiceImpl taskService;

    @Test
    void createTaskTest(){
        Long projectId = snowflakeGenerator.next();
        Long taskId = snowflakeGenerator.next();
        Long workspaceId = snowflakeGenerator.next();

        Workspace workspace = new Workspace();
        workspace.setId(workspaceId);
        workspace.setName("Wahl Harms Stiftung & Co. KGaA");
        workspace.setPersonal(false);

        Project project = new Project();
        project.setId(projectId);
        project.setName("New project");
        project.setWorkspace(workspace);

        Task task = new Task();
        task.setId(taskId);
        task.setTitle("Lorem ipsum dolor sit amet");
        task.setDescription("Vivamus auctor nec ex id tempor");
        task.setProject(project);
        task.setWorkspace(workspace);

        TaskDto payload = new TaskDto();
        payload.setTitle("Lorem ipsum dolor sit amet");
        payload.setDescription("Vivamus auctor nec ex id tempor");
        payload.setProjectId(projectId.toString());
        payload.setWorkspaceId(workspaceId.toString());

        Mockito.when(workspaceRepository.getReferenceById(workspaceId)).thenReturn(workspace);
        Mockito.when(projectRepository.getReferenceById(projectId)).thenReturn(project);
        Mockito.when(taskRepository.save(task)).thenReturn(task);

        TaskDto result = taskService.createTask(payload);
        Assertions.assertThat(result).isNotNull();


    }

    @Test
    void updateTask_notFoundTest(){
        Long projectId = snowflakeGenerator.next();
        Long taskId = snowflakeGenerator.next();
        Long workspaceId = snowflakeGenerator.next();

        TaskDto payload = new TaskDto();
        payload.setId(taskId.toString());
        payload.setTitle("Lorem ipsum dolor sit amet");
        payload.setDescription("Vivamus auctor nec ex id tempor");
        payload.setProjectId(projectId.toString());
        payload.setWorkspaceId(workspaceId.toString());

        Mockito.when(taskRepository.findById(taskId)).thenReturn(Optional.empty());
        Assertions.assertThatThrownBy(() -> taskService.updateTask(payload)).isInstanceOf(TaskNotFoundException.class);
    }

    @Test
    void updateTask_successTest(){
        Long projectId = snowflakeGenerator.next();
        Long taskId = snowflakeGenerator.next();
        Long workspaceId = snowflakeGenerator.next();

        Workspace workspace = new Workspace();
        workspace.setId(workspaceId);
        workspace.setName("Fritz GmbH & Co. KG");
        workspace.setPersonal(false);

        Project project = new Project();
        project.setId(projectId);
        project.setName("New project");
        project.setWorkspace(workspace);

        Task task = new Task();
        task.setId(taskId);
        task.setTitle("Aenean odio odio");
        task.setDescription("Nulla convallis sem at pretium");
        task.setProject(project);
        task.setWorkspace(workspace);

        TaskDto payload = new TaskDto();
        payload.setId(taskId.toString());
        payload.setTitle("Aenean odio odio");
        payload.setDescription("Nulla convallis sem at pretium");
        payload.setProjectId(projectId.toString());
        payload.setWorkspaceId(workspaceId.toString());

        Mockito.when(taskRepository.findById(taskId)).thenReturn(Optional.of(task));
        Mockito.when(taskRepository.save(task)).thenReturn(task);

        TaskDto result = taskService.updateTask(payload);
        Assertions.assertThat(result).isNotNull();
    }

    @Test
    void updateTaskWithStatusAndPriority_successTest(){
        Long projectId = snowflakeGenerator.next();
        Long taskId = snowflakeGenerator.next();
        Long workspaceId = snowflakeGenerator.next();
        Long priorityId = snowflakeGenerator.next();
        Long statusId = snowflakeGenerator.next();

        Workspace workspace = new Workspace();
        workspace.setId(workspaceId);
        workspace.setName("Jost Marquardt GmbH & Co. KG");
        workspace.setPersonal(false);

        Status status = new Status();
        status.setId(statusId);
        status.setName("In Progress");
        status.setWorkspace(workspace);

        Priority priority = new Priority();
        priority.setId(priorityId);
        priority.setName("High");
        priority.setWorkspace(workspace);
        priority.setUiOrder(1);

        Project project = new Project();
        project.setId(projectId);
        project.setName("New project");
        project.setWorkspace(workspace);

        Task task = new Task();
        task.setId(taskId);
        task.setTitle("Duis id nulla sed leo consequat");
        task.setDescription("Aliquam scelerisque lectus nec ullamcorper condimentum");
        task.setProject(project);
        task.setWorkspace(workspace);
        task.setStatus(status);
        task.setPriority(priority);

        TaskDto payload = new TaskDto();
        payload.setId(taskId.toString());
        payload.setTitle("Duis id nulla sed leo consequat");
        payload.setDescription("Aliquam scelerisque lectus nec ullamcorper condimentum");
        payload.setProjectId(projectId.toString());
        payload.setWorkspaceId(workspaceId.toString());
        payload.setStatusId(statusId.toString());
        payload.setPriorityId(priorityId.toString());

        Mockito.when(taskRepository.findById(taskId)).thenReturn(Optional.of(task));
        Mockito.when(statusRepository.getReferenceById(statusId)).thenReturn(status);
        Mockito.when(priorityRepository.getReferenceById(priorityId)).thenReturn(priority);
        Mockito.when(taskRepository.save(task)).thenReturn(task);

        TaskDto result = taskService.updateTask(payload);
        Assertions.assertThat(result)
                .isNotNull()
                .hasFieldOrProperty("status")
                .hasFieldOrProperty("priority");
    }

    @Test
    void getTasksInWorkspaceTest(){
        Long workspaceId = snowflakeGenerator.next();
        Workspace workspace = new Workspace();
        workspace.setId(workspaceId);
        workspace.setName("Wilke Thiel AG & Co. KGaA");
        workspace.setPersonal(false);

        Project project = new Project();
        project.setId(snowflakeGenerator.next());
        project.setName("New project");
        project.setWorkspace(workspace);

        List<Task> tasks = new ArrayList<>();

        Task task = new Task();
        task.setId(snowflakeGenerator.next());
        task.setTitle("Phasellus tempor mattis felis");
        task.setDescription("Etiam dui nulla, dapibus ac luctus id, luctus a lacus.");
        task.setProject(project);
        task.setWorkspace(workspace);
        tasks.add(task);

        Mockito.when(taskRepository.findAllByWorkspaceId(workspaceId)).thenReturn(tasks);
        List<TaskDto> result = taskService.getTasksInWorkspace(workspaceId);
        Assertions.assertThat(result).isNotNull();
        Assertions.assertThat(result).hasSize(1);

    }

    @Test
    void getTask_notExistsTest(){
        Long taskId = snowflakeGenerator.next();
        Mockito.when(taskRepository.findById(taskId)).thenReturn(Optional.empty());
        Optional<TaskDto> result = taskService.getTask(taskId);
        Assertions.assertThat(result).isEmpty();
    }

    @Test
    void getTask_existsTest(){
        Long projectId = snowflakeGenerator.next();
        Long taskId = snowflakeGenerator.next();
        Long workspaceId = snowflakeGenerator.next();

        Workspace workspace = new Workspace();
        workspace.setId(workspaceId);
        workspace.setName("Seeger Münch GmbH & Co. KGaA");
        workspace.setPersonal(false);

        Project project = new Project();
        project.setId(projectId);
        project.setName("New project");
        project.setWorkspace(workspace);

        Task task = new Task();
        task.setId(taskId);
        task.setTitle("Aenean odio odio");
        task.setDescription("Nulla convallis sem at pretium");
        task.setProject(project);
        task.setWorkspace(workspace);

        Mockito.when(taskRepository.findById(taskId)).thenReturn(Optional.of(task));
        Optional<TaskDto> result = taskService.getTask(taskId);
        Assertions.assertThat(result).isPresent();
    }

}
