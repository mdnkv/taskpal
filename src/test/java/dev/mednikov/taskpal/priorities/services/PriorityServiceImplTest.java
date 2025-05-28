package dev.mednikov.taskpal.priorities.services;

import cn.hutool.core.lang.generator.SnowflakeGenerator;
import dev.mednikov.taskpal.priorities.domain.PriorityDto;
import dev.mednikov.taskpal.priorities.exceptions.PriorityNotFoundException;
import dev.mednikov.taskpal.priorities.models.Priority;
import dev.mednikov.taskpal.priorities.repositories.PriorityRepository;
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
class PriorityServiceImplTest {

    private final static SnowflakeGenerator snowflakeGenerator = new SnowflakeGenerator();

    @Mock private PriorityRepository priorityRepository;
    @Mock private WorkspaceRepository workspaceRepository;
    @InjectMocks private PriorityServiceImpl priorityService;

    @Test
    void createPriorityTest(){
        Long workspaceId = snowflakeGenerator.next();
        Workspace workspace = new Workspace();
        workspace.setId(workspaceId);
        workspace.setName("Lorenz GmbH & Co. KGaA");
        workspace.setPersonal(false);

        Priority priority = new Priority();
        priority.setWorkspace(workspace);
        priority.setId(snowflakeGenerator.next());
        priority.setUiOrder(1);
        priority.setName("High");

        PriorityDto payload = new PriorityDto();
        payload.setWorkspaceId(workspaceId.toString());
        payload.setName("High");
        payload.setUiOrder(1);

        Mockito.when(workspaceRepository.getReferenceById(workspaceId)).thenReturn(workspace);
        Mockito.when(priorityRepository.save(priority)).thenReturn(priority);

        PriorityDto result = priorityService.createPriority(payload);
        Assertions.assertThat(result).isNotNull();

    }

    @Test
    void updatePriority_notFoundTest(){
        Long priorityId = snowflakeGenerator.next();
        Long workspaceId = snowflakeGenerator.next();
        PriorityDto payload = new PriorityDto();
        payload.setId(priorityId.toString());
        payload.setWorkspaceId(workspaceId.toString());
        payload.setName("High");
        payload.setUiOrder(1);

        Mockito.when(priorityRepository.findById(priorityId)).thenReturn(Optional.empty());
        Assertions
                .assertThatThrownBy(() -> priorityService.updatePriority(payload))
                .isInstanceOf(PriorityNotFoundException.class);
    }

    @Test
    void updatePriority_successTest(){
        Long priorityId = snowflakeGenerator.next();
        Long workspaceId = snowflakeGenerator.next();

        PriorityDto payload = new PriorityDto();
        payload.setId(priorityId.toString());
        payload.setWorkspaceId(workspaceId.toString());
        payload.setName("High");
        payload.setUiOrder(1);

        Workspace workspace = new Workspace();
        workspace.setId(workspaceId);
        workspace.setName("Schmitz Decker KG");
        workspace.setPersonal(false);

        Priority priority = new Priority();
        priority.setId(priorityId);
        priority.setName("High");
        priority.setWorkspace(workspace);
        priority.setUiOrder(1);

        Mockito.when(priorityRepository.findById(priorityId)).thenReturn(Optional.of(priority));
        Mockito.when(priorityRepository.save(priority)).thenReturn(priority);

        PriorityDto result = priorityService.updatePriority(payload);
        Assertions.assertThat(result).isNotNull();
    }

    @Test
    void getPrioritiesTest(){
        Long workspaceId = snowflakeGenerator.next();
        Workspace workspace = new Workspace();
        workspace.setId(workspaceId);
        workspace.setName("Kuhn Ackermann GmbH & Co. OHG");
        workspace.setPersonal(false);

        List<Priority> priorities = new ArrayList<>();
        Priority high = new Priority();
        high.setWorkspace(workspace);
        high.setId(snowflakeGenerator.next());
        high.setUiOrder(1);
        high.setName("High");
        Priority medium = new Priority();
        medium.setWorkspace(workspace);
        medium.setId(snowflakeGenerator.next());
        medium.setUiOrder(2);
        medium.setName("Medium");
        Priority low = new Priority();
        low.setWorkspace(workspace);
        low.setId(snowflakeGenerator.next());
        low.setUiOrder(3);
        low.setName("Low");
        priorities.add(high);
        priorities.add(medium);
        priorities.add(low);

        Mockito.when(priorityRepository.findAllByWorkspaceId(workspaceId)).thenReturn(priorities);
        List<PriorityDto> result = priorityService.getPriorities(workspaceId);
        Assertions.assertThat(result).isNotNull();
        Assertions.assertThat(result).hasSize(3);

    }


}
