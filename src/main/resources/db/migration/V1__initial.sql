CREATE TABLE IF NOT EXISTS workspaces_workspace (
    id BIGINT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    is_personal BOOLEAN NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS users_user (
    id BIGINT PRIMARY KEY,
    keycloak_id VARCHAR(100) UNIQUE NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    first_name VARCHAR(255),
    last_name VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS roles_role (
    id BIGINT PRIMARY KEY,
    workspace_id BIGINT NOT NULL,
    is_administrator BOOLEAN NOT NULL,
    name VARCHAR(255) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UNIQUE (workspace_id, name),
    CONSTRAINT fk_role_workspace
        FOREIGN KEY (workspace_id)
            REFERENCES workspaces_workspace(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS workspaces_user (
    id BIGINT PRIMARY KEY,
    role_id BIGINT NOT NULL,
    workspace_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL,
    is_active BOOLEAN NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UNIQUE (workspace_id, user_id),
    CONSTRAINT fk_workspace_user_workspace
        FOREIGN KEY (workspace_id)
            REFERENCES workspaces_workspace(id) ON DELETE CASCADE,
    CONSTRAINT fk_workspace_user_user
        FOREIGN KEY (user_id)
            REFERENCES users_user(id) ON DELETE CASCADE,
    CONSTRAINT fk_workspace_user_role
        FOREIGN KEY (role_id)
            REFERENCES roles_role(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS projects_project (
    id BIGINT PRIMARY KEY,
    workspace_id BIGINT NOT NULL,
    name VARCHAR(255) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_project_workspace
        FOREIGN KEY (workspace_id)
            REFERENCES workspaces_workspace(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS priorities_priority (
    id BIGINT PRIMARY KEY,
    workspace_id BIGINT NOT NULL,
    name VARCHAR(100) NOT NULL,
    ui_order INTEGER DEFAULT 0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_priority_workspace
        FOREIGN KEY (workspace_id)
            REFERENCES workspaces_workspace(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS statuses_status (
    id BIGINT PRIMARY KEY,
    workspace_id BIGINT NOT NULL,
    name VARCHAR(100) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_status_workspace
        FOREIGN KEY (workspace_id)
            REFERENCES workspaces_workspace(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS tasks_task (
    id BIGINT PRIMARY KEY,
    workspace_id BIGINT NOT NULL,
    project_id BIGINT NOT NULL,
    priority_id BIGINT,
    status_id BIGINT,
    title VARCHAR(255) NOT NULL,
    description TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_task_workspace
        FOREIGN KEY (workspace_id)
            REFERENCES workspaces_workspace(id) ON DELETE CASCADE,
    CONSTRAINT fk_task_project
        FOREIGN KEY (project_id)
            REFERENCES projects_project(id) ON DELETE CASCADE,
    CONSTRAINT fk_task_status
        FOREIGN KEY (status_id)
            REFERENCES statuses_status(id) ON DELETE SET NULL,
    CONSTRAINT fk_task_priority
        FOREIGN KEY (priority_id)
            REFERENCES priorities_priority(id) ON DELETE SET NULL
);