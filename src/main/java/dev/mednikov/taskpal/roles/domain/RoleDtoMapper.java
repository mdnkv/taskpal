package dev.mednikov.taskpal.roles.domain;

import dev.mednikov.taskpal.roles.models.Role;

import java.util.function.Function;

public final class RoleDtoMapper implements Function<Role, RoleDto> {

    @Override
    public RoleDto apply(Role role) {
        RoleDto result = new RoleDto();
        result.setName(role.getName());
        result.setAdministrator(role.isAdministrator());
        result.setId(role.getId().toString());
        result.setWorkspaceId(role.getWorkspace().getId().toString());
        return result;
    }

}
