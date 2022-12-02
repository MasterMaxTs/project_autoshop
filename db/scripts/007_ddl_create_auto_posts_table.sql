CREATE TABLE IF NOT EXISTS auto_posts
(
    id      SERIAL PRIMARY KEY,
    text    TEXT NOT NULL,
    created TIMESTAMP WITHOUT TIME ZONE DEFAULT now(),
    updated TIMESTAMP WITHOUT TIME ZONE DEFAULT now(),
    saled   TIMESTAMP WITHOUT TIME ZONE,
    is_sold BOOLEAN                     DEFAULT false,
    user_id INT REFERENCES auto_users (id),
    car_id  INT UNIQUE REFERENCES auto_cars (id)
);


comment on table auto_posts is 'Публикации';
comment on column auto_posts.id is 'Идентификатор объявления - первичный ключ';
comment on column auto_posts.text is 'Текст объявления';
comment on column auto_posts.created is 'Локальное время публикации объявления на сайте';
comment on column auto_posts.updated is 'Локальное время обновления объявления на сайте';
comment on column auto_posts.is_sold is 'Статус объявления';
comment on column auto_posts.user_id is 'Идентификатор владельца объявления - внешний ключ';
comment on column auto_posts.car_id is 'Идентификатор автомобиля - внешний ключ';

