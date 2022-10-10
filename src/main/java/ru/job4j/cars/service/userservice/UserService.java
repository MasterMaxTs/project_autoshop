package ru.job4j.cars.service.userservice;

import ru.job4j.cars.entity.User;
import ru.job4j.cars.service.ManageService;

import java.util.Optional;

public interface UserService extends ManageService<User> {

    Optional<User> findByLoginAndPassword(User user);

    Optional<User> findByLogin(User user);

    Optional<User> findByEmail(User user);
}
