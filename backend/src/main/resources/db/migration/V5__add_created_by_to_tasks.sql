-- V5__add_created_by_to_tasks.sql

ALTER TABLE tasks
ADD COLUMN created_by UUID;

-- First try assignee
UPDATE tasks
SET created_by = assignee_id
WHERE created_by IS NULL AND assignee_id IS NOT NULL;

-- fallback to project owner
UPDATE tasks
SET created_by = (
    SELECT owner_id FROM projects WHERE projects.id = tasks.project_id
)
WHERE created_by IS NULL;

ALTER TABLE tasks
ADD CONSTRAINT fk_task_creator
FOREIGN KEY (created_by)
REFERENCES users(id)
ON DELETE CASCADE;

ALTER TABLE tasks
ALTER COLUMN created_by SET NOT NULL;