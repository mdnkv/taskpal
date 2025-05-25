package dev.mednikov.taskpal.workspaces.domain;

import dev.mednikov.taskpal.roles.domain.RoleDto;

public final class WorkspaceUserDto {

    private String id;
    private RoleDto role;
    private WorkspaceDto workspace;
    private boolean active;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public RoleDto getRole() {
        return role;
    }

    public void setRole(RoleDto role) {
        this.role = role;
    }

    public WorkspaceDto getWorkspace() {
        return workspace;
    }

    public void setWorkspace(WorkspaceDto workspace) {
        this.workspace = workspace;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
