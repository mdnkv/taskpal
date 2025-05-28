package dev.mednikov.taskpal.workspaces.events;

import dev.mednikov.taskpal.users.models.User;
import dev.mednikov.taskpal.workspaces.models.Workspace;
import org.springframework.context.ApplicationEvent;

public final class WorkspaceCreatedEvent extends ApplicationEvent {

    private final Long workspaceId;
    private final User owner;

    public WorkspaceCreatedEvent(Object source, Long workspaceId, User owner) {
        super(source);
        this.workspaceId = workspaceId;
        this.owner = owner;
    }

    public Long getWorkspaceId() {
        return workspaceId;
    }

    public User getOwner() {
        return owner;
    }

}
