CREATE TABLE IF NOT EXISTS participants
(
    post_id INT REFERENCES auto_posts (id),
    user_id INT REFERENCES auto_users (id),
    PRIMARY KEY (post_id, user_id)
);