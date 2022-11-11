CREATE TABLE IF NOT EXISTS auto_users
(
    id       SERIAL PRIMARY KEY,
    name     VARCHAR(20) NOT NULL,
    phone    VARCHAR(20) NOT NULL,
    email    VARCHAR(30) NOT NULL,
    login    VARCHAR(20) NOT NULL UNIQUE,
    password VARCHAR(20) NOT NULL,
    UNIQUE (login, email)
);

CREATE TABLE IF NOT EXISTS auto_posts
(
    id      SERIAL PRIMARY KEY,
    text    TEXT NOT NULL,
    created TIMESTAMP WITHOUT TIME ZONE DEFAULT now(),
    is_sold BOOLEAN DEFAULT false,
    user_id INT REFERENCES auto_users (id)
);