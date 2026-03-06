SET FOREIGN_KEY_CHECKS = 0;

TRUNCATE TABLE task_users;
TRUNCATE TABLE tasks;
TRUNCATE TABLE group_users;
TRUNCATE TABLE groupdata;
TRUNCATE TABLE devices;
TRUNCATE TABLE users;

SET FOREIGN_KEY_CHECKS = 1;

INSERT INTO users (id, first_name, last_name, username, password)
VALUES 
(1,'Test','User','testuser','$2a$10$0YU3lSbE40LpGqP0zPKGb.AxOH5GXAwwlBccmTSiLT2z/yn6ayyQS'),
(2,'Maria','Lopez','maria','$2a$10$dz.gJV.gc1Dk4Eu6d.lebeENt7VZCnKz7l.B7MopDQlpXYGav/Ry.'),
(3,'John','Doe','john','$2a$10$//ikVrmM4gxjpAGkRYoCUu2NXaLjJxPZaG17u4yGFDBWv1mIElXHW');

INSERT INTO groupdata (id, name, created_by_id, created_at, updated_at)
VALUES
(1,'Backend Team',1,NOW(),NOW()),
(2,'Frontend Team',2,NOW(),NOW()),
(3,'QA Team',3,NOW(),NOW());

INSERT INTO group_users (group_id, user_id) VALUES
-- Group 1
(1,1),(1,2),(1,3),

-- Group 2
(2,1),(2,2),

-- Group 3
(3,1),(3,3);

-- ===========================
-- TASKS (10 total)
-- Group 1 = 4 tasks
-- Group 2 = 3 tasks
-- Group 3 = 3 tasks
-- ===========================
INSERT INTO tasks (id, created_at, description, status, title, updated_at, created_by_id, group_id) VALUES

-- Group 1 (Backend)
(1,NOW(),'Setup authentication','IN_PROGRESS','Auth Module',NOW(),1,1),
(2,NOW(),'Implement task API','PENDING','Task CRUD',NOW(),2,1),
(3,NOW(),'Database optimization','PENDING','DB Optimization',NOW(),3,1),
(4,NOW(),'Refactor services','COMPLETED','Service Refactor',NOW(),1,1),

-- Group 2 (Frontend)
(5,NOW(),'Design login page','IN_PROGRESS','Login UI',NOW(),2,2),
(6,NOW(),'Dashboard layout','PENDING','Dashboard UI',NOW(),1,2),
(7,NOW(),'API integration','PENDING','API Integration',NOW(),2,2),

-- Group 3 (QA)
(8,NOW(),'Write unit tests','IN_PROGRESS','Unit Testing',NOW(),3,3),
(9,NOW(),'Integration tests','PENDING','Integration Testing',NOW(),1,3),
(10,NOW(),'Bug verification','COMPLETED','Bug Validation',NOW(),3,3);

-- ===========================
-- TASK ASSIGNMENTS
-- ===========================
INSERT INTO task_users (task_id, user_id) VALUES

-- Group 1
(1,1),(1,2),
(2,2),
(3,3),
(4,1),

-- Group 2
(5,2),
(6,1),
(7,1),(7,2),

-- Group 3
(8,3),
(9,1),
(10,3);

