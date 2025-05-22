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