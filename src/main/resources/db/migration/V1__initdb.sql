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
                       visibility ENUM('public', 'friend', 'private'),
                       role_id BIGINT,
                       bio VARCHAR(255),
                       location VARCHAR(255),
                       work VARCHAR(255),
                       education VARCHAR(255),
                       created_at DATETIME,
                       updated_at DATETIME,
                       avatar VARCHAR(255),
                       background_image VARCHAR(255),
                       CONSTRAINT fk_role FOREIGN KEY (role_id) REFERENCES roles(role_id)
);

CREATE TABLE friends (
                         user_id BIGINT,
                         friend_id BIGINT,
                         is_friend BIT(1),
                         other_relation ENUM('love', 'relative', 'none'),
                         created_at DATETIME,
                         PRIMARY KEY (user_id, friend_id),
                         CONSTRAINT fk_user FOREIGN KEY (user_id) REFERENCES users(user_id),
                         CONSTRAINT fk_friend FOREIGN KEY (friend_id) REFERENCES users(user_id)
);

CREATE TABLE posts (
                       post_id BIGINT PRIMARY KEY AUTO_INCREMENT,
                       user_id BIGINT,
                       content VARCHAR(255),
                       visibility ENUM('public', 'friend', 'private'),
                       created_at DATETIME,
                       updated_at DATETIME,
                       is_deleted BIT(1),
                       photo_lists VARCHAR(255),
                       CONSTRAINT fk_post_user FOREIGN KEY (user_id) REFERENCES users(user_id)
);

CREATE TABLE comments (
                          comment_id BIGINT AUTO_INCREMENT,
                          user_id BIGINT,
                          post_id BIGINT,
                          parent_comment_id BIGINT,
                          content VARCHAR(255),
                          created_at DATETIME,
                          updated_at DATETIME,
                          is_deleted BIT(1),
                          is_hidden BIT(1),
                          PRIMARY KEY (comment_id),
                          CONSTRAINT fk_comment_user FOREIGN KEY (user_id) REFERENCES users(user_id),
                          CONSTRAINT fk_comment_post FOREIGN KEY (post_id) REFERENCES posts(post_id),
                          CONSTRAINT fk_parent_comment FOREIGN KEY (parent_comment_id) REFERENCES comments(comment_id)
);

CREATE TABLE post_reactions (
                                user_id BIGINT,
                                post_id BIGINT,
                                reaction_type ENUM('like', 'dislike', 'love', 'sad'),
                                created_at DATETIME,
                                PRIMARY KEY (user_id, post_id),
                                CONSTRAINT fk_post_reaction_user FOREIGN KEY (user_id) REFERENCES users(user_id),
                                CONSTRAINT fk_post_reaction_post FOREIGN KEY (post_id) REFERENCES posts(post_id)
);

CREATE TABLE comment_reactions (
                                   user_id BIGINT,
                                   comment_id BIGINT,
                                   reaction_type ENUM('like', 'dislike', 'love', 'sad'),
                                   created_at DATETIME,
                                   PRIMARY KEY (user_id, comment_id),
                                   CONSTRAINT fk_comment_reaction_user FOREIGN KEY (user_id) REFERENCES users(user_id),
                                   CONSTRAINT fk_comment_reaction_comment FOREIGN KEY (comment_id) REFERENCES comments(comment_id)
);

CREATE TABLE tags (
                      user_id BIGINT,
                      post_id BIGINT,
                      PRIMARY KEY (user_id, post_id),
                      CONSTRAINT fk_tag_user FOREIGN KEY (user_id) REFERENCES users(user_id),
                      CONSTRAINT fk_tag_post FOREIGN KEY (post_id) REFERENCES posts(post_id)
);

CREATE TABLE conversations (
                               conversation_id BIGINT AUTO_INCREMENT,
                               conversation_name VARCHAR(255),
                               is_group BIT(1),
                               created_at DATETIME,
                               is_deleted BIT(1),
                               PRIMARY KEY (conversation_id)
);

CREATE TABLE chat_members (
                              conversation_id BIGINT,
                              user_id BIGINT,
                              join_at DATETIME,
                              is_admin BIT(1),
                              is_deleted BIT(1),
                              PRIMARY KEY (conversation_id, user_id),
                              CONSTRAINT fk_chat_member_user FOREIGN KEY (user_id) REFERENCES users(user_id),
                              CONSTRAINT fk_chat_member_conversation FOREIGN KEY (conversation_id) REFERENCES conversations(conversation_id)
);

CREATE TABLE messages (
                          message_id BIGINT AUTO_INCREMENT,
                          sender_id BIGINT,
                          conversation_id BIGINT,
                          content VARCHAR(255),
                          sent_at DATETIME,
                          updated_at DATETIME,
                          is_deleted BIT(1),
                          PRIMARY KEY (message_id),
                          CONSTRAINT fk_message_sender FOREIGN KEY (sender_id) REFERENCES users(user_id),
                          CONSTRAINT fk_message_conversation FOREIGN KEY (conversation_id) REFERENCES conversations(conversation_id)
);
