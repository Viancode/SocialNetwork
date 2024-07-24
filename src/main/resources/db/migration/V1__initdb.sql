USE socialnetwork;

CREATE TABLE roles (
                       role_id BIGINT PRIMARY KEY AUTO_INCREMENT,
                       name VARCHAR(255)
);

CREATE TABLE users (
                       user_id BIGINT PRIMARY KEY AUTO_INCREMENT,
                       username VARCHAR(255),
                       email VARCHAR(255),
                       password VARCHAR(255),
                       first_name VARCHAR(255),
                       last_name VARCHAR(255),
                        gender ENUM('MALE','FEMALE','OTHERS'),
                       visibility ENUM('PUBLIC', 'FRIEND', 'PRIVATE'),
                       role_id BIGINT,
                       bio VARCHAR(255),
                       location VARCHAR(255),
                       work VARCHAR(255),
                       education VARCHAR(255),
                       created_at DATETIME,
                       updated_at DATETIME,
                       avatar VARCHAR(255),
                       background_image VARCHAR(255),
                        date_of_birth DATE,
                       is_email_verified BOOLEAN default false,
                       CONSTRAINT fk_role FOREIGN KEY (role_id) REFERENCES roles(role_id)
);

CREATE TABLE relationships (
                        relationship_id BIGINT PRIMARY KEY AUTO_INCREMENT,
                         user_id BIGINT,
                         friend_id BIGINT,
                         created_at DATETIME,
                        relation ENUM('FRIEND', 'PENDING', 'BLOCK'),
                         CONSTRAINT fk_user FOREIGN KEY (user_id) REFERENCES users(user_id),
                         CONSTRAINT fk_friend FOREIGN KEY (friend_id) REFERENCES users(user_id)
);

CREATE TABLE posts (
                       post_id BIGINT PRIMARY KEY AUTO_INCREMENT,
                       user_id BIGINT,
                       content VARCHAR(255),
                       visibility ENUM('PUBLIC', 'FRIEND', 'PRIVATE'),
                       created_at DATETIME,
                       updated_at DATETIME,
                       is_deleted BIT(1),
                       photo_lists MEDIUMTEXT,
                       CONSTRAINT fk_post_user FOREIGN KEY (user_id) REFERENCES users(user_id)
);

CREATE TABLE comments (
                          comment_id BIGINT PRIMARY KEY AUTO_INCREMENT,
                          user_id BIGINT,
                          post_id BIGINT,
                          parent_comment_id BIGINT,
                          content VARCHAR(255),
                          created_at DATETIME,
                          updated_at DATETIME,
                          is_deleted BIT(1),
                          is_hidden BIT(1),
                          CONSTRAINT fk_comment_user FOREIGN KEY (user_id) REFERENCES users(user_id),
                          CONSTRAINT fk_comment_post FOREIGN KEY (post_id) REFERENCES posts(post_id),
                          CONSTRAINT fk_parent_comment FOREIGN KEY (parent_comment_id) REFERENCES comments(comment_id)
);

CREATE TABLE post_reactions (
                                post_reaction_id BIGINT PRIMARY KEY AUTO_INCREMENT,
                                user_id BIGINT,
                                post_id BIGINT,
                                reaction_type ENUM('LIKE', 'WOW', 'LOVE', 'SAD','ANGRY'),
                                created_at DATETIME,
                                CONSTRAINT fk_post_reaction_user FOREIGN KEY (user_id) REFERENCES users(user_id),
                                CONSTRAINT fk_post_reaction_post FOREIGN KEY (post_id) REFERENCES posts(post_id)
);

CREATE TABLE comment_reactions (
                                   comment_reaction_id BIGINT PRIMARY KEY AUTO_INCREMENT,
                                   user_id BIGINT,
                                   comment_id BIGINT,
                                   reaction_type ENUM('LIKE', 'WOW', 'LOVE', 'SAD','ANGRY'),
                                   created_at DATETIME,
                                   CONSTRAINT fk_comment_reaction_user FOREIGN KEY (user_id) REFERENCES users(user_id),
                                   CONSTRAINT fk_comment_reaction_comment FOREIGN KEY (comment_id) REFERENCES comments(comment_id)
);

CREATE TABLE tags (
                      tag_id BIGINT PRIMARY KEY AUTO_INCREMENT,
                      tagged_user_id BIGINT,
                      post_id BIGINT,
                      tagged_by_user_id BIGINT,
                      CONSTRAINT fk_tagged_user FOREIGN KEY (tagged_user_id) REFERENCES users(user_id),
                      CONSTRAINT fk_tagged_by_user FOREIGN KEY (tagged_by_user_id) REFERENCES users(user_id),
                      CONSTRAINT fk_tag_post FOREIGN KEY (post_id) REFERENCES posts(post_id)
);

