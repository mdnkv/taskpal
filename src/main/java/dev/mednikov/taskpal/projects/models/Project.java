package dev.mednikov.taskpal.projects.models;

import dev.mednikov.taskpal.statuses.models.Status;
import dev.mednikov.taskpal.workspaces.models.Workspace;
import jakarta.persistence.*;
import org.hibernate.annotations.CurrentTimestamp;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.Optional;

@Entity
@Table(name = "projects_project")
public class Project {

    @Id
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "workspace_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Workspace workspace;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "status_id")
    @OnDelete(action = OnDeleteAction.SET_NULL)
    private Status status;

    @Column(nullable = false)
    private String name;

    @CurrentTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Override
    public final boolean equals(Object o) {
        if (!(o instanceof Project project)) return false;

        return workspace.equals(project.workspace) && name.equals(project.name);
    }

    @Override
    public int hashCode() {
        int result = workspace.hashCode();
        result = 31 * result + name.hashCode();
        return result;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Workspace getWorkspace() {
        return workspace;
    }

    public void setWorkspace(Workspace workspace) {
        this.workspace = workspace;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Optional<Status> getStatus() {
        return Optional.ofNullable(status);
    }

}
