package dev.mednikov.taskpal.workspaces.services;

import cn.hutool.core.lang.generator.SnowflakeGenerator;
import dev.mednikov.taskpal.workspaces.domain.WorkspaceDto;
import dev.mednikov.taskpal.workspaces.exceptions.WorkspaceNotFoundException;
import dev.mednikov.taskpal.workspaces.models.Workspace;
import dev.mednikov.taskpal.workspaces.repositories.WorkspaceRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class WorkspaceServiceImplTest {

    private final static SnowflakeGenerator snowflakeGenerator = new SnowflakeGenerator();

    @Mock private WorkspaceRepository workspaceRepository;
    @InjectMocks private WorkspaceServiceImpl workspaceService;

    @Test
    void createWorkspaceTest(){
        Workspace workspace = new Workspace();
        workspace.setId(snowflakeGenerator.next());
        workspace.setName("My workspace");
        workspace.setPersonal(true);

        WorkspaceDto payload = new WorkspaceDto();
        payload.setName("My workspace");
        payload.setPersonal(true);

        Mockito.when(workspaceRepository.save(workspace)).thenReturn(workspace);

        WorkspaceDto result = workspaceService.createWorkspace(payload);
        Assertions.assertThat(result).isNotNull();
    }

    @Test
    void updateWorkspace_notFoundTest(){
        Long id = snowflakeGenerator.next();
        WorkspaceDto payload = new WorkspaceDto();
        payload.setId(id.toString());
        payload.setName("My workspace");
        payload.setPersonal(true);

        Mockito.when(workspaceRepository.findById(id)).thenReturn(Optional.empty());

        Assertions
                .assertThatThrownBy(() -> workspaceService.updateWorkspace(payload))
                .isInstanceOf(WorkspaceNotFoundException.class);

    }

    @Test
    void updateWorkspace_foundTest(){
        Long id = snowflakeGenerator.next();
        WorkspaceDto payload = new WorkspaceDto();
        payload.setId(id.toString());
        payload.setName("My workspace");
        payload.setPersonal(true);

        Workspace workspace = new Workspace();
        workspace.setId(id);
        workspace.setName("My workspace");
        workspace.setPersonal(true);

        Mockito.when(workspaceRepository.findById(id)).thenReturn(Optional.of(workspace));
        Mockito.when(workspaceRepository.save(workspace)).thenReturn(workspace);

        WorkspaceDto result = workspaceService.updateWorkspace(payload);
        Assertions.assertThat(result).isNotNull();
    }

    @Test
    void getWorkspace_existsTest(){
        Long id = snowflakeGenerator.next();
        Workspace workspace = new Workspace();
        workspace.setId(id);
        workspace.setName("My workspace");
        workspace.setPersonal(true);

        Mockito.when(workspaceRepository.findById(id)).thenReturn(Optional.of(workspace));

        Optional<WorkspaceDto> result = workspaceService.getWorkspace(id);
        Assertions.assertThat(result).isPresent();
    }

    @Test
    void getWorkspace_doesNotExistTest(){
        Long id = snowflakeGenerator.next();

        Mockito.when(workspaceRepository.findById(id)).thenReturn(Optional.empty());

        Optional<WorkspaceDto> result = workspaceService.getWorkspace(id);
        Assertions.assertThat(result).isNotPresent();
    }

}
