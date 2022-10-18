CREATE TABLE IF NOT EXISTS auto_engines
(
    id SERIAL PRIMARY KEY,
    name VARCHAR(20),
    transmission CHAR(3) NOT NULL,
    drive_unit VARCHAR(20) NOT NULL,
    power INT NOT NULL
);