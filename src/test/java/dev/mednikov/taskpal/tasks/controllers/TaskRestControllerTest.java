package dev.mednikov.taskpal.tasks.controllers;

import cn.hutool.core.lang.generator.SnowflakeGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.mednikov.taskpal.projects.domain.ProjectDto;
import dev.mednikov.taskpal.tasks.domain.TaskDto;
import dev.mednikov.taskpal.tasks.exceptions.TaskNotFoundException;
import dev.mednikov.taskpal.tasks.services.TaskService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TaskRestController.class)
class TaskRestControllerTest {

    private final static SnowflakeGenerator snowflakeGenerator = new SnowflakeGenerator();

    @Autowired private MockMvc mockMvc;
    @Autowired private ObjectMapper objectMapper;

    @MockitoBean private TaskService taskService;

    @Test
    void createTaskTest() throws Exception{
        Long taskId = snowflakeGenerator.next();
        Long projectId = snowflakeGenerator.next();
        Long workspaceId = snowflakeGenerator.next();

        ProjectDto projectDto = new ProjectDto();
        projectDto.setName("My project");
        projectDto.setId(projectId.toString());
        projectDto.setWorkspaceId(workspaceId.toString());

        TaskDto payload = new TaskDto();
        payload.setId(taskId.toString());
        payload.setProject(projectDto);
        payload.setProjectId(projectId.toString());
        payload.setWorkspaceId(workspaceId.toString());
        payload.setTitle("Fusce ut tempus tellus");
        payload.setDescription("Praesent id quam vitae nibh mattis fermentum fringilla hendrerit tortor");

        String keycloakId = UUID.randomUUID().toString();
        String body = objectMapper.writeValueAsString(payload);

        Mockito.when(taskService.createTask(Mockito.any())).thenReturn(payload);

        mockMvc.perform(
                        post("/tasks/create")
                                .with(jwt().jwt(jwt -> jwt.claim("sub", keycloakId)))
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(body))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(taskId.toString()))
                .andExpect(jsonPath("$.workspaceId").value(workspaceId.toString()))
                .andExpect(jsonPath("$.projectId").value(projectId.toString()))
                .andExpect(jsonPath("$.project").exists())
                .andExpect(jsonPath("$.title").value("Fusce ut tempus tellus"))
                .andExpect(jsonPath("$.description").value("Praesent id quam vitae nibh mattis fermentum fringilla hendrerit tortor"));
    }

    @Test
    void updateTask_notFoundTest() throws Exception{
        Long taskId = snowflakeGenerator.next();
        Long projectId = snowflakeGenerator.next();
        Long workspaceId = snowflakeGenerator.next();

        TaskDto payload = new TaskDto();
        payload.setId(taskId.toString());
        payload.setProjectId(projectId.toString());
        payload.setWorkspaceId(workspaceId.toString());
        payload.setTitle("Maecenas vulputate nunc sem");
        payload.setDescription("Duis maximus sagittis sem, et semper urna fringilla eget");

        String keycloakId = UUID.randomUUID().toString();
        String body = objectMapper.writeValueAsString(payload);

        Mockito.when(taskService.updateTask(Mockito.any())).thenThrow(TaskNotFoundException.class);

        mockMvc.perform(
                        put("/tasks/update")
                                .with(jwt().jwt(jwt -> jwt.claim("sub", keycloakId)))
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(body))
                .andExpect(status().isBadRequest());

    }

    @Test
    void updateTask_successTest() throws Exception{
        Long taskId = snowflakeGenerator.next();
        Long projectId = snowflakeGenerator.next();
        Long workspaceId = snowflakeGenerator.next();

        ProjectDto projectDto = new ProjectDto();
        projectDto.setName("My project");
        projectDto.setId(projectId.toString());
        projectDto.setWorkspaceId(workspaceId.toString());

        TaskDto payload = new TaskDto();
        payload.setId(taskId.toString());
        payload.setProject(projectDto);
        payload.setProjectId(projectId.toString());
        payload.setWorkspaceId(workspaceId.toString());
        payload.setTitle("Maecenas vulputate nunc sem");
        payload.setDescription("Duis maximus sagittis sem, et semper urna fringilla eget");

        String keycloakId = UUID.randomUUID().toString();
        String body = objectMapper.writeValueAsString(payload);

        Mockito.when(taskService.updateTask(Mockito.any())).thenReturn(payload);

        mockMvc.perform(
                        put("/tasks/update")
                                .with(jwt().jwt(jwt -> jwt.claim("sub", keycloakId)))
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(body))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(taskId.toString()))
                .andExpect(jsonPath("$.workspaceId").value(workspaceId.toString()))
                .andExpect(jsonPath("$.project").exists())
                .andExpect(jsonPath("$.projectId").value(projectId.toString()))
                .andExpect(jsonPath("$.title").value("Maecenas vulputate nunc sem"))
                .andExpect(jsonPath("$.description").value("Duis maximus sagittis sem, et semper urna fringilla eget"));
    }

    @Test
    void deleteTaskTest() throws Exception{
        Long taskId = snowflakeGenerator.next();
        String keycloakId = UUID.randomUUID().toString();
        Mockito.doNothing().when(taskService).deleteTask(taskId);

        mockMvc.perform(
                        delete("/tasks/delete/{taskId}", taskId)
                                .with(jwt().jwt(jwt -> jwt.claim("sub", keycloakId))))
                .andExpect(status().isNoContent());
    }

    @Test
    void getTask_existsTest() throws Exception{
        Long taskId = snowflakeGenerator.next();
        Long projectId = snowflakeGenerator.next();
        Long workspaceId = snowflakeGenerator.next();

        ProjectDto projectDto = new ProjectDto();
        projectDto.setName("My project");
        projectDto.setId(projectId.toString());
        projectDto.setWorkspaceId(workspaceId.toString());

        TaskDto payload = new TaskDto();
        payload.setId(taskId.toString());
        payload.setProject(projectDto);
        payload.setProjectId(projectId.toString());
        payload.setWorkspaceId(workspaceId.toString());
        payload.setTitle("Donec quis urna eget magna");
        payload.setDescription("Morbi semper lectus vel laoreet efficitur");

        String keycloakId = UUID.randomUUID().toString();

        Mockito.when(taskService.getTask(taskId)).thenReturn(Optional.of(payload));

        mockMvc.perform(
                        get("/tasks/task/{taskId}", taskId)
                                .with(jwt().jwt(jwt -> jwt.claim("sub", keycloakId)))
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(taskId.toString()))
                .andExpect(jsonPath("$.workspaceId").value(workspaceId.toString()))
                .andExpect(jsonPath("$.projectId").value(projectId.toString()))
                .andExpect(jsonPath("$.project").exists())
                .andExpect(jsonPath("$.title").value("Donec quis urna eget magna"))
                .andExpect(jsonPath("$.description").value("Morbi semper lectus vel laoreet efficitur"));

    }

    @Test
    void getTask_notExistsTest() throws Exception{
        Long taskId = snowflakeGenerator.next();
        String keycloakId = UUID.randomUUID().toString();

        Mockito.when(taskService.getTask(taskId)).thenReturn(Optional.empty());

        mockMvc.perform(
                        get("/tasks/task/{taskId}", taskId)
                                .with(jwt().jwt(jwt -> jwt.claim("sub", keycloakId))))
                .andExpect(status().isNotFound());
    }

    @Test
    void getTasksInWorkspaceTest() throws Exception{
        Long workspaceId = snowflakeGenerator.next();
        String keycloakId = UUID.randomUUID().toString();

        Mockito.when(taskService.getTasksInWorkspace(workspaceId)).thenReturn(List.of());
        mockMvc.perform(
                        get("/tasks/workspace/{workspaceId}", workspaceId)
                                .with(jwt().jwt(jwt -> jwt.claim("sub", keycloakId))))
                .andExpect(status().isOk());
    }

}
