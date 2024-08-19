USE socialnetwork;
-- Tạo role
INSERT INTO roles (name) VALUES ('USER');

-- Tạo 3 người dùng
INSERT INTO users (username, email, password, first_name, last_name, gender, visibility, role_id, location, work, education, created_at, updated_at, avatar, background_image, date_of_birth, is_email_verified)
VALUES
    ('user1', 'user1@gmail.com', '$2a$10$63fedCD/3qKGqcEjrb7RxeNzMaI8bXFNwXlzXWwPDw8mw77LNjIc6', 'First1', 'Last1', 'MALE', 'PUBLIC', 1, 'Ha Noi', 'Ha Noi', 'HUST', NOW(), NOW(), null, null, '1990-01-01', true),
    ('user2', 'user2@gmail.com', '$2a$10$63fedCD/3qKGqcEjrb7RxeNzMaI8bXFNwXlzXWwPDw8mw77LNjIc6', 'First2', 'Last2', 'FEMALE', 'PUBLIC', 1, 'Ha Noi', 'HCM', 'NEU', NOW(), NOW(), null, null, '1992-01-01', true),
    ('user3', 'user3@gmail.com', '$2a$10$63fedCD/3qKGqcEjrb7RxeNzMaI8bXFNwXlzXWwPDw8mw77LNjIc6', 'First3', 'Last3', 'OTHERS', 'PUBLIC', 1, 'Bac Giang', 'Ha Noi', 'NEU', NOW(), NOW(), null, null, '1991-01-01', true);

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
    DECLARE parent_comment_id BIGINT;
    DECLARE post_created_at DATETIME;
    DECLARE post_user_id INT;
    DECLARE can_tag BOOLEAN;
    DECLARE tag_user_id INT;
    DECLARE comment_image VARCHAR(255);

    WHILE i < 60 DO
        SET post_user_id = (i DIV 20) + 1;
        -- Tạo thời gian ngẫu nhiên trong khoảng 1 năm trở lại
        SET post_created_at = DATE_SUB(NOW(), INTERVAL FLOOR(RAND() * 365) DAY);
        SET post_created_at = DATE_SUB(post_created_at, INTERVAL FLOOR(RAND() * 24) HOUR);
        SET post_created_at = DATE_SUB(post_created_at, INTERVAL FLOOR(RAND() * 60) MINUTE);
        SET post_created_at = DATE_SUB(post_created_at, INTERVAL FLOOR(RAND() * 60) SECOND);

        -- Tạo bài đăng (giữ nguyên phần này)
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

        -- Tạo 10 bình luận cho mỗi bài đăng
        SET j = 0;
        SET parent_comment_id = NULL;
        WHILE j < 20 DO
            -- Quyết định xem comment này có phải là reply hay không
            IF j > 0 AND RAND() < 0.4 THEN  -- 40% cơ hội là reply
                SET parent_comment_id = (SELECT comment_id FROM comments WHERE post_id = post_id_var ORDER BY RAND() LIMIT 1);
ELSE
                SET parent_comment_id = NULL;
END IF;

            -- Quyết định xem comment có ảnh hay không
            IF RAND() < 0.3 THEN  -- 30% cơ hội có ảnh
                SET comment_image = 'https://ghtk-socialnetwork.s3.ap-southeast-2.amazonaws.com/images/9b227680-ff92-4bbf-a237-3001cd7f98c1.png';
ELSE
                SET comment_image = NULL;
END IF;

