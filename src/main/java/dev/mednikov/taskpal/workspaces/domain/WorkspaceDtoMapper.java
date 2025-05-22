package dev.mednikov.taskpal.workspaces.domain;

import dev.mednikov.taskpal.workspaces.models.Workspace;

import java.util.function.Function;

public final class WorkspaceDtoMapper implements Function<Workspace, WorkspaceDto> {

    @Override
    public WorkspaceDto apply(Workspace workspace) {
        WorkspaceDto result = new WorkspaceDto();
        result.setId(workspace.getId().toString());
        result.setName(workspace.getName());
        result.setPersonal(workspace.isPersonal());
        return result;
    }
}
