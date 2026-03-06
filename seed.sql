USE `komandomobiledb`;

SET FOREIGN_KEY_CHECKS = 0;

TRUNCATE TABLE task_users;
TRUNCATE TABLE tasks;
TRUNCATE TABLE group_users;
TRUNCATE TABLE groupdata;
TRUNCATE TABLE devices;
TRUNCATE TABLE users;

SET FOREIGN_KEY_CHECKS = 1;
-- ---------------------------
-- Users
-- ---------------------------
INSERT INTO `users` (`first_name`, `last_name`, `username`, `password`)
VALUES 
('Test', 'User', 'testuser', '$2a$10$0YU3lSbE40LpGqP0zPKGb.AxOH5GXAwwlBccmTSiLT2z/yn6ayyQS'),
('Maria', 'Lopez', 'maria', '$2a$10$dz.gJV.gc1Dk4Eu6d.lebeENt7VZCnKz7l.B7MopDQlpXYGav/Ry.'),
('John', 'Doe', 'john', '$2a$10$//ikVrmM4gxjpAGkRYoCUu2NXaLjJxPZaG17u4yGFDBWv1mIElXHW'),
('Brian','Santos','brian','$2a$10$0YU3lSbE40LpGqP0zPKGb.AxOH5GXAwwlBccmTSiLT2z/yn6ayyQS'),
('Carla','Mendoza','carla','$2a$10$0YU3lSbE40LpGqP0zPKGb.AxOH5GXAwwlBccmTSiLT2z/yn6ayyQS'),
('David','Lim','david','$2a$10$0YU3lSbE40LpGqP0zPKGb.AxOH5GXAwwlBccmTSiLT2z/yn6ayyQS'),
('Ella','Cruz','ella','$2a$10$0YU3lSbE40LpGqP0zPKGb.AxOH5GXAwwlBccmTSiLT2z/yn6ayyQS'),
('Frank','Garcia','frank','$2a$10$0YU3lSbE40LpGqP0zPKGb.AxOH5GXAwwlBccmTSiLT2z/yn6ayyQS'),
('Grace','Torres','grace','$2a$10$0YU3lSbE40LpGqP0zPKGb.AxOH5GXAwwlBccmTSiLT2z/yn6ayyQS');

-- ---------------------------
-- Groups
-- ---------------------------
INSERT INTO groupdata (name, created_by_id) VALUES
('Backend Team', 1),
('Frontend Team', 2),
('QA Team', 3),
('DevOps Team', 4),
('Mobile Team', 5);


-- ---------------------------
-- Group-Users mapping
-- ---------------------------
INSERT INTO group_users (group_id, user_id) VALUES
(1, 1),
(1, 2),
(1, 7);

-- Frontend Team
INSERT INTO group_users (group_id, user_id) VALUES
(2, 2),
(2, 4),
(2, 6);

-- QA Team
INSERT INTO group_users (group_id, user_id) VALUES
(3, 3),
(3, 5),
(3, 8);

-- DevOps Team
INSERT INTO group_users (group_id, user_id) VALUES
(4, 4),
(4, 5),
(4, 9);

-- Mobile Team
INSERT INTO group_users (group_id, user_id) VALUES
(5, 1),
(5, 6),
(5, 9);

-- ---------------------------
-- Tasks
-- ---------------------------
-- Backend Team tasks (7 tasks)
INSERT INTO tasks (title, description, status, created_by_id, group_id, created_at, updated_at) VALUES
('Setup Authentication', 'Implement JWT login system', 'PENDING', 1, 1, NOW(), NOW()),
('Database Schema', 'Design initial MySQL schema', 'IN_PROGRESS', 1, 1, NOW(), NOW()),
('API Endpoints', 'Create REST endpoints for users', 'PENDING', 2, 1, NOW(), NOW()),
('Backend CI/CD', 'Setup GitHub Actions for backend', 'PENDING', 1, 1, NOW(), NOW()),
('Service Layer', 'Implement service classes', 'IN_PROGRESS', 2, 1, NOW(), NOW()),
('Repository Layer', 'Implement JPA repositories', 'PENDING', 1, 1, NOW(), NOW()),
('Error Handling', 'Global exception handling', 'PENDING', 1, 1, NOW(), NOW());

-- Frontend Team tasks (4 tasks)
INSERT INTO tasks (title, description, status, created_by_id, group_id, created_at, updated_at) VALUES
('Landing Page', 'Create main landing page', 'PENDING', 2, 2, NOW(), NOW()),
('User Dashboard', 'Design dashboard UI', 'IN_PROGRESS', 4, 2, NOW(), NOW()),
('Forms Validation', 'Implement frontend validation', 'PENDING', 6, 2, NOW(), NOW()),
('API Integration', 'Connect frontend with backend API', 'PENDING', 2, 2, NOW(), NOW());

-- QA Team tasks (3 tasks)
INSERT INTO tasks (title, description, status, created_by_id, group_id, created_at, updated_at) VALUES
('Unit Testing', 'Write unit tests for backend', 'PENDING', 3, 3, NOW(), NOW()),
('Integration Testing', 'Test all APIs end-to-end', 'IN_PROGRESS', 3, 3, NOW(), NOW()),
('UI Testing', 'Test frontend UI functionality', 'PENDING', 8, 3, NOW(), NOW());

-- DevOps Team tasks (3 tasks)
INSERT INTO tasks (title, description, status, created_by_id, group_id, created_at, updated_at) VALUES
('Server Setup', 'Provision AWS EC2 instances', 'PENDING', 4, 4, NOW(), NOW()),
('Docker Setup', 'Containerize services', 'PENDING', 4, 4, NOW(), NOW()),
('Deployment Pipeline', 'Implement CI/CD pipelines', 'IN_PROGRESS', 5, 4, NOW(), NOW());

-- Mobile Team tasks (3 tasks)
INSERT INTO tasks (title, description, status, created_by_id, group_id, created_at, updated_at) VALUES
('Login Screen', 'Design login UI for mobile', 'PENDING', 5, 5, NOW(), NOW()),
('Push Notifications', 'Integrate FCM', 'IN_PROGRESS', 6, 5, NOW(), NOW()),
('Offline Mode', 'Enable offline caching', 'PENDING', 1, 5, NOW(), NOW());

-- ---------------------------
-- Task-Users mapping
-- ---------------------------
INSERT INTO task_users (task_id, user_id) VALUES
(1, 1), (1, 2),
(2, 1), (2, 7),
(3, 1), (3, 2),
(4, 1), (4, 2),
(5, 2), (5, 7),
(6, 1), (6, 7),
(7, 1), (7, 2);

-- Frontend Team task assignments
INSERT INTO task_users (task_id, user_id) VALUES
(8, 2), (8, 4),
(9, 4), (9, 6),
(10, 2), (10, 6),
(11, 2), (11, 4);

-- QA Team task assignments
INSERT INTO task_users (task_id, user_id) VALUES
(12, 3), (12, 5),
(13, 3), (13, 8),
(14, 3), (14, 8);

-- DevOps Team task assignments
INSERT INTO task_users (task_id, user_id) VALUES
(15, 4), (15, 5),
(16, 4), (16, 9),
(17, 4), (17, 5);

-- Mobile Team task assignments
INSERT INTO task_users (task_id, user_id) VALUES
(18, 5), (18, 6),
(19, 6), (19, 1),
(20, 1), (20, 6);

