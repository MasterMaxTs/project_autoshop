CREATE TABLE IF NOT EXISTS auto_engines
(
    id SERIAL PRIMARY KEY,
    name VARCHAR(20),
    power INT NOT NULL
);