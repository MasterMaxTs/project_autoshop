CREATE TABLE IF NOT EXISTS auto_user
(
    id       SERIAL PRIMARY KEY,
    login    VARCHAR(20) NOT NULL UNIQUE,
    password VARCHAR(20) NOT NULL
);

CREATE TABLE IF NOT EXISTS auto_post
(
    id           SERIAL PRIMARY KEY,
    text         TEXT,
    created      TIMESTAMP,
    auto_user_id INT REFERENCES auto_user(id)
);