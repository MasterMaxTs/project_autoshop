package ru.job4j.cars.service.post;

import ru.job4j.cars.model.Post;
import ru.job4j.cars.model.User;
import ru.job4j.cars.service.Service;

import java.util.List;

/**
 * Сервис публикаций
 * @see ru.job4j.cars.model.Post
 */
public interface PostService extends Service<Post> {

    /**
     * Возвращает список всех публикаций, принадлежащих
     * определённому пользователю
     * @param id id пользователя
     * @return список публикаций пользователя
     */
    List<Post> findAllByUserId(int id);

    /**
     * Возвращает список всех архивных публикаций
     * @return список всех архивных публикаций
     */
    List<Post> findAllArchivedPosts();

    /**
     * Возвращает список всех избранных публикаций пользователя
     * @param user пользователь
     * @return список всех избранных публикаций пользователя
     */
    List<Post> findAllFavoritePostsByUser(User user);
}
