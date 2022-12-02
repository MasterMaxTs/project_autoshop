CREATE TABLE IF NOT EXISTS history_owners
(
    car_id    INT NOT NULL REFERENCES auto_cars (id),
    driver_id INT NOT NULL REFERENCES auto_drivers (id),
    PRIMARY KEY (car_id, driver_id)
);


comment on table history_owners is 'История владений';
comment on column history_owners.car_id is 'Идентификатор автомобиля - внешний ключ';
comment on column history_owners.driver_id is 'Идентификатор автовладельца - внешний ключ';