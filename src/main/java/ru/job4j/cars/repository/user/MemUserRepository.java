package ru.job4j.cars.repository.user;

import net.jcip.annotations.ThreadSafe;
import org.springframework.stereotype.Repository;
import ru.job4j.cars.model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Реализация локального потокобезопасного хранилища пользователей
 */
@Repository
@ThreadSafe
public class MemUserRepository implements UserRepository {

    private final Map<Integer, User> users;
    private final AtomicInteger atomicInteger;

    /**
     * Конструктор - создание нового объекта с определенными значениями.
     * Инициализация локального хранилища в виде ConcurrentHashMap
     */
    public MemUserRepository() {
        this.users = new ConcurrentHashMap<>();
        this.atomicInteger = new AtomicInteger(1);
    }

    @Override
    public List<User> findAll() {
        return new ArrayList<>(users.values());
    }

    @Override
    public User create(User user) {
        user.setId(atomicInteger.getAndIncrement());
        return users.putIfAbsent(user.getId(), user);
    }

    @Override
    public void update(User user) {
        users.replace(user.getId(), user);
    }

    @Override
    public void delete(User user) {
        users.remove(user.getId());
    }

    @Override
    public void deleteAll() {
        users.clear();
    }

    @Override
    public Optional<User> findById(int id) {
        return Optional.ofNullable(users.get(id));
    }

    @Override
    public Optional<User> findByLoginAndPassword(User user) {
        return users.values()
                .stream()
                .filter(
                        u -> u.getLogin().equals(user.getLogin())
                                & u.getPassword().equals(user.getPassword()))
                .findFirst();
    }

    @Override
    public Optional<User> findByLogin(User user) {
        return users.values()
                .stream()
                .filter(
                        u -> u.getLogin().equals(user.getLogin()))
                .findFirst();
    }

    @Override
    public Optional<User> findByEmail(User user) {
        return users.values()
                .stream()
                .filter(
                        u -> u.getEmail().equals(user.getEmail()))
                .findFirst();
    }
}
