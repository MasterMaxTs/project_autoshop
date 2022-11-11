CREATE TABLE IF NOT EXISTS auto_cars
(
    id SERIAL PRIMARY KEY,
    name VARCHAR(50) NOT NULL,
    body_type VARCHAR(20) NOT NULL,
    photo BYTEA NOT NULL,
    engine_id INT NOT NULL REFERENCES auto_engines(id)
);

