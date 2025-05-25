package dev.mednikov.taskpal.workspaces.controllers;

import cn.hutool.core.lang.generator.SnowflakeGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.mednikov.taskpal.roles.domain.RoleDto;
import dev.mednikov.taskpal.users.exceptions.UserNotFoundException;
import dev.mednikov.taskpal.users.models.User;
import dev.mednikov.taskpal.users.services.UserService;
import dev.mednikov.taskpal.workspaces.domain.CreateWorkspaceUserRequestDto;
import dev.mednikov.taskpal.workspaces.domain.WorkspaceDto;
import dev.mednikov.taskpal.workspaces.domain.WorkspaceUserDto;
import dev.mednikov.taskpal.workspaces.exceptions.PersonalWorkspaceException;
import dev.mednikov.taskpal.workspaces.exceptions.WorkspaceUserAlreadyExistsException;
import dev.mednikov.taskpal.workspaces.services.WorkspaceUserService;
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

@WebMvcTest(WorkspaceUserRestController.class)
class WorkspaceUserRestControllerTest {

    private final static SnowflakeGenerator snowflakeGenerator = new SnowflakeGenerator();

    @Autowired private MockMvc mockMvc;
    @Autowired private ObjectMapper objectMapper;
    @MockitoBean private UserService userService;
    @MockitoBean private WorkspaceUserService workspaceUserService;

