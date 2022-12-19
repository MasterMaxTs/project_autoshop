package ru.job4j.cars.repository.post;

import ru.job4j.cars.model.Post;
import ru.job4j.cars.repository.Repository;

import java.util.List;

/**
 * Хранилище публикаций
 * @see ru.job4j.cars.model.Post
 */
public interface PostRepository extends Repository<Post> {

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
}
