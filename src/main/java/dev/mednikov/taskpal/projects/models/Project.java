package dev.mednikov.taskpal.projects.models;

import dev.mednikov.taskpal.workspaces.models.Workspace;
import jakarta.persistence.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Table(name = "projects_project")
public class Project {

    @Id
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "workspace_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Workspace workspace;

    @Column(nullable = false)
    private String name;

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
}
