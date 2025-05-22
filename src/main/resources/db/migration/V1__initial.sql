CREATE TABLE IF NOT EXISTS workspaces_workspace (
    id BIGINT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    is_personal BOOLEAN NOT NULL
);