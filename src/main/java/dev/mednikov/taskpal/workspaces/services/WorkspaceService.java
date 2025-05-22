package dev.mednikov.taskpal.workspaces.services;

import dev.mednikov.taskpal.workspaces.domain.WorkspaceDto;

import java.util.Optional;

public interface WorkspaceService {

    WorkspaceDto createWorkspace(WorkspaceDto workspaceDto);

    WorkspaceDto updateWorkspace(WorkspaceDto workspaceDto);

    void deleteWorkspace(Long workspaceId);

    Optional<WorkspaceDto> getWorkspace(Long workspaceId);

}
