CREATE TABLE IF NOT EXISTS auto_cars
(
    id SERIAL PRIMARY KEY,
    body_type VARCHAR(20) NOT NULL,
    color VARCHAR(20) NOT NULL,
    model_year INT NOT NULL,
    mileage INT NOT NULL,
    photo BYTEA NOT NULL,
    brand_id INT NOT NULL REFERENCES auto_car_brands(id),
    engine_id INT NOT NULL REFERENCES auto_engines(id)
);

