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
    CONSTRAINT fk_roles_workspace
        FOREIGN KEY (workspace_id)
            REFERENCES workspaces_workspace(id) ON DELETE CASCADE
);