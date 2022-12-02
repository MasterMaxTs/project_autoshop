CREATE TABLE IF NOT EXISTS participants
(
    post_id INT REFERENCES auto_posts (id),
    user_id INT REFERENCES auto_users (id),
    PRIMARY KEY (post_id, user_id)
);


comment on table participants is 'Подписчики';
comment on column participants.post_id is 'Идентификатор объявления - внешний ключ';
comment on column participants.user_id is 'Идентификатор пользователя - внешний ключ';