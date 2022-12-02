CREATE TABLE IF NOT EXISTS auto_drivers
(
    id              SERIAL PRIMARY KEY,
    first_name      VARCHAR,
    last_name       VARCHAR,
    ownership_start DATE NOT NULL,
    ownership_end   DATE NOT NULL,
    user_id         INT REFERENCES auto_users (id)
);


comment on table auto_drivers is 'Автовладельцы';
comment on column auto_drivers.id is 'Идентификатор автовладельца - первичный ключ';
comment on column auto_drivers.first_name is 'Имя автовладельца';
comment on column auto_drivers.last_name is 'Фамилия автовладельца';
comment on column auto_drivers.ownership_start is 'Дата начала владения автомобилем';
comment on column auto_drivers.ownership_end is 'Дата окончания владения автомобилем';
comment on column auto_drivers.user_id is 'Идентификатор пользователя - внешний ключ';
