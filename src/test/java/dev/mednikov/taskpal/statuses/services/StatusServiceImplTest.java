package dev.mednikov.taskpal.statuses.services;

import cn.hutool.core.lang.generator.SnowflakeGenerator;
import dev.mednikov.taskpal.statuses.domain.StatusDto;
import dev.mednikov.taskpal.statuses.exceptions.StatusNotFoundException;
import dev.mednikov.taskpal.statuses.models.Status;
import dev.mednikov.taskpal.statuses.repositories.StatusRepository;
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
class StatusServiceImplTest {

    private final static SnowflakeGenerator snowflakeGenerator = new SnowflakeGenerator();
    @Mock private StatusRepository statusRepository;
    @Mock private WorkspaceRepository workspaceRepository;
    @InjectMocks private StatusServiceImpl statusService;

    @Test
    void createStatusTest(){
        Long workspaceId = snowflakeGenerator.next();
        Workspace workspace = new Workspace();
        workspace.setId(workspaceId);
        workspace.setName("Baur Riedl AG & Co. KG");
        workspace.setPersonal(false);

        Status status = new Status();
        status.setId(snowflakeGenerator.next());
        status.setName("In Progress");
        status.setWorkspace(workspace);

        Mockito.when(workspaceRepository.getReferenceById(workspaceId)).thenReturn(workspace);
        Mockito.when(statusRepository.save(status)).thenReturn(status);

        StatusDto payload = new StatusDto();
        payload.setName("In Progress");
        payload.setWorkspaceId(workspaceId.toString());

        StatusDto result = statusService.createStatus(payload);
        Assertions.assertThat(result).isNotNull();

    }

    @Test
    void updateStatus_notFoundTest(){
        Long workspaceId = snowflakeGenerator.next();
        Long statusId = snowflakeGenerator.next();
        StatusDto payload = new StatusDto();
        payload.setId(statusId.toString());
        payload.setName("In Progress");
        payload.setWorkspaceId(workspaceId.toString());

        Mockito.when(statusRepository.findById(statusId)).thenReturn(Optional.empty());
        Assertions
                .assertThatThrownBy(() -> statusService.updateStatus(payload))
                .isInstanceOf(StatusNotFoundException.class);
    }

    @Test
    void updateStatus_successTest(){
        Long workspaceId = snowflakeGenerator.next();
        Long statusId = snowflakeGenerator.next();
        Workspace workspace = new Workspace();
        workspace.setId(workspaceId);
        workspace.setName("Hahn Pfeifer Stiftung & Co. KGaA");
        workspace.setPersonal(false);

        Status status = new Status();
        status.setId(statusId);
        status.setName("In Progress");
        status.setWorkspace(workspace);

        StatusDto payload = new StatusDto();
        payload.setId(statusId.toString());
        payload.setName("In Progress");
        payload.setWorkspaceId(workspaceId.toString());

        Mockito.when(statusRepository.findById(statusId)).thenReturn(Optional.of(status));
        Mockito.when(statusRepository.save(status)).thenReturn(status);

        StatusDto result = statusService.updateStatus(payload);
        Assertions.assertThat(result).isNotNull();
    }

    @Test
    void getStatusesTest(){
        Long workspaceId = snowflakeGenerator.next();
        Workspace workspace = new Workspace();
        workspace.setId(workspaceId);
        workspace.setName("Stock Springer AG & Co. KG");
        workspace.setPersonal(false);

        List<Status> statuses = new ArrayList<>();
        Status created = new Status();
        created.setId(snowflakeGenerator.next());
        created.setName("Created");
        created.setWorkspace(workspace);
        statuses.add(created);
        Status inProgress = new Status();
        inProgress.setId(snowflakeGenerator.next());
        inProgress.setName("In Progress");
        inProgress.setWorkspace(workspace);
        statuses.add(inProgress);
        Status done = new Status();
        done.setId(snowflakeGenerator.next());
        done.setName("Done");
        done.setWorkspace(workspace);
        statuses.add(done);
        Status waiting = new Status();
        waiting.setId(snowflakeGenerator.next());
        waiting.setName("Waiting feedback");
        waiting.setWorkspace(workspace);
        statuses.add(waiting);

        Mockito.when(statusRepository.findAllByWorkspaceId(workspaceId)).thenReturn(statuses);
        List<StatusDto> result = statusService.getStatuses(workspaceId);
        Assertions.assertThat(result).isNotNull();
        Assertions.assertThat(result).hasSize(4);

    }

}
