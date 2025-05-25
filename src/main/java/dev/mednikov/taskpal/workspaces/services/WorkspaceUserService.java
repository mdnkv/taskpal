package dev.mednikov.taskpal.workspaces.services;

import dev.mednikov.taskpal.users.models.User;
import dev.mednikov.taskpal.workspaces.domain.CreateWorkspaceUserRequestDto;
import dev.mednikov.taskpal.workspaces.domain.WorkspaceUserDto;

import java.util.List;
import java.util.Optional;

public interface WorkspaceUserService {

    Optional<WorkspaceUserDto> getActiveWorkspace (User user);

    WorkspaceUserDto setActiveWorkspace (User user, Long workspaceId);

    WorkspaceUserDto createWorkspaceUser(CreateWorkspaceUserRequestDto request);

    List<WorkspaceUserDto> getUserWorkspaces (User user);

}
