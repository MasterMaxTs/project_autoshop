package ru.job4j.cars.repository.users;

import ru.job4j.cars.model.User;
import ru.job4j.cars.repository.Repository;

import java.util.Optional;

/**
 * Хранилище пользователей
 * @see ru.job4j.cars.model.User
 */
public interface UserRepository extends Repository<User> {

    /**
     * Найти пользователя в базе данных по login и password
     * @param user пользователь
     * @return Optional.of(user), если пользователь найден в базе данных,
     * иначе Optional.empty()
     */
    Optional<User> findByLoginAndPassword(User user);

    /**
     * Найти пользователя в базе данных по login
     * @param user пользователь
     * @return Optional.of(user), если пользователь найден в базе данных,
     * иначе Optional.empty()
     */
    Optional<User> findByLogin(User user);

    /**
     * Найти пользователя в базе данных по email
     * @param user пользователь
     * @return Optional.of(user), если пользователь найден в базе данных,
     * иначе Optional.empty()
     */
    Optional<User> findByEmail(User user);

}
