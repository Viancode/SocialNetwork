-- Tạo role
INSERT INTO roles (name) VALUES ('USER');

-- Tạo 3 người dùng
INSERT INTO users (username, email, password, first_name, last_name, gender, visibility, role_id, created_at, updated_at, avatar, background_image, date_of_birth, is_email_verified)
VALUES
    ('user1', 'user1@gmail.com', '$2a$10$63fedCD/3qKGqcEjrb7RxeNzMaI8bXFNwXlzXWwPDw8mw77LNjIc6', 'First1', 'Last1', 'MALE', 'PUBLIC', 1, NOW(), NOW(), null, null, '1990-01-01', true),
    ('user2', 'user2@gmail.com', '$2a$10$63fedCD/3qKGqcEjrb7RxeNzMaI8bXFNwXlzXWwPDw8mw77LNjIc6', 'First2', 'Last2', 'FEMALE', 'PUBLIC', 1, NOW(), NOW(), null, null, '1990-01-01', true),
    ('user3', 'user3@gmail.com', '$2a$10$63fedCD/3qKGqcEjrb7RxeNzMaI8bXFNwXlzXWwPDw8mw77LNjIc6', 'First3', 'Last3', 'OTHERS', 'PUBLIC', 1, NOW(), NOW(), null, null, '1990-01-01', true);

-- Tạo mối quan hệ giữa 3 người dùng
INSERT INTO relationships (user_id, friend_id, created_at, relation)
VALUES
    (1, 2, NOW(), 'FRIEND'),
    (1, 3, NOW(), 'BLOCK'),
    (2, 3, NOW(), 'PENDING');
DELIMITER //
CREATE PROCEDURE create_posts_comments_reactions()
BEGIN
    DECLARE i INT DEFAULT 0;
    DECLARE j INT DEFAULT 0;
    DECLARE post_id_var BIGINT;
    DECLARE comment_id_var BIGINT;
    DECLARE post_created_at DATETIME;
    DECLARE post_user_id INT;
    DECLARE can_tag BOOLEAN;
    DECLARE tag_user_id INT;

    WHILE i < 60 DO
        SET post_user_id = (i DIV 20) + 1;
        -- Tạo thời gian ngẫu nhiên trong khoảng 1 năm trở lại
        SET post_created_at = DATE_SUB(NOW(), INTERVAL FLOOR(RAND() * 365) DAY);
        SET post_created_at = DATE_SUB(post_created_at, INTERVAL FLOOR(RAND() * 24) HOUR);
        SET post_created_at = DATE_SUB(post_created_at, INTERVAL FLOOR(RAND() * 60) MINUTE);
        SET post_created_at = DATE_SUB(post_created_at, INTERVAL FLOOR(RAND() * 60) SECOND);

        -- Tạo bài đăng
INSERT INTO posts (user_id, content, visibility, created_at, updated_at, photo_lists)
VALUES (
           post_user_id,
           CONCAT('Post content ', i),
           ELT(FLOOR(1 + RAND() * 3), 'PUBLIC', 'FRIEND', 'PRIVATE'),
           post_created_at,
           post_created_at,
           'https://ghtk-socialnetwork.s3.ap-southeast-2.amazonaws.com/images/9b227680-ff92-4bbf-a237-3001cd7f98c1.png'
       );

SET post_id_var = LAST_INSERT_ID();

        -- Tạo 5 bình luận cho mỗi bài đăng
        SET j = 0;
        WHILE j < 5 DO
            INSERT INTO comments (user_id, post_id, content, created_at, updated_at)
            VALUES (
                FLOOR(1 + RAND() * 3),
                post_id_var,
                CONCAT('Comment ', j, ' on post ', i),
                DATE_ADD(post_created_at, INTERVAL FLOOR(RAND() * 24 * 60) MINUTE),
                DATE_ADD(post_created_at, INTERVAL FLOOR(RAND() * 24 * 60) MINUTE)
            );

            SET comment_id_var = LAST_INSERT_ID();

            -- Thêm phản ứng cho bình luận (50% cơ hội)
            IF RAND() < 0.5 THEN
                INSERT INTO comment_reactions (user_id, comment_id, reaction_type, created_at)
                VALUES (
                    FLOOR(1 + RAND() * 3),
                    comment_id_var,
                    ELT(FLOOR(1 + RAND() * 5), 'LIKE', 'WOW', 'LOVE', 'SAD', 'ANGRY'),
                    DATE_ADD(post_created_at, INTERVAL FLOOR(RAND() * 24 * 60) MINUTE)
                );
END IF;

            SET j = j + 1;
END WHILE;

        -- Thêm phản ứng cho bài đăng từ cả 3 người dùng
INSERT INTO post_reactions (user_id, post_id, reaction_type, created_at)
VALUES
    (1, post_id_var, ELT(FLOOR(1 + RAND() * 5), 'LIKE', 'WOW', 'LOVE', 'SAD', 'ANGRY'), DATE_ADD(post_created_at, INTERVAL FLOOR(RAND() * 24 * 60) MINUTE)),
    (2, post_id_var, ELT(FLOOR(1 + RAND() * 5), 'LIKE', 'WOW', 'LOVE', 'SAD', 'ANGRY'), DATE_ADD(post_created_at, INTERVAL FLOOR(RAND() * 24 * 60) MINUTE)),
    (3, post_id_var, ELT(FLOOR(1 + RAND() * 5), 'LIKE', 'WOW', 'LOVE', 'SAD', 'ANGRY'), DATE_ADD(post_created_at, INTERVAL FLOOR(RAND() * 24 * 60) MINUTE));

-- Gắn thẻ người dùng khác (chỉ khi là bạn bè)
SET can_tag = FALSE;
        SET tag_user_id = FLOOR(1 + RAND() * 3);

        -- Kiểm tra mối quan hệ
SELECT COUNT(*) INTO can_tag
FROM relationships
WHERE user_id = post_user_id AND friend_id = tag_user_id AND relation = 'FRIEND';

IF can_tag AND tag_user_id != post_user_id THEN
            INSERT INTO tags (tagged_user_id, post_id, tagged_by_user_id)
            VALUES (tag_user_id, post_id_var, post_user_id);
END IF;

        SET i = i + 1;
END WHILE;
END //
DELIMITER ;

-- Gọi procedure để tạo dữ liệu
CALL create_posts_comments_reactions();

-- Xóa procedure sau khi sử dụng
DROP PROCEDURE create_posts_comments_reactions;