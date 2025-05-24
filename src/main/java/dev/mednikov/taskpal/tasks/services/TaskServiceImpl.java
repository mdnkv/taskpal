package dev.mednikov.taskpal.tasks.services;

import cn.hutool.core.lang.generator.SnowflakeGenerator;
import dev.mednikov.taskpal.priorities.repositories.PriorityRepository;
import dev.mednikov.taskpal.projects.models.Project;
import dev.mednikov.taskpal.projects.repositories.ProjectRepository;
import dev.mednikov.taskpal.statuses.repositories.StatusRepository;
import dev.mednikov.taskpal.tasks.domain.TaskDto;
import dev.mednikov.taskpal.tasks.domain.TaskDtoMapper;
import dev.mednikov.taskpal.tasks.exceptions.TaskNotFoundException;
import dev.mednikov.taskpal.tasks.models.Task;
import dev.mednikov.taskpal.tasks.repositories.TaskRepository;
import dev.mednikov.taskpal.workspaces.models.Workspace;
import dev.mednikov.taskpal.workspaces.repositories.WorkspaceRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class TaskServiceImpl implements TaskService {

    private final static SnowflakeGenerator snowflakeGenerator = new SnowflakeGenerator();
    private final static TaskDtoMapper taskDtoMapper = new TaskDtoMapper();

    private final TaskRepository taskRepository;
    private final ProjectRepository projectRepository;
    private final WorkspaceRepository workspaceRepository;
    private final StatusRepository statusRepository;
    private final PriorityRepository priorityRepository;

    public TaskServiceImpl(
            TaskRepository taskRepository,
            ProjectRepository projectRepository,
            WorkspaceRepository workspaceRepository,
            StatusRepository statusRepository,
            PriorityRepository priorityRepository) {
        this.taskRepository = taskRepository;
        this.projectRepository = projectRepository;
        this.workspaceRepository = workspaceRepository;
        this.statusRepository = statusRepository;
        this.priorityRepository = priorityRepository;
    }

    @Override
    public TaskDto createTask(TaskDto taskDto) {
        Long projectId = Long.parseLong(taskDto.getProjectId());
        Long workspaceId = Long.parseLong(taskDto.getWorkspaceId());
        Project project = this.projectRepository.getReferenceById(projectId);
        Workspace workspace = this.workspaceRepository.getReferenceById(workspaceId);
        Task task = new Task();
        task.setId(snowflakeGenerator.next());
        task.setWorkspace(workspace);
        task.setProject(project);
        task.setTitle(taskDto.getTitle());
        task.setDescription(taskDto.getDescription());
        Task result = this.taskRepository.save(task);
        return taskDtoMapper.apply(result);
    }

    @Override
    public TaskDto updateTask(TaskDto taskDto) {
        Objects.requireNonNull(taskDto.getId());
        Long taskId = Long.parseLong(taskDto.getId());
        Task task = this.taskRepository.findById(taskId).orElseThrow(TaskNotFoundException::new);
        task.setTitle(taskDto.getTitle());
        task.setDescription(taskDto.getDescription());

        // Verify that the project was changed
        Long projectId = Long.parseLong(taskDto.getProjectId());
        if (!projectId.equals(task.getProject().getId())){
            Project project = this.projectRepository.getReferenceById(projectId);
            task.setProject(project);
        }

        // verify that the status was changed
        if (taskDto.getStatusId() != null){
            Long statusId = Long.parseLong(taskDto.getStatusId());
            task.setStatus(this.statusRepository.getReferenceById(statusId));
        }

        // verify that the priority was changed
        if (taskDto.getPriorityId() != null){
            Long priorityId = Long.parseLong(taskDto.getPriorityId());
            task.setPriority(this.priorityRepository.getReferenceById(priorityId));
        }

        Task result = this.taskRepository.save(task);
        return taskDtoMapper.apply(result);
    }

    @Override
    public void deleteTask(Long taskId) {
        this.taskRepository.deleteById(taskId);
    }

    @Override
    public Optional<TaskDto> getTask(Long taskId) {
        return this.taskRepository.findById(taskId).map(taskDtoMapper);
    }

    @Override
    public List<TaskDto> getTasksInWorkspace(Long workspaceId) {
        return this.taskRepository.findAllByWorkspaceId(workspaceId)
                .stream().map(taskDtoMapper).toList();
    }
}
