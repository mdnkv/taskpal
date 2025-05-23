package dev.mednikov.taskpal.roles.services;

import dev.mednikov.taskpal.roles.domain.RoleDto;

import java.util.List;

public interface RoleService {

    RoleDto createRole (RoleDto roleDto);

    RoleDto updateRole (RoleDto roleDto);

    void deleteRole (Long roleId);

    List<RoleDto> getRoles (Long workspaceId);

}
