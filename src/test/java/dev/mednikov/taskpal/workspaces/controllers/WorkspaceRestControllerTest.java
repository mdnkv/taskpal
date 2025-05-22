package dev.mednikov.taskpal.workspaces.controllers;

import cn.hutool.core.lang.UUID;
import cn.hutool.core.lang.generator.SnowflakeGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.mednikov.taskpal.users.models.User;
import dev.mednikov.taskpal.users.services.UserService;
import dev.mednikov.taskpal.workspaces.domain.WorkspaceDto;
import dev.mednikov.taskpal.workspaces.services.WorkspaceService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(WorkspaceRestController.class)
class WorkspaceRestControllerTest {

    private final static SnowflakeGenerator snowflakeGenerator = new SnowflakeGenerator();

    @Autowired private MockMvc mockMvc;
    @Autowired private ObjectMapper objectMapper;
    @MockitoBean private WorkspaceService workspaceService;
    @MockitoBean private UserService userService;

    @Test
    void createWorkspaceTest() throws Exception{
        WorkspaceDto payload = new WorkspaceDto();
        payload.setName("My workspace");
        payload.setPersonal(true);

        WorkspaceDto result = new WorkspaceDto();
        result.setId(snowflakeGenerator.next().toString());
        result.setName("My workspace");
        result.setPersonal(true);

        String keycloakId = UUID.randomUUID().toString();

        User user = new User();
        user.setId(snowflakeGenerator.next());
        user.setKeycloakId(keycloakId);
        user.setEmail("30kkkbtxsr1oy2f4b9fx@gmail.com");
        user.setFirstName("Manuela");
        user.setLastName("Schütz");

        Mockito.when(workspaceService.createWorkspace(Mockito.any())).thenReturn(result);
        Mockito.when(userService.getOrCreateUser(Mockito.any())).thenReturn(user);

        String body = objectMapper.writeValueAsString(payload);
        mockMvc.perform(
                post("/workspaces/create")
                        .with(jwt().jwt(jwt -> jwt.claim("sub", keycloakId)))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(result.getId()))
                .andExpect(jsonPath("$.name").value(result.getName()))
                .andExpect(jsonPath("$.personal").value(result.isPersonal()));
    }

    @Test
    void updateWorkspaceTest() throws Exception{
        WorkspaceDto payload = new WorkspaceDto();
        payload.setId(snowflakeGenerator.next().toString());
        payload.setName("My workspace");
        payload.setPersonal(true);
        String keycloakId = UUID.randomUUID().toString();
        Mockito.when(workspaceService.updateWorkspace(Mockito.any())).thenReturn(payload);

        String body = objectMapper.writeValueAsString(payload);
        mockMvc.perform(
                        put("/workspaces/update")
                                .with(jwt().jwt(jwt -> jwt.claim("sub", keycloakId)))
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(body))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(payload.getId()))
                .andExpect(jsonPath("$.name").value(payload.getName()))
                .andExpect(jsonPath("$.personal").value(payload.isPersonal()));
    }

    @Test
    void deleteWorkspaceTest() throws Exception{
        Long id = snowflakeGenerator.next();
        String keycloakId = UUID.randomUUID().toString();
        Mockito.doNothing().when(workspaceService).deleteWorkspace(id);
        mockMvc.perform(
                        delete("/workspaces/delete/{workspaceId}", id)
                                .with(jwt().jwt(jwt -> jwt.claim("sub", keycloakId)))
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    void getWorkspace_existsTest() throws Exception{
        Long id = snowflakeGenerator.next();
        WorkspaceDto payload = new WorkspaceDto();
        payload.setId(id.toString());
        payload.setName("My workspace");
        payload.setPersonal(true);

        String keycloakId = UUID.randomUUID().toString();
        Mockito.when(workspaceService.getWorkspace(id)).thenReturn(Optional.of(payload));
        mockMvc.perform(
                        get("/workspaces/workspace/{workspaceId}", id)
                                .with(jwt().jwt(jwt -> jwt.claim("sub", keycloakId)))
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(payload.getId()))
                .andExpect(jsonPath("$.name").value(payload.getName()))
                .andExpect(jsonPath("$.personal").value(payload.isPersonal()));
    }

    @Test
    void getWorkspace_doesNotExistTest() throws Exception{
        Long id = snowflakeGenerator.next();
        String keycloakId = UUID.randomUUID().toString();
        Mockito.when(workspaceService.getWorkspace(id)).thenReturn(Optional.empty());
        mockMvc.perform(
                        get("/workspaces/workspace/{workspaceId}", id)
                                .with(jwt().jwt(jwt -> jwt.claim("sub", keycloakId)))
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }


}
