USE socialnetwork;

RENAME TABLE friends TO relationships;

-- Thêm cột mới 'relationship'
ALTER TABLE relationships
    ADD COLUMN relationship ENUM('friend', 'lover', 'relative', 'none');

-- Xóa các cột cũ
ALTER TABLE relationships
DROP COLUMN is_friend,
DROP COLUMN other_relation;
