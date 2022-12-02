CREATE TABLE IF NOT EXISTS auto_cars
(
    id         SERIAL PRIMARY KEY,
    body_type  VARCHAR NOT NULL,
    color      VARCHAR NOT NULL,
    model_year INT     NOT NULL,
    mileage    INT     NOT NULL,
    photo      BYTEA   NOT NULL,
    brand_id   INT     NOT NULL REFERENCES auto_car_brands (id),
    engine_id  INT     NOT NULL REFERENCES auto_engines (id)
);


comment on table auto_cars is 'Автомобили';
comment on column auto_cars.id is 'Идентификатор автомобиля - первичный ключ';
comment on column auto_cars.body_type is 'Тип кузова автомобиля';
comment on column auto_cars.color is 'Цвет автомобиля';
comment on column auto_cars.model_year is 'Год выпуска автомобиля';
comment on column auto_cars.mileage is 'Пробег автомобиля';
comment on column auto_cars.photo is 'Фотография автомобиля';
comment on column auto_cars.brand_id is 'Идентификатор автобренда - внешний ключ';
comment on column auto_cars.engine_id is 'Идентификатор двигателя - внешний ключ';


