package dev.mednikov.taskpal.priorities.controllers;

import cn.hutool.core.lang.generator.SnowflakeGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.mednikov.taskpal.priorities.domain.PriorityDto;
import dev.mednikov.taskpal.priorities.exceptions.PriorityNotFoundException;
import dev.mednikov.taskpal.priorities.services.PriorityService;
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

@WebMvcTest(PriorityRestController.class)
class PriorityRestControllerTest {

    private final static SnowflakeGenerator snowflakeGenerator = new SnowflakeGenerator();

    @Autowired private MockMvc mockMvc;
    @Autowired private ObjectMapper objectMapper;
    @MockitoBean private PriorityService priorityService;

    @Test
    void createPriorityTest() throws Exception {
        Long workspaceId = snowflakeGenerator.next();
        Long priorityId = snowflakeGenerator.next();
        PriorityDto payload = new PriorityDto();
        payload.setName("High");
        payload.setWorkspaceId(workspaceId.toString());
        payload.setUiOrder(1);

        PriorityDto result = new PriorityDto();
        result.setName("High");
        result.setUiOrder(1);
        result.setWorkspaceId(workspaceId.toString());
        result.setId(priorityId.toString());

        String keycloakId = UUID.randomUUID().toString();
        String body = objectMapper.writeValueAsString(payload);

        Mockito.when(priorityService.createPriority(Mockito.any())).thenReturn(result);

        mockMvc.perform(
                        post("/priorities/create")
                                .with(jwt().jwt(jwt -> jwt.claim("sub", keycloakId)))
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(body))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(priorityId.toString()))
                .andExpect(jsonPath("$.workspaceId").value(workspaceId.toString()))
                .andExpect(jsonPath("$.uiOrder").value(1))
                .andExpect(jsonPath("$.name").value("High"));
    }

    @Test
    void updatePriority_notFoundTest() throws Exception {
        Long workspaceId = snowflakeGenerator.next();
        Long priorityId = snowflakeGenerator.next();
        PriorityDto payload = new PriorityDto();
        payload.setName("High");
        payload.setWorkspaceId(workspaceId.toString());
        payload.setUiOrder(1);
        payload.setId(priorityId.toString());

        String keycloakId = UUID.randomUUID().toString();
        String body = objectMapper.writeValueAsString(payload);

        Mockito.when(priorityService.updatePriority(Mockito.any())).thenThrow(PriorityNotFoundException.class);

        mockMvc.perform(
                        put("/priorities/update")
                                .with(jwt().jwt(jwt -> jwt.claim("sub", keycloakId)))
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(body))
                .andExpect(status().isBadRequest());
    }

    @Test
    void updatePriority_successTest() throws Exception {
        Long workspaceId = snowflakeGenerator.next();
        Long priorityId = snowflakeGenerator.next();
        PriorityDto payload = new PriorityDto();
        payload.setName("High");
        payload.setWorkspaceId(workspaceId.toString());
        payload.setUiOrder(1);
        payload.setId(priorityId.toString());

        PriorityDto result = new PriorityDto();
        result.setName("High");
        result.setWorkspaceId(workspaceId.toString());
        result.setUiOrder(1);
        result.setId(priorityId.toString());

        String keycloakId = UUID.randomUUID().toString();
        String body = objectMapper.writeValueAsString(payload);

        Mockito.when(priorityService.updatePriority(Mockito.any())).thenReturn(result);

        mockMvc.perform(
                        put("/priorities/update")
                                .with(jwt().jwt(jwt -> jwt.claim("sub", keycloakId)))
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(body))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(priorityId.toString()))
                .andExpect(jsonPath("$.workspaceId").value(workspaceId.toString()))
                .andExpect(jsonPath("$.uiOrder").value(1))
                .andExpect(jsonPath("$.name").value("High"));
    }

    @Test
    void deletePriorityTest() throws Exception {
        Long priorityId = snowflakeGenerator.next();
        String keycloakId = UUID.randomUUID().toString();
        Mockito.doNothing().when(priorityService).deletePriority(priorityId);
        mockMvc.perform(
                        delete("/priorities/delete/{priorityId}", priorityId)
                                .with(jwt().jwt(jwt -> jwt.claim("sub", keycloakId))))
                .andExpect(status().isNoContent());
    }

    @Test
    void getPrioritiesTest() throws Exception {
        Long workspaceId = snowflakeGenerator.next();
        String keycloakId = UUID.randomUUID().toString();
        List<PriorityDto> priorities = List.of();
        Mockito.when(priorityService.getPriorities(workspaceId)).thenReturn(priorities);
        mockMvc.perform(
                        get("/priorities/workspace/{workspaceId}", workspaceId)
                                .with(jwt().jwt(jwt -> jwt.claim("sub", keycloakId))))
                .andExpect(status().isOk());
    }


}
