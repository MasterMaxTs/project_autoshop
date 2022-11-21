package ru.job4j.cars.repository.userrepository;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.job4j.cars.entity.User;
import ru.job4j.cars.repository.CrudRepository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
@AllArgsConstructor
public class UserRepositoryImpl implements UserRepository {

    private final CrudRepository crudRepository;

    /**
     * Найти в базе всех зарегистрированных пользователей
     * @return список пользователей
     */
    @Override
    public List<User> findAll() {
        return crudRepository.query("from User", User.class);
    }

    /**
     * Добавить пользователя в базу
     * @param user пользователь
     * @return пользователь с id
     */
    @Override
    public User create(User user) {
        crudRepository.run(session -> session.save(user));
        return user;
    }

    /**
     * Обновить данные пользователя в базе
     * @param user пользователь
     */
    @Override
    public void update(User user) {
        crudRepository.run(session -> session.merge(user));
    }

    /**
     * Удалить пользователя из базы
     * @param user пользователь
     */
    @Override
    public void delete(User user) {
        crudRepository.run(session -> session.delete(user));
    }

    /**
     * Удалить из базы всех пользователей
     */
    @Override
    public void deleteAll() {
        crudRepository.run("delete from User", Map.of());
    }

    /**
     * Найти пользователя в базе по ID
     * @param id пользователя
     * @return пользователя в виде Optional
     */
    @Override
    public Optional<User> findById(int id) {
        return crudRepository.optional(
                "from User where id = :fId",
                User.class,
                Map.of("fId", id)
        );
    }

    /**
     * Найти пользователя в базе по login и password
     * @param user пользователь
     * @return пользователя в виде Optional
     */
    @Override
    public Optional<User> findByLoginAndPassword(User user) {
        return crudRepository.optional(
                "from User where login = :fLogin and password = :fPwd",
                User.class,
                Map.of("fLogin", user.getLogin(), "fPwd", user.getPassword())
        );
    }

    /**
     * Найти пользователя в базе по login
     * @param user пользователь
     * @return пользователя в виде Optional
     */
    @Override
    public Optional<User> findByLogin(User user) {
        return crudRepository
                .optional(
                        "from User where login = :fLogin",
                        User.class,
                        Map.of("fLogin", user.getLogin())
                );
    }

    /**
     * Найти пользователя в базе по email
     * @param user пользователь
     * @return пользователя в виде Optional
     */
    @Override
    public Optional<User> findByEmail(User user) {
        return crudRepository
                .optional(
                        "from User where email = :fEmail",
                        User.class,
                        Map.of("fEmail", user.getEmail())
                );
    }
}
