CREATE TABLE IF NOT EXISTS auto_cars
(
    id SERIAL PRIMARY KEY,
    name VARCHAR(50),
    photo BYTEA,
    engine_id INT NOT NULL REFERENCES auto_engines(id)
);