CREATE TABLE conversations (
                               conversation_id BIGINT PRIMARY KEY AUTO_INCREMENT,
                               conversation_name VARCHAR(255),
                               is_group BIT(1),
                               created_at DATETIME,
                               is_deleted BIT(1)
);

CREATE TABLE chat_members (
                              chat_members_id BIGINT PRIMARY KEY AUTO_INCREMENT,
                              conversation_id BIGINT,
                              user_id BIGINT,
                              join_at DATETIME,
                              is_admin BIT(1),
                              is_deleted BIT(1),
                              CONSTRAINT fk_chat_member_user FOREIGN KEY (user_id) REFERENCES users(user_id),
                              CONSTRAINT fk_chat_member_conversation FOREIGN KEY (conversation_id) REFERENCES conversations(conversation_id)
);

CREATE TABLE messages (
                          message_id  BIGINT PRIMARY KEY AUTO_INCREMENT,
                          sender_id BIGINT,
                          conversation_id BIGINT,
                          content VARCHAR(255),
                          sent_at DATETIME,
                          updated_at DATETIME,
                          is_deleted BIT(1),
                          CONSTRAINT fk_message_sender FOREIGN KEY (sender_id) REFERENCES users(user_id),
                          CONSTRAINT fk_message_conversation FOREIGN KEY (conversation_id) REFERENCES conversations(conversation_id)
);


-- Thêm dữ liệu vào bảng roles
INSERT INTO roles (name) VALUES
                             ('USER'),
                             ('ADMIN');

-- Thêm dữ liệu vào bảng users
INSERT INTO users (username, email, password, first_name, last_name, gender, visibility, role_id, bio, location, work, education, created_at, updated_at, avatar, background_image, date_of_birth, is_email_verified) VALUES
                                                                                                                                                                                                                          ('johndoe', 'johndoe@example.com', '$2a$12$vtu.Pclyb0vzclTjAJZgCOrbd6SVOia8PGOFcgODYd27ZfDeiJTNu', 'John', 'Doe', 'MALE', 'PUBLIC', 1, 'I am John', 'New York', 'Engineer', 'MIT', NOW(), NOW(), 'avatar1.jpg', 'background1.jpg', '1990-01-01', true),
                                                                                                                                                                                                                          ('janedoe', 'janedoe@example.com', '$2a$12$vtu.Pclyb0vzclTjAJZgCOrbd6SVOia8PGOFcgODYd27ZfDeiJTNu', 'Jane', 'Doe', 'FEMALE', 'FRIEND', 2, 'I am Jane', 'Los Angeles', 'Doctor', 'Harvard', NOW(), NOW(), 'avatar2.jpg', 'background2.jpg', '1992-02-02', true);

-- Thêm dữ liệu vào bảng relationships
INSERT INTO relationships (user_id, friend_id, created_at, relation) VALUES
                                                                                 (1, 2, NOW(), 'FRIEND');


-- Thêm dữ liệu vào bảng posts
INSERT INTO posts (user_id, content, visibility, created_at, updated_at, is_deleted, photo_lists) VALUES
                                                                                                      (1, 'Hello, this is my first post!', 'PUBLIC', NOW(), NOW(), 0, 'photo1.jpg'),
                                                                                                      (2, 'Enjoying a sunny day!', 'FRIEND', NOW(), NOW(), 0, 'photo2.jpg');

-- Thêm dữ liệu vào bảng comments
INSERT INTO comments (user_id, post_id, parent_comment_id, content, created_at, updated_at, is_deleted, is_hidden) VALUES
                                                                                                                       (2, 1, NULL, 'Great post!', NOW(), NOW(), 0, 0),
                                                                                                                       (1, 2, NULL, 'Thanks for sharing!', NOW(), NOW(), 0, 0);

-- Thêm dữ liệu vào bảng post_reactions
INSERT INTO post_reactions (user_id, post_id, reaction_type, created_at) VALUES
                                                                             (2, 1, 'LIKE', NOW()),
                                                                             (1, 2, 'LIKE', NOW());

-- Thêm dữ liệu vào bảng comment_reactions
INSERT INTO comment_reactions (user_id, comment_id, reaction_type, created_at) VALUES
                                                                                   (1, 1, 'LIKE', NOW()),
                                                                                   (2, 2, 'LIKE', NOW());

-- Thêm dữ liệu vào bảng tags
INSERT INTO tags (tagged_user_id, post_id, tagged_by_user_id) VALUES
                                                                  (2, 1, 1),
                                                                  (1, 2, 2);



