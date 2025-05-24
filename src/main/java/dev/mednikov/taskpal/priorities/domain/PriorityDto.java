package dev.mednikov.taskpal.priorities.domain;

public final class PriorityDto {

    private String id;
    private String workspaceId;
    private String name;
    private int uiOrder;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getWorkspaceId() {
        return workspaceId;
    }

    public void setWorkspaceId(String workspaceId) {
        this.workspaceId = workspaceId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getUiOrder() {
        return uiOrder;
    }

    public void setUiOrder(int uiOrder) {
        this.uiOrder = uiOrder;
    }
}