INSERT INTO comments (user_id, post_id, parent_comment_id, content, created_at, updated_at, image)
VALUES (
           FLOOR(1 + RAND() * 3),
           post_id_var,
           parent_comment_id,
           CASE
               WHEN parent_comment_id IS NULL THEN CONCAT('Comment ', j, ' on post ', i)
               ELSE CONCAT('Reply to comment ', parent_comment_id, ' on post ', i)
               END,
           DATE_ADD(post_created_at, INTERVAL FLOOR(RAND() * 24 * 60) MINUTE),
           DATE_ADD(post_created_at, INTERVAL FLOOR(RAND() * 24 * 60) MINUTE),
--            0,  -- Giả sử mặc định không ẩn
           comment_image
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
            INSERT INTO tags (tagged_user_id, post_id, tagged_by_user_id, created_at)
            VALUES (tag_user_id, post_id_var, post_user_id, DATE_ADD(post_created_at, INTERVAL FLOOR(RAND() * 24 * 60) MINUTE));
END IF;

        SET i = i + 1;
END WHILE;
END //

DELIMITER ;

-- Gọi procedure để tạo dữ liệu
CALL create_posts_comments_reactions();

-- Xóa procedure sau khi sử dụng
DROP PROCEDURE create_posts_comments_reactions;

-- Tạo 50 bản ghi ngẫu nhiên cho bảng users
DELIMITER //

CREATE PROCEDURE populate_users()
BEGIN
    DECLARE i INT DEFAULT 0;
    DECLARE first_name_list VARCHAR(255);
    DECLARE last_name_list VARCHAR(255);
    DECLARE location_list VARCHAR(255);
    DECLARE work_list VARCHAR(255);
    DECLARE education_list VARCHAR(255);

    SET first_name_list = 'Lan,Hà,Nam,Minh,Trung,Hoàng,Mạnh,Nhung,Đức,Cường,Quân,Anh,Trâm,Đức,An,Bình,Xuân,Quang,Trường';
    SET last_name_list = 'Nguyễn,Trần,Ngô,Tống,Phạm,Lê,Vương,Mạc,Vũ,Võ,Đặng,Phan,Trương,Bùi,Đỗ,Hồ,Dương';
    SET location_list = 'Hà Nội,Hải Phòng,Quảng Ninh,Bắc Ninh,Hải Dương,Hưng Yên,Hà Nam,Thái Bình,Nam Định,Ninh Bình,Hồ Chí Minh,Quảng Ngãi,Quảng Nam,Bình Dương,Cà Mau,Vinh,Nghệ An,Thanh Hóa,Phú Thọ,Đà Nẵng';
    SET work_list = 'doctor,teacher,student,farmer,policeman,engineer,IT';
    SET education_list = 'ĐH Bách Khoa,ĐH Quốc Gia,ĐH Xây Dựng,ĐH Kinh Tế Quốc Dân,ĐH Công Nghệ,ĐH Luật,ĐH Dược,ĐH Y,ĐH Ngoại Ngữ,ĐH Hàng Hải,ĐH Thương Mại,ĐH Sư Phạm,ĐH Ngoại Thương';

    WHILE i < 50 DO
            SET @first_name = SUBSTRING_INDEX(SUBSTRING_INDEX(first_name_list, ',', FLOOR(1 + (RAND() * 19))), ',', -1);
            SET @last_name = SUBSTRING_INDEX(SUBSTRING_INDEX(last_name_list, ',', FLOOR(1 + (RAND() * 16))), ',', -1);

        -- Generate username
            SET @username = CONCAT_WS(' ', @first_name, @last_name);

        -- Generate email from username (replace spaces with periods)
            SET @email = CONCAT(LOWER(REPLACE(@username, ' ', '.')), '@example.com');

INSERT INTO users (username, email, password, first_name, last_name, gender, visibility, role_id, bio, location, work, education, created_at, updated_at, avatar, background_image, date_of_birth, is_email_verified)
VALUES (
           @username,
           @email,
           '$2a$12$rPhLzKBcnY/CwnEAINZ4L.09YaLvRQjphN2QT8nIEWX/BrA37xIzC',
           @first_name,
           @last_name,
           CASE
               WHEN RAND() < 0.33 THEN 'MALE'
               WHEN RAND() < 0.66 THEN 'FEMALE'
               ELSE 'OTHERS'
               END,
           CASE
               WHEN RAND() < 0.33 THEN 'PUBLIC'
               WHEN RAND() < 0.66 THEN 'PRIVATE'
               ELSE 'FRIEND'
               END,
           1,
           'This is a bio.',
           SUBSTRING_INDEX(SUBSTRING_INDEX(location_list, ',', FLOOR(1 + (RAND() * 10))), ',', -1),
           SUBSTRING_INDEX(SUBSTRING_INDEX(work_list, ',', FLOOR(1 + (RAND() * 7))), ',', -1),
           SUBSTRING_INDEX(SUBSTRING_INDEX(education_list, ',', FLOOR(1 + (RAND() * 5))), ',', -1),
           NOW(),
           NOW(),
           CASE
               WHEN RAND() < 0.40 THEN 'https://ghtk-socialnetwork.s3.ap-southeast-2.amazonaws.com/images/02282ff2-7a79-4e60-92c4-38fd148c711a.png'
               ELSE null
               END,
           null,
           DATE_ADD('1990-01-01', INTERVAL FLOOR(RAND() * 10000) DAY),
           TRUE
       );
SET i = i + 1;
END WHILE;
END//

DELIMITER ;

-- Gọi thủ tục để tạo dữ liệu
CALL populate_users();

-- Xóa procedure sau khi sử dụng
DROP PROCEDURE populate_users;