package ru.job4j.cars.service.user;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.job4j.cars.exception.IllegalUserEmailException;
import ru.job4j.cars.exception.IllegalUserLoginException;
import ru.job4j.cars.exception.IllegalUserNameException;
import ru.job4j.cars.model.User;
import ru.job4j.cars.repository.users.UserRepository;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository store;

    @Override
    public List<User> findAll() {
        return store.findAll();
    }

    @Override
    public User create(User user) {
        Optional<User> userOptional = store.findByEmail(user);
        if (user.getName().equals(GUEST)) {
            throw new IllegalUserNameException(
                    String.format(
                            "The new user named '%s' cannot be stored in the "
                                    + "database", user.getName())
            );
        }
        if (userOptional.isPresent()) {
            throw new IllegalUserEmailException(
                    String.format(
                            "The new user with email '%s' cannot be stored in"
                                    + " the database", user.getEmail())
            );
        }
        userOptional = store.findByLogin(user);
        if (userOptional.isPresent()) {
            throw new IllegalUserLoginException(
                    String.format(
                            "The new user with login '%s' cannot be stored in"
                                    + " the database", user.getLogin())
            );
        }
        return store.create(user);
    }

    @Override
    public void update(User user) {
        store.update(user);
    }

    @Override
    public void delete(User user) {
        store.delete(user);
    }

    @Override
    public void deleteAll() {
        store.deleteAll();
    }

    @Override
    public Optional<User> findById(int id) {
        return store.findById(id);
    }

    @Override
    public Optional<User> findByLoginAndPassword(User user) {
        Optional<User> userOptional = store.findByLoginAndPassword(user);
        if (userOptional.isEmpty()) {
            throw new IllegalArgumentException(
                    String.format(
                            "The user with login = %s and password = %s"
                                    + " was not found in the database",
                            user.getLogin(), user.getPassword()
                    )
            );
        }
        return userOptional;
    }

    @Override
    public Optional<User> findByLogin(User user) {
        return store.findByLogin(user);
    }

    @Override
    public Optional<User> findByEmail(User user) {
        return store.findByEmail(user);
    }
}
