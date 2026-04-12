-- V4__seed_data.sql

-- Insert user (password = "password123")
-- BCrypt hash for "password123"
INSERT INTO users (id, name, email, password)
VALUES (
    '11111111-1111-1111-1111-111111111111',
    'Test User',
    'test@taskflow.com',
    '$2a$12$Oz4zXyu1/esGO2u7JdRcZ.xv0.y54u/2yqKp04bNSf9P488Xmdr.m'
);

-- Insert project
INSERT INTO projects (id, name, description, owner_id)
VALUES (
    '22222222-2222-2222-2222-222222222222',
    'Demo Project',
    'Sample project for testing',
    '11111111-1111-1111-1111-111111111111'
);

-- Insert tasks
INSERT INTO tasks (title, description, status, priority, project_id, assignee_id)
VALUES
(
    'Task 1',
    'First task',
    'todo',
    'low',
    '22222222-2222-2222-2222-222222222222',
    '11111111-1111-1111-1111-111111111111'
),
(
    'Task 2',
    'Second task',
    'in_progress',
    'medium',
    '22222222-2222-2222-2222-222222222222',
    '11111111-1111-1111-1111-111111111111'
),
(
    'Task 3',
    'Third task',
    'done',
    'high',
    '22222222-2222-2222-2222-222222222222',
    '11111111-1111-1111-1111-111111111111'
);