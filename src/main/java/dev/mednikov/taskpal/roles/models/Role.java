package dev.mednikov.taskpal.roles.models;

import dev.mednikov.taskpal.workspaces.models.Workspace;
import jakarta.persistence.*;
import org.hibernate.annotations.CurrentTimestamp;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(
        name = "roles_role",
        uniqueConstraints = @UniqueConstraint(columnNames = {"workspace_id", "name"})
)
public class Role {

    @Id
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "workspace_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Workspace workspace;

    @Column(nullable = false)
    private String name;

    @Column(name = "is_administrator", nullable = false)
    private boolean administrator;

    @CurrentTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Override
    public final boolean equals(Object o) {
        if (!(o instanceof Role role)) return false;

        return administrator == role.administrator && workspace.equals(role.workspace) && name.equals(role.name);
    }

    @Override
    public int hashCode() {
        int result = workspace.hashCode();
        result = 31 * result + name.hashCode();
        result = 31 * result + Boolean.hashCode(administrator);
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

    public boolean isAdministrator() {
        return administrator;
    }

    public void setAdministrator(boolean administrator) {
        this.administrator = administrator;
    }
}
