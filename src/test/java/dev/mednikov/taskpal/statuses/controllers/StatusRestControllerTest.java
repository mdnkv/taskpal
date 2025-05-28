package dev.mednikov.taskpal.statuses.controllers;

import cn.hutool.core.lang.generator.SnowflakeGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.mednikov.taskpal.statuses.domain.StatusDto;
import dev.mednikov.taskpal.statuses.exceptions.StatusNotFoundException;
import dev.mednikov.taskpal.statuses.services.StatusService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.UUID;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(StatusRestController.class)
class StatusRestControllerTest {

    private final static SnowflakeGenerator snowflakeGenerator = new SnowflakeGenerator();

    @Autowired private MockMvc mockMvc;
    @Autowired private ObjectMapper objectMapper;

    @MockitoBean private StatusService statusService;

    @Test
    void createStatusTest() throws Exception {
        Long workspaceId = snowflakeGenerator.next();
        Long statusId = snowflakeGenerator.next();
        StatusDto payload = new StatusDto();
        payload.setName("In Progress");
        payload.setWorkspaceId(workspaceId.toString());

        StatusDto result = new StatusDto();
        result.setName("In Progress");
        result.setWorkspaceId(workspaceId.toString());
        result.setId(statusId.toString());

        String body = objectMapper.writeValueAsString(payload);
        String keycloakId = UUID.randomUUID().toString();

        Mockito.when(statusService.createStatus(Mockito.any())).thenReturn(result);
        mockMvc.perform(
                        post("/statuses/create")
                                .with(jwt().jwt(jwt -> jwt.claim("sub", keycloakId)))
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(body))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(statusId.toString()))
                .andExpect(jsonPath("$.workspaceId").value(workspaceId.toString()))
                .andExpect(jsonPath("$.name").value("In Progress"));
    }

    @Test
    void updateStatus_notFoundTest() throws Exception{
        Long statusId = snowflakeGenerator.next();
        StatusDto payload = new StatusDto();
        payload.setName("In Progress");
        payload.setId(statusId.toString());
        payload.setWorkspaceId(snowflakeGenerator.next().toString());

        String body = objectMapper.writeValueAsString(payload);
        String keycloakId = UUID.randomUUID().toString();

        Mockito.when(statusService.updateStatus(Mockito.any())).thenThrow(StatusNotFoundException.class);
        mockMvc.perform(
                        put("/statuses/update")
                                .with(jwt().jwt(jwt -> jwt.claim("sub", keycloakId)))
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(body))
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateStatus_successTest() throws Exception{
        Long workspaceId = snowflakeGenerator.next();
        Long statusId = snowflakeGenerator.next();
        StatusDto payload = new StatusDto();
        payload.setName("In Progress");
        payload.setWorkspaceId(workspaceId.toString());
        payload.setId(statusId.toString());

        StatusDto result = new StatusDto();
        result.setName("In Progress");
        result.setWorkspaceId(workspaceId.toString());
        result.setId(statusId.toString());

        String body = objectMapper.writeValueAsString(payload);
        String keycloakId = UUID.randomUUID().toString();

        Mockito.when(statusService.updateStatus(Mockito.any())).thenReturn(result);
        mockMvc.perform(
                        put("/statuses/update")
                                .with(jwt().jwt(jwt -> jwt.claim("sub", keycloakId)))
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(body))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(statusId.toString()))
                .andExpect(jsonPath("$.workspaceId").value(workspaceId.toString()))
                .andExpect(jsonPath("$.name").value("In Progress"));
    }

    @Test
    void deleteStatusTest() throws Exception{
        Long statusId = snowflakeGenerator.next();
        String keycloakId = UUID.randomUUID().toString();
        Mockito.doNothing().when(statusService).deleteStatus(statusId);
        mockMvc.perform(
                        delete("/statuses/delete/{statusId}", statusId)
                                .with(jwt().jwt(jwt -> jwt.claim("sub", keycloakId))))
                .andExpect(status().isNoContent());
    }

    @Test
    void getStatusesTest() throws Exception{
        Long workspaceId = snowflakeGenerator.next();
        String keycloakId = UUID.randomUUID().toString();
        Mockito.when(statusService.getStatuses(workspaceId)).thenReturn(List.of());
        mockMvc.perform(
                        get("/statuses/workspace/{workspaceId}", workspaceId)
                                .with(jwt().jwt(jwt -> jwt.claim("sub", keycloakId))))
                .andExpect(status().isOk());
    }


}
