package dev.mednikov.taskpal.workspaces.services;

import cn.hutool.core.lang.generator.SnowflakeGenerator;
import dev.mednikov.taskpal.workspaces.domain.WorkspaceDto;
import dev.mednikov.taskpal.workspaces.domain.WorkspaceDtoMapper;
import dev.mednikov.taskpal.workspaces.exceptions.WorkspaceNotFoundException;
import dev.mednikov.taskpal.workspaces.models.Workspace;
import dev.mednikov.taskpal.workspaces.repositories.WorkspaceRepository;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

@Service
public class WorkspaceServiceImpl implements WorkspaceService {

    private final static WorkspaceDtoMapper workspaceDtoMapper = new WorkspaceDtoMapper();
    private final static SnowflakeGenerator snowflakeGenerator = new SnowflakeGenerator();

    private final WorkspaceRepository workspaceRepository;

    public WorkspaceServiceImpl(WorkspaceRepository workspaceRepository) {
        this.workspaceRepository = workspaceRepository;
    }

    @Override
    public WorkspaceDto createWorkspace(WorkspaceDto workspaceDto) {
        Workspace workspace = new Workspace();
        workspace.setId(snowflakeGenerator.next());
        workspace.setName(workspaceDto.getName());
        workspace.setPersonal(workspaceDto.isPersonal());
        Workspace result = this.workspaceRepository.save(workspace);
        return workspaceDtoMapper.apply(result);
    }

    @Override
    public WorkspaceDto updateWorkspace(WorkspaceDto workspaceDto) {
        Objects.requireNonNull(workspaceDto.getId());
        Long workspaceId = Long.parseLong(workspaceDto.getId());
        Workspace workspace = this.workspaceRepository.findById(workspaceId).orElseThrow(WorkspaceNotFoundException::new);
        workspace.setName(workspaceDto.getName());
        workspace.setPersonal(workspaceDto.isPersonal());
        Workspace result = this.workspaceRepository.save(workspace);
        return workspaceDtoMapper.apply(result);
    }

    @Override
    public void deleteWorkspace(Long workspaceId) {
        this.workspaceRepository.deleteById(workspaceId);
    }

    @Override
    public Optional<WorkspaceDto> getWorkspace(Long workspaceId) {
        return this.workspaceRepository.findById(workspaceId).map(workspaceDtoMapper);
    }
}
