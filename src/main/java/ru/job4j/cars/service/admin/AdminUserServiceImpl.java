package ru.job4j.cars.service.admin;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.job4j.cars.model.User;
import ru.job4j.cars.repository.user.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Данный класс реализует функционал администрирования пользователей сайта
 * @author Maxim Tsurkanov
 */
@Service
public class AdminUserServiceImpl implements AdminUserService {

    /**
     * Делегирование выполнения CRUD-операций хранилищу пользователей
     */
    private final UserRepository store;

    public AdminUserServiceImpl(@Qualifier("userRepositoryImpl") UserRepository store) {
        this.store = store;
    }

    @Override
    public List<User> findAllDeletionRequests() {
        return store.findAll()
                            .stream()
                            .filter(User::isCheck)
                            .collect(Collectors.toList());
    }

    @Override
    public void delete(User user) {
        store.delete(user);
    }
}
