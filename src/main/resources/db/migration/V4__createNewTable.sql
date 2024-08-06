CREATE TABLE suggestions
(
    suggestion_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id       BIGINT,
    friend_id     BIGINT,
    suggest_point INT,
    mutual_friends INT,
    status ENUM('BLOCK','FRIEND','NONE') default 'NONE'
);

INSERT INTO suggestions (user_id, friend_id, suggest_point, mutual_friends, status)
VALUES (1, 2, 20, 0, 'FRIEND'),
       (1, 3, 10, 0, 'BLOCK'),
       (2, 3, 10, 0, 'NONE');
