package ru.job4j.cars.service.userservice;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.job4j.cars.entity.User;
import ru.job4j.cars.repository.userrepository.UserRepository;

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
    public Optional<User> findById(int id) {
        return store.findById(id);
    }

    @Override
    public Optional<User> findByLoginAndPassword(User user) {
        return store.findByLoginAndPassword(user);
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