    @Test
    void createWorkspaceUser_userNotFoundTest() throws Exception {
        Long workspaceId = snowflakeGenerator.next();
        Long roleId = snowflakeGenerator.next();
        String email = "vfm9hpuhvs2oloj4@hotmail.com";

        CreateWorkspaceUserRequestDto payload = new CreateWorkspaceUserRequestDto();
        payload.setEmail(email);
        payload.setWorkspaceId(workspaceId.toString());
        payload.setRoleId(roleId.toString());

        Mockito.when(workspaceUserService.createWorkspaceUser(Mockito.any())).thenThrow(UserNotFoundException.class);
        String keycloakId = UUID.randomUUID().toString();
        String body = objectMapper.writeValueAsString(payload);

        mockMvc.perform(
                        post("/workspaces/users/create")
                                .with(jwt().jwt(jwt -> jwt.claim("sub", keycloakId)))
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(body))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createWorkspaceUser_alreadyExistsTest() throws Exception {
        Long workspaceId = snowflakeGenerator.next();
        Long roleId = snowflakeGenerator.next();
        String email = "aix4b96qnkvc5oqgp@msn.com";

        CreateWorkspaceUserRequestDto payload = new CreateWorkspaceUserRequestDto();
        payload.setEmail(email);
        payload.setWorkspaceId(workspaceId.toString());
        payload.setRoleId(roleId.toString());

        Mockito.when(workspaceUserService.createWorkspaceUser(Mockito.any())).thenThrow(WorkspaceUserAlreadyExistsException.class);
        String keycloakId = UUID.randomUUID().toString();
        String body = objectMapper.writeValueAsString(payload);

        mockMvc.perform(
                        post("/workspaces/users/create")
                                .with(jwt().jwt(jwt -> jwt.claim("sub", keycloakId)))
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(body))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createWorkspaceUser_personalWorkspaceTest() throws Exception {
        Long workspaceId = snowflakeGenerator.next();
        Long roleId = snowflakeGenerator.next();
        String email = "8kzq6c9t89xx2yno5dz@msn.com";

        CreateWorkspaceUserRequestDto payload = new CreateWorkspaceUserRequestDto();
        payload.setEmail(email);
        payload.setWorkspaceId(workspaceId.toString());
        payload.setRoleId(roleId.toString());

        Mockito.when(workspaceUserService.createWorkspaceUser(Mockito.any())).thenThrow(PersonalWorkspaceException.class);
        String keycloakId = UUID.randomUUID().toString();
        String body = objectMapper.writeValueAsString(payload);

        mockMvc.perform(
                        post("/workspaces/users/create")
                                .with(jwt().jwt(jwt -> jwt.claim("sub", keycloakId)))
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(body))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createWorkspaceUser_successTest() throws Exception {
        Long workspaceId = snowflakeGenerator.next();
        Long roleId = snowflakeGenerator.next();
        String email = "0rshgsnfq1@gmail.com";

        CreateWorkspaceUserRequestDto payload = new CreateWorkspaceUserRequestDto();
        payload.setEmail(email);
        payload.setWorkspaceId(workspaceId.toString());
        payload.setRoleId(roleId.toString());

        WorkspaceUserDto result = new WorkspaceUserDto();
        WorkspaceDto workspaceDto = new WorkspaceDto();
        workspaceDto.setId(workspaceId.toString());
        workspaceDto.setName("Ackermann Förster AG");
        workspaceDto.setPersonal(false);

        RoleDto roleDto = new RoleDto();
        roleDto.setId(roleId.toString());
        roleDto.setName("Administrator");
        roleDto.setAdministrator(true);
        roleDto.setWorkspaceId(workspaceId.toString());

        result.setWorkspace(workspaceDto);
        result.setRole(roleDto);
        result.setActive(true);
        result.setId(snowflakeGenerator.next().toString());

        Mockito.when(workspaceUserService.createWorkspaceUser(Mockito.any())).thenReturn(result);
        String keycloakId = UUID.randomUUID().toString();
        String body = objectMapper.writeValueAsString(payload);

        mockMvc.perform(
                        post("/workspaces/users/create")
                                .with(jwt().jwt(jwt -> jwt.claim("sub", keycloakId)))
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(body))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(result.getId()))
                .andExpect(jsonPath("$.workspace.id").value(result.getWorkspace().getId()))
                .andExpect(jsonPath("$.workspace.name").value(result.getWorkspace().getName()))
                .andExpect(jsonPath("$.workspace.personal").value(result.getWorkspace().isPersonal()))
                .andExpect(jsonPath("$.role.id").value(result.getRole().getId()))
                .andExpect(jsonPath("$.role.name").value(result.getRole().getName()))
                .andExpect(jsonPath("$.role.administrator").value(result.getRole().isAdministrator()))
                .andExpect(jsonPath("$.role.workspaceId").value(result.getRole().getWorkspaceId()))
                .andExpect(jsonPath("$.active").value(result.isActive()));

    }

    @Test
    void getActiveWorkspace_existsTest() throws Exception {
        WorkspaceUserDto result = new WorkspaceUserDto();
        WorkspaceDto workspaceDto = new WorkspaceDto();
        workspaceDto.setId(snowflakeGenerator.next().toString());
        workspaceDto.setName("Bode Bartsch AG & Co. KGaA");
        workspaceDto.setPersonal(false);
        RoleDto roleDto = new RoleDto();
        roleDto.setId(snowflakeGenerator.next().toString());
        roleDto.setName("Administrator");
        roleDto.setAdministrator(true);

        result.setWorkspace(workspaceDto);
        result.setRole(roleDto);
        result.setActive(true);
        result.setId(snowflakeGenerator.next().toString());

        String keycloakId = UUID.randomUUID().toString();
        User user = new User();
        user.setId(snowflakeGenerator.next());
        user.setEmail("xy08jqip34zmdgodn5b@ymail.com");
        user.setKeycloakId(keycloakId);

        Mockito.when(userService.getOrCreateUser(Mockito.any())).thenReturn(user);
        Mockito.when(workspaceUserService.getActiveWorkspace(Mockito.any())).thenReturn(Optional.of(result));

        mockMvc.perform(
                        get("/workspaces/users/active")
                                .with(jwt().jwt(jwt -> jwt.claim("sub", keycloakId)))
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void getActiveWorkspace_doesNotExistTest() throws Exception {
        String keycloakId = UUID.randomUUID().toString();
        User user = new User();
        user.setId(snowflakeGenerator.next());
        user.setEmail("eocjmdd0txx0803n6i09@gmail.com");
        user.setKeycloakId(keycloakId);

        Mockito.when(userService.getOrCreateUser(Mockito.any())).thenReturn(user);
        Mockito.when(workspaceUserService.getActiveWorkspace(Mockito.any())).thenReturn(Optional.empty());

        mockMvc.perform(
                        get("/workspaces/users/active")
                                .with(jwt().jwt(jwt -> jwt.claim("sub", keycloakId)))
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void setActiveWorkspaceTest() throws Exception {
        Long workspaceUserId = snowflakeGenerator.next();
        WorkspaceUserDto result = new WorkspaceUserDto();
        WorkspaceDto workspaceDto = new WorkspaceDto();
        workspaceDto.setId(snowflakeGenerator.next().toString());
        workspaceDto.setName("Harms Strauß AG & Co. KGaA");
        workspaceDto.setPersonal(false);
        RoleDto roleDto = new RoleDto();
        roleDto.setId(workspaceUserId.toString());
        roleDto.setName("Administrator");
        roleDto.setAdministrator(true);
        result.setWorkspace(workspaceDto);
        result.setRole(roleDto);
        result.setActive(true);
        result.setId(snowflakeGenerator.next().toString());

        String keycloakId = UUID.randomUUID().toString();
        User user = new User();
        user.setId(snowflakeGenerator.next());
        user.setEmail("wmq4y8spjshqlm7rmww@googlemail.com");
        user.setKeycloakId(keycloakId);

        Mockito.when(userService.getOrCreateUser(Mockito.any())).thenReturn(user);
        Mockito.when(workspaceUserService.setActiveWorkspace(user, workspaceUserId)).thenReturn(result);

        mockMvc.perform(
                        post("/workspaces/users/active/{workspaceUserId}", workspaceUserId)
                                .with(jwt().jwt(jwt -> jwt.claim("sub", keycloakId)))
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

    }

    @Test
    void getUserWorkspacesTest() throws Exception {
        String keycloakId = UUID.randomUUID().toString();
        User user = new User();
        user.setId(snowflakeGenerator.next());
        user.setEmail("5v4kg0zxeisf364xddo@comcast.net");
        user.setKeycloakId(keycloakId);

        Mockito.when(userService.getOrCreateUser(Mockito.any())).thenReturn(user);
        Mockito.when(workspaceUserService.getUserWorkspaces(user)).thenReturn(List.of());

        mockMvc.perform(
                        get("/workspaces/users/my")
                                .with(jwt().jwt(jwt -> jwt.claim("sub", keycloakId)))
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

    }

}
