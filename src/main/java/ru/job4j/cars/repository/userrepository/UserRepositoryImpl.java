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

    @Override
    public List<User> findAll() {
        return crudRepository.query("from User", User.class);
    }

    @Override
    public User create(User user) {
        crudRepository.run(session -> session.save(user));
        return user;
    }

    @Override
    public void update(User user) {
        crudRepository.run(session -> session.merge(user));
    }

    @Override
    public void delete(User user) {
        crudRepository.run(session -> session.delete(user));
    }

    @Override
    public void deleteAll() {
        crudRepository.run("delete from User", Map.of());
    }

    @Override
    public Optional<User> findById(int id) {
        return crudRepository.optional(
                "from User where id = :fId",
                User.class,
                Map.of("fId", id)
        );
    }

    @Override
    public Optional<User> findByLoginAndPassword(User user) {
        return crudRepository.optional(
                "from User where login = :fLogin and password = :fPwd",
                User.class,
                Map.of("fLogin", user.getLogin(), "fPwd", user.getPassword())
        );
    }

    @Override
    public Optional<User> findByLogin(User user) {
        return crudRepository
                .optional(
                        "from User where login = :fLogin",
                        User.class,
                        Map.of("fLogin", user.getLogin())
                );
    }

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
