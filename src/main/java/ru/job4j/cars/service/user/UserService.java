package ru.job4j.cars.service.user;

import ru.job4j.cars.model.User;
import ru.job4j.cars.service.ManageService;

import java.util.Optional;

public interface UserService extends ManageService<User> {

    String GUEST = "Гость";

    Optional<User> findByLoginAndPassword(User user);

    Optional<User> findByLogin(User user);

    Optional<User> findByEmail(User user);
}
