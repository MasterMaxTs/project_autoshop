CREATE TABLE IF NOT EXISTS auto_engines
(
    id SERIAL PRIMARY KEY,
    name VARCHAR(20),
    volume VARCHAR(3) NOT NULL,
    transmission VARCHAR(5) NOT NULL,
    drive_unit VARCHAR(20) NOT NULL,
    power INT NOT NULL
);