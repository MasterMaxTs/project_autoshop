CREATE TABLE IF NOT EXISTS auto_car_brands
(
    id    SERIAL PRIMARY KEY,
    brand VARCHAR NOT NULL UNIQUE
);


comment on table auto_car_brands is 'Автобренды';
comment on column auto_car_brands.id is 'Идентификатор автобренда - первичный ключ';
comment on column auto_car_brands.brand is 'Наименование бренда';
