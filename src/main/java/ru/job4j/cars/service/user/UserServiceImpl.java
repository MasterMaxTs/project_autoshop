package ru.job4j.cars.service.user;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.job4j.cars.exception.IllegalUserEmailException;
import ru.job4j.cars.exception.IllegalUserLoginException;
import ru.job4j.cars.exception.IllegalUserNameException;
import ru.job4j.cars.model.User;
import ru.job4j.cars.repository.users.UserRepository;

import java.util.List;
import java.util.Optional;

/**
 * Реализация сервиса пользователей
 */
@Service
public class UserServiceImpl implements UserService {

    /**
     * Делегирование выполнения CRUD-операций хранилищу пользователей
     */
    private final UserRepository store;

    public UserServiceImpl(@Qualifier("userRepositoryImpl") UserRepository store) {
        this.store = store;
    }

    @Override
    public List<User> findAll() {
        return store.findAll();
    }

    @Override
    public User create(User user) {
        if (user.getName().equals(GUEST)) {
            throw new IllegalUserNameException(
                    String.format(
                            "Пользователь с указанным именем '%s'"
                                    + "не может быть зарегистрирован. ",
                            user.getName())
            );
        }
        Optional<User> userOptional = findByLogin(user);
        if (userOptional.isPresent()) {
            throw new IllegalUserLoginException(
                    String.format(
                            "Пользователь с указанным login '%s' уже существует"
                    + " и не может быть зарегистрирован. ", user.getEmail())
            );
        }
        userOptional = findByEmail(user);
        if (userOptional.isPresent()) {
            throw new IllegalUserEmailException(
                    String.format(
                            "Пользователь с указанным email '%s' уже существует"
                                    + " и не может быть зарегистрирован. ",
                            user.getEmail())
            );
        }
        return store.create(user);
    }

    @Override
    public void update(User user) {
        if (user.getName().equals(GUEST)) {
            throw new IllegalUserNameException(
                    String.format(
                            "Пользователь с указанным именем '%s'"
                                    + "не может быть зарегистрирован. ",
                            user.getName())
            );
        }
        Optional<User> optionalUser = findByEmail(user);
        if (optionalUser.isPresent() && optionalUser.get().getId() != user.getId()) {
            throw new IllegalUserEmailException(
                    String.format(
                            "Пользователь с указанным email '%s' уже "
                                    + "существует и не может быть зарегистрирован. ",
                            user.getEmail())
            );
        }
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
                            "Пользователь с указанным login = %s и password"
                                    + "= %s не найден. ",
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
