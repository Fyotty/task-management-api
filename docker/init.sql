-- Task Management Database Initialization Script

-- Create database if not exists
CREATE DATABASE IF NOT EXISTS taskmanagement;

-- Use the database
\c taskmanagement;

-- Create extensions
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

-- Insert initial users
INSERT INTO users (id, name, email, created_at) VALUES 
    (uuid_generate_v4(), 'John Doe', 'john.doe@email.com', CURRENT_TIMESTAMP),
    (uuid_generate_v4(), 'Jane Smith', 'jane.smith@email.com', CURRENT_TIMESTAMP),
    (uuid_generate_v4(), 'Bob Johnson', 'bob.johnson@email.com', CURRENT_TIMESTAMP)
ON CONFLICT (email) DO NOTHING;

-- Insert sample tasks
WITH user_ids AS (
    SELECT id, name FROM users LIMIT 3
)
INSERT INTO tasks (id, title, description, status, created_at, user_id)
SELECT 
    uuid_generate_v4(),
    'Sample Task ' || ROW_NUMBER() OVER(),
    'This is a sample task for demonstration purposes',
    'PENDING',
    CURRENT_TIMESTAMP,
    u.id
FROM user_ids u;

-- Insert sample subtasks
WITH task_ids AS (
    SELECT id, title FROM tasks LIMIT 2
)
INSERT INTO subtasks (id, title, description, status, created_at, task_id)
SELECT 
    uuid_generate_v4(),
    'Sample Subtask ' || ROW_NUMBER() OVER(),
    'This is a sample subtask',
    'PENDING',
    CURRENT_TIMESTAMP,
    t.id
FROM task_ids t;
