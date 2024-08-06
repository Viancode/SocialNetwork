USE socialnetwork;
-- Tạo thủ tục để thêm bình luận xúc phạm
-- V5__Add_offensive_comments.sql

-- Tạo thủ tục để thêm bình luận xúc phạm
CREATE PROCEDURE add_offensive_comments()
BEGIN
    DECLARE i INT DEFAULT 0;
    DECLARE post_id_var BIGINT;
    DECLARE user_id_var BIGINT;
    DECLARE comment_content VARCHAR(255);

    WHILE i < 50 DO
        -- Chọn một bài đăng ngẫu nhiên
SELECT post_id, user_id INTO post_id_var, user_id_var
FROM posts
ORDER BY RAND()
    LIMIT 1;

-- Tạo nội dung bình luận xúc phạm bằng tiếng Việt
SET comment_content = ELT(FLOOR(1 + RAND() * 10),
            'Mày ngu như con bò, sao mày sống được đến giờ vậy?',
            'Bài đăng này như cứt, y như quan điểm của mày vậy.',
            'Về học lại đi, đồ mù chữ.',
            'Tao hy vọng mày sẽ bị nghẹn bởi sự ngu học của chính mày.',
            'Sống như mày thì sống làm chó gì',
            'Mày nên tìm hiểu kỹ trước khi bình luận, đừng để lộ sự ngu dốt của mày ra ngoài.',
            'Ngu',
            'Vai lon',
            'Địt mẹ mày',
            'Hèn như chó vậy'
        );

        -- Chèn bình luận xúc phạm vào cơ sở dữ liệu
INSERT INTO comments (user_id, post_id, content, created_at, updated_at, is_hidden)
VALUES (
           (SELECT user_id FROM users WHERE user_id != user_id_var ORDER BY RAND() LIMIT 1),
    post_id_var,
    comment_content,
    NOW(),
    NOW(),
    0
    );

SET i = i + 1;
END WHILE;
END;

-- Gọi thủ tục để thêm bình luận xúc phạm
CALL add_offensive_comments();

-- Xóa thủ tục sau khi sử dụng
DROP PROCEDURE IF EXISTS add_offensive_comments;