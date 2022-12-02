CREATE TABLE IF NOT EXISTS auto_engines
(
    id           SERIAL PRIMARY KEY,
    name         VARCHAR,
    volume       VARCHAR NOT NULL,
    transmission VARCHAR NOT NULL,
    drive_unit   VARCHAR NOT NULL,
    power        INT     NOT NULL
);


comment on table auto_engines is 'Двигатели';
comment on column auto_engines.id is 'Идентификатор двигателя - первичный ключ';
comment on column auto_engines.name is 'Наименование двигателя';
comment on column auto_engines.volume is 'Рабочий объём двигателя';
comment on column auto_engines.transmission is 'Тип коробки передач';
comment on column auto_engines.drive_unit is 'Тип привода';
comment on column auto_engines.power is 'Мощность двигателя';