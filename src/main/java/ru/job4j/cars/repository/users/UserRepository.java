package ru.job4j.cars.repository.users;

import ru.job4j.cars.model.User;
import ru.job4j.cars.repository.ManageRepository;

import java.util.Optional;

/**
 * Хранилище пользователей
 * @see ru.job4j.cars.model.User
 */
public interface UserRepository extends ManageRepository<User> {

    Optional<User> findByLoginAndPassword(User user);

    Optional<User> findByLogin(User user);
    Optional<User> findByEmail(User user);

}
