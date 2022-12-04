package ru.job4j.cars.service.admin;

import ru.job4j.cars.model.User;

import java.util.List;

/**
 * Сервис администрирования пользователей сайта
 */
public interface AdminUserService {

    /**
     * Находит в базе данных список заявок от пользователей,
     * поданных для удаления их профилей
     * @return список заявок
     */
    List<User> findAllDeletionRequests();

    /**
     * Удаляет профиль пользователя из базы данных
     * @param user пользователь
     */
    void delete(User user);
}
