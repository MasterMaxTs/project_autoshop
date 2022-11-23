CREATE TABLE IF NOT EXISTS history_owners
(
    car_id INT NOT NULL REFERENCES auto_cars(id),
    driver_id INT NOT NULL REFERENCES auto_drivers(id),
    PRIMARY KEY (car_id, driver_id)
);