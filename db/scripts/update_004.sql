CREATE TABLE IF NOT EXISTS participants
(
    id      SERIAL PRIMARY KEY,
    post_id INT REFERENCES auto_post (id),
    user_id INT REFERENCES auto_user (id)
);