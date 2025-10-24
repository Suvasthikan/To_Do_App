-- Create database if it doesn't exist
CREATE DATABASE IF NOT EXISTS tododb;

USE tododb;

-- Create task table
CREATE TABLE IF NOT EXISTS task (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    description VARCHAR(1000) NOT NULL,
    completed BOOLEAN NOT NULL DEFAULT FALSE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_completed_created (completed, created_at DESC),
    INDEX idx_created (created_at DESC)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Insert sample data
INSERT INTO task (title, description, completed, created_at) VALUES
('Buy books', 'Buy books for the next school year', false, NOW() - INTERVAL 1 HOUR),
('Clean home', 'Need to clean the bed room', false, NOW() - INTERVAL 2 HOUR),
('Takehome assignment', 'Finish the mid-term assignment', false, NOW() - INTERVAL 3 HOUR),
('Play Cricket', 'Plan the soft ball cricket match on next Sunday', false, NOW() - INTERVAL 4 HOUR),
('Help Saman', 'Saman need help with his software project', false, NOW() - INTERVAL 5 HOUR);

