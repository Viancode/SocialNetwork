USE socialnetwork;

-- Insert a role
INSERT INTO roles (name) VALUES ('User');

-- Get the role_id of the newly inserted role
SET @role_id = LAST_INSERT_ID();

-- Insert a user
INSERT INTO users (username, email, password, first_name, last_name, visibility, role_id, bio, location, work, education, created_at, updated_at, avatar, background_image)
VALUES ('john_doe', 'john.doe@example.com', '$2a$10$LpjT9yWWy.HEaEj/MpSbbeGFdG7MkluyuYSNYCst8iGNWR5MmyOja', 'John', 'Doe', 'public', @role_id, 'Hello! I am John.', 'New York', 'Software Developer', 'MIT', NOW(), NOW(), 'avatar.png', 'background.png');