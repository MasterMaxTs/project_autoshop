package ru.job4j.cars.repository.userrepository;

import ru.job4j.cars.entity.User;
import ru.job4j.cars.repository.ManageRepository;

import java.util.Optional;

public interface UserRepository extends ManageRepository<User> {

    Optional<User> findByLoginAndPassword(User user);

    Optional<User> findByLogin(User user);
    Optional<User> findByEmail(User user);

}
