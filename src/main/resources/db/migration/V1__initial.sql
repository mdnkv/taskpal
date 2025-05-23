CREATE TABLE IF NOT EXISTS workspaces_workspace (
    id BIGINT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    is_personal BOOLEAN NOT NULL
);

CREATE TABLE IF NOT EXISTS users_user (
    id BIGINT PRIMARY KEY,
    keycloak_id VARCHAR(100) UNIQUE NOT NULL,
    email VARCHAR(255) NOT NULL,
    first_name VARCHAR(255),
    last_name VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS roles_role (
    id BIGINT PRIMARY KEY,
    workspace_id BIGINT NOT NULL,
    is_administrator BOOLEAN NOT NULL,
    name VARCHAR(255) NOT NULL,
    UNIQUE (workspace_id, name),
    CONSTRAINT fk_role_workspace
        FOREIGN KEY (workspace_id)
            REFERENCES workspaces_workspace(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS projects_project (
    id BIGINT PRIMARY KEY,
    workspace_id BIGINT NOT NULL,
    name VARCHAR(255) NOT NULL,
    CONSTRAINT fk_project_workspace
        FOREIGN KEY (workspace_id)
            REFERENCES workspaces_workspace(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS tasks_task (
    id BIGINT PRIMARY KEY,
    workspace_id BIGINT NOT NULL,
    project_id BIGINT NOT NULL,
    title VARCHAR(255) NOT NULL,
    description TEXT,
    CONSTRAINT fk_task_workspace
        FOREIGN KEY (workspace_id)
            REFERENCES workspaces_workspace(id) ON DELETE CASCADE,
    CONSTRAINT fk_task_project
        FOREIGN KEY (project_id)
            REFERENCES projects_project(id) ON DELETE CASCADE
);