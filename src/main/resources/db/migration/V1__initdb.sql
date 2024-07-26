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
                                   CONSTRAINT fk_comment_reaction_user FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE,
                                   CONSTRAINT fk_comment_reaction_comment FOREIGN KEY (comment_id) REFERENCES comments(comment_id) ON DELETE CASCADE
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
                               created_at DATETIME
                           );

CREATE TABLE chat_members (
                              chat_members_id BIGINT PRIMARY KEY AUTO_INCREMENT,
                              conversation_id BIGINT,
                              user_id BIGINT,
                              join_at DATETIME,
                              is_admin BIT(1),
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
                          CONSTRAINT fk_message_sender FOREIGN KEY (sender_id) REFERENCES users(user_id),
                          CONSTRAINT fk_message_conversation FOREIGN KEY (conversation_id) REFERENCES conversations(conversation_id)
);




-- -- Thêm dữ liệu vào bảng roles
-- INSERT INTO roles (name) VALUES
--                              ('USER'),
--                              ('ADMIN');
--
-- -- Thêm dữ liệu vào bảng users
-- -- Thêm 30 người dùng mới với role USER
-- INSERT INTO users (username, email, password, first_name, last_name, gender, visibility, role_id, bio, location, work, education, created_at, updated_at, avatar, background_image, date_of_birth, is_email_verified) VALUES
--                                                                                                                                                                                                                           ('user1', 'user1@example.com', '$2a$12$vtu.Pclyb0vzclTjAJZgCOrbd6SVOia8PGOFcgODYd27ZfDeiJTNu', 'First1', 'Last1', 'MALE', 'PUBLIC', 1, 'Bio of user1', 'Location1', 'Work1', 'Education1', NOW(), NOW(), 'avatar1.jpg', 'background1.jpg', '1990-01-01', true),
--                                                                                                                                                                                                                           ('user2', 'user2@example.com', '$2a$12$vtu.Pclyb0vzclTjAJZgCOrbd6SVOia8PGOFcgODYd27ZfDeiJTNu', 'First2', 'Last2', 'FEMALE', 'FRIEND', 1, 'Bio of user2', 'Location2', 'Work2', 'Education2', NOW(), NOW(), 'avatar2.jpg', 'background2.jpg', '1991-02-02', true),
--                                                                                                                                                                                                                           ('user3', 'user3@example.com', '$2a$12$vtu.Pclyb0vzclTjAJZgCOrbd6SVOia8PGOFcgODYd27ZfDeiJTNu', 'First3', 'Last3', 'OTHERS', 'PRIVATE', 1, 'Bio of user3', 'Location3', 'Work3', 'Education3', NOW(), NOW(), 'avatar3.jpg', 'background3.jpg', '1992-03-03', true),
--                                                                                                                                                                                                                           ('user4', 'user4@example.com', '$2a$12$vtu.Pclyb0vzclTjAJZgCOrbd6SVOia8PGOFcgODYd27ZfDeiJTNu', 'First4', 'Last4', 'MALE', 'PUBLIC', 1, 'Bio of user4', 'Location4', 'Work4', 'Education4', NOW(), NOW(), 'avatar4.jpg', 'background4.jpg', '1993-04-04', true),
--                                                                                                                                                                                                                           ('user5', 'user5@example.com', '$2a$12$vtu.Pclyb0vzclTjAJZgCOrbd6SVOia8PGOFcgODYd27ZfDeiJTNu', 'First5', 'Last5', 'FEMALE', 'FRIEND', 1, 'Bio of user5', 'Location5', 'Work5', 'Education5', NOW(), NOW(), 'avatar5.jpg', 'background5.jpg', '1994-05-05', true),
--                                                                                                                                                                                                                           ('user6', 'user6@example.com', '$2a$12$vtu.Pclyb0vzclTjAJZgCOrbd6SVOia8PGOFcgODYd27ZfDeiJTNu', 'First6', 'Last6', 'OTHERS', 'PRIVATE', 1, 'Bio of user6', 'Location6', 'Work6', 'Education6', NOW(), NOW(), 'avatar6.jpg', 'background6.jpg', '1995-06-06', true),
--                                                                                                                                                                                                                           ('user7', 'user7@example.com', '$2a$12$vtu.Pclyb0vzclTjAJZgCOrbd6SVOia8PGOFcgODYd27ZfDeiJTNu', 'First7', 'Last7', 'MALE', 'PUBLIC', 1, 'Bio of user7', 'Location7', 'Work7', 'Education7', NOW(), NOW(), 'avatar7.jpg', 'background7.jpg', '1996-07-07', true),
--                                                                                                                                                                                                                           ('user8', 'user8@example.com', '$2a$12$vtu.Pclyb0vzclTjAJZgCOrbd6SVOia8PGOFcgODYd27ZfDeiJTNu', 'First8', 'Last8', 'FEMALE', 'FRIEND', 1, 'Bio of user8', 'Location8', 'Work8', 'Education8', NOW(), NOW(), 'avatar8.jpg', 'background8.jpg', '1997-08-08', true),
--                                                                                                                                                                                                                           ('user9', 'user9@example.com', '$2a$12$vtu.Pclyb0vzclTjAJZgCOrbd6SVOia8PGOFcgODYd27ZfDeiJTNu', 'First9', 'Last9', 'OTHERS', 'PRIVATE', 1, 'Bio of user9', 'Location9', 'Work9', 'Education9', NOW(), NOW(), 'avatar9.jpg', 'background9.jpg', '1998-09-09', true),
--                                                                                                                                                                                                                           ('user10', 'user10@example.com', '$2a$12$vtu.Pclyb0vzclTjAJZgCOrbd6SVOia8PGOFcgODYd27ZfDeiJTNu', 'First10', 'Last10', 'MALE', 'PUBLIC', 1, 'Bio of user10', 'Location10', 'Work10', 'Education10', NOW(), NOW(), 'avatar10.jpg', 'background10.jpg', '1999-10-10', true),
--                                                                                                                                                                                                                           ('user11', 'user11@example.com', '$2a$12$vtu.Pclyb0vzclTjAJZgCOrbd6SVOia8PGOFcgODYd27ZfDeiJTNu', 'First11', 'Last11', 'FEMALE', 'FRIEND', 1, 'Bio of user11', 'Location11', 'Work11', 'Education11', NOW(), NOW(), 'avatar11.jpg', 'background11.jpg', '2000-11-11', true),
--                                                                                                                                                                                                                           ('user12', 'user12@example.com', '$2a$12$vtu.Pclyb0vzclTjAJZgCOrbd6SVOia8PGOFcgODYd27ZfDeiJTNu', 'First12', 'Last12', 'OTHERS', 'PRIVATE', 1, 'Bio of user12', 'Location12', 'Work12', 'Education12', NOW(), NOW(), 'avatar12.jpg', 'background12.jpg', '2001-12-12', true),
--                                                                                                                                                                                                                           ('user13', 'user13@example.com', '$2a$12$vtu.Pclyb0vzclTjAJZgCOrbd6SVOia8PGOFcgODYd27ZfDeiJTNu', 'First13', 'Last13', 'MALE', 'PUBLIC', 1, 'Bio of user13', 'Location13', 'Work13', 'Education13', NOW(), NOW(), 'avatar13.jpg', 'background13.jpg', '2002-01-13', true),
--                                                                                                                                                                                                                           ('user14', 'user14@example.com', '$2a$12$vtu.Pclyb0vzclTjAJZgCOrbd6SVOia8PGOFcgODYd27ZfDeiJTNu', 'First14', 'Last14', 'FEMALE', 'FRIEND', 1, 'Bio of user14', 'Location14', 'Work14', 'Education14', NOW(), NOW(), 'avatar14.jpg', 'background14.jpg', '2003-02-14', true),
--                                                                                                                                                                                                                           ('user15', 'user15@example.com', '$2a$12$vtu.Pclyb0vzclTjAJZgCOrbd6SVOia8PGOFcgODYd27ZfDeiJTNu', 'First15', 'Last15', 'OTHERS', 'PRIVATE', 1, 'Bio of user15', 'Location15', 'Work15', 'Education15', NOW(), NOW(), 'avatar15.jpg', 'background15.jpg', '2004-03-15', true),
--                                                                                                                                                                                                                           ('user16', 'user16@example.com', '$2a$12$vtu.Pclyb0vzclTjAJZgCOrbd6SVOia8PGOFcgODYd27ZfDeiJTNu', 'First16', 'Last16', 'MALE', 'PUBLIC', 1, 'Bio of user16', 'Location16', 'Work16', 'Education16', NOW(), NOW(), 'avatar16.jpg', 'background16.jpg', '2005-04-16', true),
--                                                                                                                                                                                                                           ('user17', 'user17@example.com', '$2a$12$vtu.Pclyb0vzclTjAJZgCOrbd6SVOia8PGOFcgODYd27ZfDeiJTNu', 'First17', 'Last17', 'FEMALE', 'FRIEND', 1, 'Bio of user17', 'Location17', 'Work17', 'Education17', NOW(), NOW(), 'avatar17.jpg', 'background17.jpg', '2006-05-17', true),
--                                                                                                                                                                                                                           ('user18', 'user18@example.com', '$2a$12$vtu.Pclyb0vzclTjAJZgCOrbd6SVOia8PGOFcgODYd27ZfDeiJTNu', 'First18', 'Last18', 'OTHERS', 'PRIVATE', 1, 'Bio of user18', 'Location18', 'Work18', 'Education18', NOW(), NOW(), 'avatar18.jpg', 'background18.jpg', '2007-06-18', true),
--                                                                                                                                                                                                                           ('user19', 'user19@example.com', '$2a$12$vtu.Pclyb0vzclTjAJZgCOrbd6SVOia8PGOFcgODYd27ZfDeiJTNu', 'First19', 'Last19', 'MALE', 'PUBLIC', 1, 'Bio of user19', 'Location19', 'Work19', 'Education19', NOW(), NOW(), 'avatar19.jpg', 'background19.jpg', '2008-07-19', true),
--                                                                                                                                                                                                                           ('user20', 'user20@example.com', '$2a$12$vtu.Pclyb0vzclTjAJZgCOrbd6SVOia8PGOFcgODYd27ZfDeiJTNu', 'First20', 'Last20', 'FEMALE', 'FRIEND', 1, 'Bio of user20', 'Location20', 'Work20', 'Education20', NOW(), NOW(), 'avatar20.jpg', 'background20.jpg', '2009-08-20', true),
--                                                                                                                                                                                                                           ('user21', 'user21@example.com', '$2a$12$vtu.Pclyb0vzclTjAJZgCOrbd6SVOia8PGOFcgODYd27ZfDeiJTNu', 'First21', 'Last21', 'OTHERS', 'PRIVATE', 1, 'Bio of user21', 'Location21', 'Work21', 'Education21', NOW(), NOW(), 'avatar21.jpg', 'background21.jpg', '2010-09-21', true),
--                                                                                                                                                                                                                           ('user22', 'user22@example.com', '$2a$12$vtu.Pclyb0vzclTjAJZgCOrbd6SVOia8PGOFcgODYd27ZfDeiJTNu', 'First22', 'Last22', 'MALE', 'PUBLIC', 1, 'Bio of user22', 'Location22', 'Work22', 'Education22', NOW(), NOW(), 'avatar22.jpg', 'background22.jpg', '2011-10-22', true),
--                                                                                                                                                                                                                           ('user23', 'user23@example.com', '$2a$12$vtu.Pclyb0vzclTjAJZgCOrbd6SVOia8PGOFcgODYd27ZfDeiJTNu', 'First23', 'Last23', 'FEMALE', 'FRIEND', 1, 'Bio of user23', 'Location23', 'Work23', 'Education23', NOW(), NOW(), 'avatar23.jpg', 'background23.jpg', '2012-11-23', true),
--                                                                                                                                                                                                                           ('user24', 'user24@example.com', '$2a$12$vtu.Pclyb0vzclTjAJZgCOrbd6SVOia8PGOFcgODYd27ZfDeiJTNu', 'First24', 'Last24', 'OTHERS', 'PRIVATE', 1, 'Bio of user24', 'Location24', 'Work24', 'Education24', NOW(), NOW(), 'avatar24.jpg', 'background24.jpg', '2013-12-24', true),
--                                                                                                                                                                                                                           ('user25', 'user25@example.com', '$2a$12$vtu.Pclyb0vzclTjAJZgCOrbd6SVOia8PGOFcgODYd27ZfDeiJTNu', 'First25', 'Last25', 'MALE', 'PUBLIC', 1, 'Bio of user25', 'Location25', 'Work25', 'Education25', NOW(), NOW(), 'avatar25.jpg', 'background25.jpg', '2014-01-25', true),
--                                                                                                                                                                                                                           ('user26', 'user26@example.com', '$2a$12$vtu.Pclyb0vzclTjAJZgCOrbd6SVOia8PGOFcgODYd27ZfDeiJTNu', 'First26', 'Last26', 'FEMALE', 'FRIEND', 1, 'Bio of user26', 'Location26', 'Work26', 'Education26', NOW(), NOW(), 'avatar26.jpg', 'background26.jpg', '2015-02-26', true),
--                                                                                                                                                                                                                           ('user27', 'user27@example.com', '$2a$12$vtu.Pclyb0vzclTjAJZgCOrbd6SVOia8PGOFcgODYd27ZfDeiJTNu', 'First27', 'Last27', 'OTHERS', 'PRIVATE', 1, 'Bio of user27', 'Location27', 'Work27', 'Education27', NOW(), NOW(), 'avatar27.jpg', 'background27.jpg', '2016-03-27', true),
--                                                                                                                                                                                                                           ('user28', 'user28@example.com', '$2a$12$vtu.Pclyb0vzclTjAJZgCOrbd6SVOia8PGOFcgODYd27ZfDeiJTNu', 'First28', 'Last28', 'MALE', 'PUBLIC', 1, 'Bio of user28', 'Location28', 'Work28', 'Education28', NOW(), NOW(), 'avatar28.jpg', 'background28.jpg', '2017-04-28', true),
--                                                                                                                                                                                                                           ('user29', 'user29@example.com', '$2a$12$vtu.Pclyb0vzclTjAJZgCOrbd6SVOia8PGOFcgODYd27ZfDeiJTNu', 'First29', 'Last29', 'FEMALE', 'FRIEND', 1, 'Bio of user29', 'Location29', 'Work29', 'Education29', NOW(), NOW(), 'avatar29.jpg', 'background29.jpg', '2018-05-29', true),
--                                                                                                                                                                                                                           ('user30', 'user30@example.com', '$2a$12$vtu.Pclyb0vzclTjAJZgCOrbd6SVOia8PGOFcgODYd27ZfDeiJTNu', 'First30', 'Last30', 'OTHERS', 'PRIVATE', 1, 'Bio of user30', 'Location30', 'Work30', 'Education30', NOW(), NOW(), 'avatar30.jpg', 'background30.jpg', '2019-06-30', true);
--
--
-- -- Tạo một bảng tạm thời để chứa các kết quả ngẫu nhiên
-- CREATE TEMPORARY TABLE temp_relationships AS
-- SELECT
--     user1.user_id AS user1_id,
--     user2.user_id AS user2_id,
--     ELT(FLOOR(1 + (RAND() * 3)), 'FRIEND', 'PENDING', 'BLOCK') AS relation
-- FROM
--     (SELECT user_id FROM users ORDER BY RAND() LIMIT 100) AS user1
--         JOIN
--     (SELECT user_id FROM users ORDER BY RAND() LIMIT 100) AS user2
--     ON
--         user1.user_id != user2.user_id;
--
-- -- Thêm dữ liệu từ bảng tạm vào bảng relationships
-- INSERT INTO relationships (user_id, friend_id, created_at, relation)
-- SELECT
--     user1_id,
--     user2_id,
--     NOW(),
--     relation
-- FROM
--     temp_relationships;
--
-- -- Xóa bảng tạm
-- DROP TEMPORARY TABLE temp_relationships;
--
-- -- Kiểm tra và đảm bảo có đủ 30 user
-- SELECT COUNT(*) FROM users;
--
-- -- Đoạn mã để thêm ngẫu nhiên 50 bài post từ 30 user
-- INSERT INTO posts (user_id, content, visibility, created_at, updated_at, photo_lists)
-- SELECT
--     u.user_id,
--     CONCAT('Post content ', p.post_number) AS content,
--     ELT(FLOOR(1 + (RAND() * 3)), 'PUBLIC', 'FRIEND', 'PRIVATE') AS visibility,
--     NOW() - INTERVAL FLOOR(RAND() * 365) DAY AS created_at,
--     (NOW() - INTERVAL FLOOR(RAND() * 365) DAY) + INTERVAL FLOOR(RAND() * 24) HOUR AS updated_at,
--     CONCAT('photo', p.post_number, '.jpg') AS photo_lists
-- FROM
--     (SELECT user_id FROM users ORDER BY RAND() LIMIT 30) AS u,
--     (SELECT @rownum := 0) AS r,
--     (SELECT @postnum := 0) AS pnum,
--     (SELECT (@postnum := @postnum + 1) AS post_number FROM information_schema.tables LIMIT 50) AS p;
--
-- -- Tạo một bảng tạm thời để chứa các kết quả ngẫu nhiên
-- CREATE TEMPORARY TABLE temp_comments AS
-- SELECT
--     user_id,
--     post_id,
--     NULLIF(
--             (SELECT comment_id FROM comments WHERE post_id = c.post_id ORDER BY RAND() LIMIT 1),
--         NULL
--     ) AS parent_comment_id,
--     CONCAT('Comment content ', @rownum := @rownum + 1) AS content,
--     NOW() - INTERVAL FLOOR(RAND() * 365) DAY AS created_at,
--     (NOW() - INTERVAL FLOOR(RAND() * 365) DAY) + INTERVAL FLOOR(RAND() * 24) HOUR AS updated_at,
--     0 AS is_hidden
-- FROM
--     (SELECT post_id FROM posts ORDER BY RAND()) AS c,
--     (SELECT user_id FROM users ORDER BY RAND()) AS u,
--     (SELECT @rownum := 0) AS r;
--
-- -- Thêm dữ liệu từ bảng tạm vào bảng comments
-- INSERT INTO comments (user_id, post_id, parent_comment_id, content, created_at, updated_at, is_hidden)
-- SELECT
--     user_id,
--     post_id,
--     parent_comment_id,
--     content,
--     created_at,
--     updated_at,
--     is_hidden
-- FROM
--     temp_comments;
--
-- -- Xóa bảng tạm
-- DROP TEMPORARY TABLE temp_comments;
-- -- Tạo một bảng tạm thời để chứa các kết quả ngẫu nhiên
-- CREATE TEMPORARY TABLE temp_reactions AS
-- SELECT
--     user_id,
--     post_id,
--     ELT(FLOOR(1 + (RAND() * 5)), 'LIKE', 'WOW', 'LOVE', 'SAD', 'ANGRY') AS reaction_type,
--     NOW() - INTERVAL FLOOR(RAND() * 365) DAY AS created_at
-- FROM
--     (SELECT post_id FROM posts ORDER BY RAND()) AS p,
--     (SELECT user_id FROM users ORDER BY RAND()) AS u
--     LIMIT 100;
--
-- -- Thêm dữ liệu từ bảng tạm vào bảng post_reactions
-- INSERT INTO post_reactions (user_id, post_id, reaction_type, created_at)
-- SELECT
--     user_id,
--     post_id,
--     reaction_type,
--     created_at
-- FROM
--     temp_reactions;
--
-- -- Xóa bảng tạm
-- DROP TEMPORARY TABLE temp_reactions;
-- -- Tạo một bảng tạm thời để chứa các kết quả ngẫu nhiên
-- CREATE TEMPORARY TABLE temp_tags AS
-- SELECT
--     tagged_user_id,
--     post_id,
--     tagged_by_user_id
-- FROM
--     (SELECT post_id FROM posts ORDER BY RAND()) AS p,
--     (SELECT user_id AS tagged_user_id FROM users ORDER BY RAND()) AS tu,
--     (SELECT user_id AS tagged_by_user_id FROM users ORDER BY RAND()) AS tb
-- WHERE
--     tu.tagged_user_id != tb.tagged_by_user_id
-- LIMIT 8;
--
-- -- Thêm dữ liệu từ bảng tạm vào bảng tags
-- INSERT INTO tags (tagged_user_id, post_id, tagged_by_user_id)
-- SELECT
--     tagged_user_id,
--     post_id,
--     tagged_by_user_id
-- FROM
--     temp_tags;
--
-- -- Xóa bảng tạm
-- DROP TEMPORARY TABLE temp_tags;
--


