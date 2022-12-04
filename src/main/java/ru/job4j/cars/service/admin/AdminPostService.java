package ru.job4j.cars.service.admin;

/**
 * Сервис администрирования публикаций пользователей
 */
public interface AdminPostService {

    /**
     * Удаляет все публикации пользователей, помеченные как архивные
     */
    void deleteAllArchivedPosts();
}
