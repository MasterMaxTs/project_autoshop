package ru.job4j.cars.service.user;

import ru.job4j.cars.model.User;
import ru.job4j.cars.service.Service;

import java.util.Optional;

/**
 * Сервис пользователей
 * @see ru.job4j.cars.model.User
 */
public interface UserService extends Service<User> {

    /**
     * Дефолтное имя пользователя, не прошедшего аутентификацию
     */
    String GUEST = "Гость";

    /**
     * Находит пользователя в базе данных по login и password
     * @param user пользователь
     * @return Optional.of(user), если пользователь найден,
     * иначе Optional.empty()
     */
    Optional<User> findByLoginAndPassword(User user);

    /**
     * Находит пользователя в базе данных по login
     * @param user пользователь
     * @return Optional.of(user), если пользователь найден,
     * иначе Optional.empty()
     */
    Optional<User> findByLogin(User user);

    /**
     * Находит пользователя в базе данных по email
     * @param user пользователь
     * @return Optional.of(user), если пользователь найден,
     * иначе Optional.empty()
     */
    Optional<User> findByEmail(User user);
}
