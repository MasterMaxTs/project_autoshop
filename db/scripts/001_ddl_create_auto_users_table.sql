CREATE TABLE IF NOT EXISTS auto_users
(
    id                       SERIAL PRIMARY KEY,
    name                     VARCHAR NOT NULL,
    phone                    VARCHAR NOT NULL,
    email                    VARCHAR NOT NULL,
    login                    VARCHAR NOT NULL UNIQUE,
    password                 VARCHAR NOT NULL,
    created                  TIMESTAMP WITHOUT TIME ZONE DEFAULT now(),
    deletion_request         BOOLEAN                     DEFAULT false,
    deletion_request_created TIMESTAMP WITHOUT TIME ZONE,
    UNIQUE (login, email)
);


comment on table auto_users is 'Пользователи';
comment on column auto_users.id is 'Идентификатор пользователя - первичный ключ';
comment on column auto_users.name is 'Имя пользователя';
comment on column auto_users.phone is 'Сотовый телефон пользователя';
comment on column auto_users.login is 'Логин пользователя';
comment on column auto_users.password is 'Пароль пользователя';
comment on column auto_users.created is 'Локальное время регистрации пользователя на сайте';
comment on column auto_users.deletion_request is 'Подан ли запрос на удаление профиля пользователя';
comment on column auto_users.deletion_request_created is 'Локальное время регистрации заявки, поданной пользователем для удаления профиля';