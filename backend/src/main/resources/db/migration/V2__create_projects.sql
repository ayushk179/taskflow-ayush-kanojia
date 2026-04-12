-- V2__create_projects.sql

CREATE TABLE projects (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    name TEXT NOT NULL,
    description TEXT,
    owner_id UUID NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_project_owner
        FOREIGN KEY (owner_id)
        REFERENCES users(id)
        ON DELETE CASCADE
);