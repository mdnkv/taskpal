package dev.mednikov.taskpal.roles.controllers;

import cn.hutool.core.lang.generator.SnowflakeGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.mednikov.taskpal.roles.domain.RoleDto;
import dev.mednikov.taskpal.roles.exceptions.RoleAlreadyExistsException;
import dev.mednikov.taskpal.roles.exceptions.RoleNotFoundException;
import dev.mednikov.taskpal.roles.services.RoleService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.UUID;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(RoleRestController.class)
class RoleRestControllerTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private ObjectMapper objectMapper;
    @MockitoBean private RoleService roleService;

    private final static SnowflakeGenerator snowflakeGenerator = new SnowflakeGenerator();

    @Test
    void createRoleSuccessTest() throws Exception {
        Long workspaceId = snowflakeGenerator.next();
        RoleDto role = new RoleDto();
        role.setWorkspaceId(workspaceId.toString());
        role.setId(snowflakeGenerator.next().toString());
        role.setAdministrator(true);
        role.setName("Administrator");

        String body = objectMapper.writeValueAsString(role);
        String keycloakId = UUID.randomUUID().toString();

        Mockito.when(roleService.createRole(Mockito.any())).thenReturn(role);
        mockMvc.perform(
                post("/roles/create")
                        .with(jwt().jwt(jwt -> jwt.claim("sub", keycloakId)))
                        .contentType(org.springframework.http.MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(role.getId()))
                .andExpect(jsonPath("$.name").value(role.getName()))
                .andExpect(jsonPath("$.workspaceId").value(role.getWorkspaceId()))
                .andExpect(jsonPath("$.administrator").value(role.isAdministrator()));
    }

    @Test
    void createRoleAlreadyExistsTest() throws Exception {
        Long workspaceId = snowflakeGenerator.next();
        RoleDto role = new RoleDto();
        role.setWorkspaceId(workspaceId.toString());
        role.setId(snowflakeGenerator.next().toString());
        role.setAdministrator(true);
        role.setName("Administrator");

        String body = objectMapper.writeValueAsString(role);
        String keycloakId = UUID.randomUUID().toString();

        Mockito.when(roleService.createRole(Mockito.any())).thenThrow(RoleAlreadyExistsException.class);
        mockMvc.perform(
                        post("/roles/create")
                                .with(jwt().jwt(jwt -> jwt.claim("sub", keycloakId)))
                                .contentType(org.springframework.http.MediaType.APPLICATION_JSON)
                                .content(body))
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateRoleSuccessTest() throws Exception {
        Long workspaceId = snowflakeGenerator.next();
        RoleDto role = new RoleDto();
        role.setWorkspaceId(workspaceId.toString());
        role.setId(snowflakeGenerator.next().toString());
        role.setAdministrator(true);
        role.setName("Administrator");

        String body = objectMapper.writeValueAsString(role);
        String keycloakId = UUID.randomUUID().toString();

        Mockito.when(roleService.updateRole(Mockito.any())).thenReturn(role);
        mockMvc.perform(
                        put("/roles/update")
                                .with(jwt().jwt(jwt -> jwt.claim("sub", keycloakId)))
                                .contentType(org.springframework.http.MediaType.APPLICATION_JSON)
                                .content(body))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(role.getId()))
                .andExpect(jsonPath("$.workspaceId").value(role.getWorkspaceId()))
                .andExpect(jsonPath("$.name").value(role.getName()))
                .andExpect(jsonPath("$.administrator").value(role.isAdministrator()));
    }

    @Test
    void updateRoleDoesNotExistTest() throws Exception {
        Long workspaceId = snowflakeGenerator.next();
        RoleDto role = new RoleDto();
        role.setWorkspaceId(workspaceId.toString());
        role.setId(snowflakeGenerator.next().toString());
        role.setAdministrator(true);
        role.setName("Administrator");

        String body = objectMapper.writeValueAsString(role);
        String keycloakId = UUID.randomUUID().toString();

        Mockito.when(roleService.updateRole(Mockito.any())).thenThrow(RoleNotFoundException.class);
        mockMvc.perform(
                        put("/roles/update")
                                .with(jwt().jwt(jwt -> jwt.claim("sub", keycloakId)))
                                .contentType(org.springframework.http.MediaType.APPLICATION_JSON)
                                .content(body))
                .andExpect(status().isBadRequest());
    }

    @Test
    void deleteRoleTest() throws Exception{
        Long id = snowflakeGenerator.next();
        String keycloakId = UUID.randomUUID().toString();

        Mockito.doNothing().when(roleService).deleteRole(id);

        mockMvc.perform(
                        delete("/roles/delete/{roleId}", id)
                                .with(jwt().jwt(jwt -> jwt.claim("sub", keycloakId))))
                .andExpect(status().isNoContent());

    }

    @Test
    void getRolesTest() throws Exception{
        Long workspaceId = snowflakeGenerator.next();
        RoleDto role = new RoleDto();
        role.setWorkspaceId(workspaceId.toString());
        role.setId(snowflakeGenerator.next().toString());
        role.setAdministrator(true);
        role.setName("Administrator");

        String keycloakId = UUID.randomUUID().toString();
        Mockito.when(roleService.getRoles(workspaceId)).thenReturn(List.of(role));

        mockMvc.perform(
                        get("/roles/workspace/{workspaceId}", workspaceId)
                                .with(jwt().jwt(jwt -> jwt.claim("sub", keycloakId))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(role.getId()))
                .andExpect(jsonPath("$[0].workspaceId").value(role.getWorkspaceId()))
                .andExpect(jsonPath("$[0].name").value(role.getName()))
                .andExpect(jsonPath("$[0].administrator").value(role.isAdministrator()));
    }

}
