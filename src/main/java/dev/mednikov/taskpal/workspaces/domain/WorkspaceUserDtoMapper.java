package dev.mednikov.taskpal.workspaces.domain;

import dev.mednikov.taskpal.roles.domain.RoleDto;
import dev.mednikov.taskpal.roles.domain.RoleDtoMapper;
import dev.mednikov.taskpal.workspaces.models.WorkspaceUser;

import java.util.function.Function;

public final class WorkspaceUserDtoMapper implements Function<WorkspaceUser, WorkspaceUserDto> {

    private final static WorkspaceDtoMapper workspaceDtoMapper = new WorkspaceDtoMapper();
    private final static RoleDtoMapper roleDtoMapper = new RoleDtoMapper();

    @Override
    public WorkspaceUserDto apply(WorkspaceUser workspaceUser) {
        RoleDto roleDto = roleDtoMapper.apply(workspaceUser.getRole());
        WorkspaceDto workspaceDto = workspaceDtoMapper.apply(workspaceUser.getWorkspace());
        WorkspaceUserDto result = new WorkspaceUserDto();
        result.setId(workspaceUser.getId().toString());
        result.setRole(roleDto);
        result.setWorkspace(workspaceDto);
        result.setActive(workspaceUser.isActive());
        return result;
    }
}
