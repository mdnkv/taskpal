package dev.mednikov.taskpal.projects.controllers;

import cn.hutool.core.lang.generator.SnowflakeGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.mednikov.taskpal.projects.domain.ProjectDto;
import dev.mednikov.taskpal.projects.exceptions.ProjectNotFoundException;
import dev.mednikov.taskpal.projects.services.ProjectService;
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

@WebMvcTest(ProjectRestController.class)
class ProjectRestControllerTest {

    private final static SnowflakeGenerator snowflakeGenerator = new SnowflakeGenerator();

    @Autowired private MockMvc mockMvc;
    @Autowired private ObjectMapper objectMapper;

    @MockitoBean private ProjectService projectService;

    @Test
    void createProjectTest () throws Exception {
        Long workspaceId = snowflakeGenerator.next();
        Long projectId = snowflakeGenerator.next();
        ProjectDto payload = new ProjectDto();
        payload.setId(projectId.toString());
        payload.setWorkspaceId(workspaceId.toString());
        payload.setName("My new project");

        Mockito.when(projectService.createProject(Mockito.any())).thenReturn(payload);

        String keycloakId = UUID.randomUUID().toString();
        String body = objectMapper.writeValueAsString(payload);

        mockMvc.perform(
                        post("/projects/create")
                                .with(jwt().jwt(jwt -> jwt.claim("sub", keycloakId)))
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(body))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(projectId.toString()))
                .andExpect(jsonPath("$.workspaceId").value(workspaceId.toString()))
                .andExpect(jsonPath("$.name").value("My new project"));
    }

    @Test
    void updateProject_notFoundTest () throws Exception {
        Long workspaceId = snowflakeGenerator.next();
        Long projectId = snowflakeGenerator.next();
        ProjectDto payload = new ProjectDto();
        payload.setId(projectId.toString());
        payload.setWorkspaceId(workspaceId.toString());
        payload.setName("My new project");

        Mockito.when(projectService.updateProject(Mockito.any())).thenThrow(ProjectNotFoundException.class);

        String keycloakId = UUID.randomUUID().toString();
        String body = objectMapper.writeValueAsString(payload);

        mockMvc.perform(
                        put("/projects/update")
                                .with(jwt().jwt(jwt -> jwt.claim("sub", keycloakId)))
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(body))
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateProject_successTest () throws Exception {
        Long workspaceId = snowflakeGenerator.next();
        Long projectId = snowflakeGenerator.next();
        ProjectDto payload = new ProjectDto();
        payload.setId(projectId.toString());
        payload.setWorkspaceId(workspaceId.toString());
        payload.setName("My new project");

        Mockito.when(projectService.updateProject(Mockito.any())).thenReturn(payload);


        String keycloakId = UUID.randomUUID().toString();
        String body = objectMapper.writeValueAsString(payload);

        mockMvc.perform(
                        put("/projects/update")
                                .with(jwt().jwt(jwt -> jwt.claim("sub", keycloakId)))
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(body))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(projectId.toString()))
                .andExpect(jsonPath("$.workspaceId").value(workspaceId.toString()))
                .andExpect(jsonPath("$.name").value("My new project"));
    }

    @Test
    void deleteProjectTest() throws Exception{
        Long projectId = snowflakeGenerator.next();
        String keycloakId = UUID.randomUUID().toString();

        Mockito.doNothing().when(projectService).deleteProject(projectId);

        mockMvc.perform(
                        delete("/projects/delete/{projectId}", projectId)
                                .with(jwt().jwt(jwt -> jwt.claim("sub", keycloakId))))
                .andExpect(status().isNoContent());
    }

    @Test
    void getProject_notExistsTest() throws Exception{
        Long projectId = snowflakeGenerator.next();
        String keycloakId = UUID.randomUUID().toString();

        Mockito.when(projectService.getProject(projectId)).thenReturn(Optional.empty());

        mockMvc.perform(
                        get("/projects/project/{projectId}", projectId)
                                .with(jwt().jwt(jwt -> jwt.claim("sub", keycloakId))))
                .andExpect(status().isNotFound());
    }

    @Test
    void getProject_existsTest() throws Exception{
        Long workspaceId = snowflakeGenerator.next();
        Long projectId = snowflakeGenerator.next();
        ProjectDto payload = new ProjectDto();
        payload.setId(projectId.toString());
        payload.setWorkspaceId(workspaceId.toString());
        payload.setName("My new project");

        Mockito.when(projectService.getProject(projectId)).thenReturn(Optional.of(payload));


        String keycloakId = UUID.randomUUID().toString();

        mockMvc.perform(
                        get("/projects/project/{projectId}", projectId)
                                .with(jwt().jwt(jwt -> jwt.claim("sub", keycloakId)))
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(projectId.toString()))
                .andExpect(jsonPath("$.workspaceId").value(workspaceId.toString()))
                .andExpect(jsonPath("$.name").value("My new project"));
    }

    @Test
    void getProjectsTest() throws Exception{
        Long workspaceId = snowflakeGenerator.next();

        Mockito.when(projectService.getProjects(workspaceId)).thenReturn(List.of());
        String keycloakId = UUID.randomUUID().toString();

        mockMvc.perform(
                        get("/projects/workspace/{workspaceId}", workspaceId)
                                .with(jwt().jwt(jwt -> jwt.claim("sub", keycloakId))))
                .andExpect(status().isOk());
    }

}
