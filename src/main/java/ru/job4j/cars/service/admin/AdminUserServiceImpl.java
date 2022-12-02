package ru.job4j.cars.service.admin;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.job4j.cars.model.User;
import ru.job4j.cars.repository.users.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Данный класс реализует функционал администрирования пользователей сайта
 * @author Maxim Tsurkanov
 */
@Service
@AllArgsConstructor
public class AdminUserServiceImpl implements AdminUserService {

    private final UserRepository store;

    /**
     * Найти в базе все заявки на удаление профелей пользователей
     */
    @Override
    public List<User> findAllDeletionRequests() {
        return store.findAll()
                            .stream()
                            .filter(User::isCheck)
                            .collect(Collectors.toList());
    }

    /**
     * Удалить пользователя из базы
     * @param user пользователь
     */
    @Override
    public void delete(User user) {
        store.delete(user);
    }
}
