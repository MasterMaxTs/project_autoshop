CREATE TABLE IF NOT EXISTS price_history
(
    id      SERIAL PRIMARY KEY,
    price   BIGINT NOT NULL,
    created TIMESTAMP WITHOUT TIME ZONE DEFAULT now(),
    post_id INT REFERENCES auto_posts (id)
);


comment on table price_history is 'История цены';
comment on column price_history.id is 'Идентификатор цены - первичный ключ';
comment on column price_history.price is 'Значение цены';
comment on column price_history.created is 'Локальное время установления цены в объявлении на сайте';
comment on column price_history.post_id is 'Идентификатор объявления - внешний ключ';