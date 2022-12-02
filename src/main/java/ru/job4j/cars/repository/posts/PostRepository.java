package ru.job4j.cars.repository.posts;

import ru.job4j.cars.model.Post;
import ru.job4j.cars.repository.ManageRepository;

import java.util.List;

/**
 * Хранилище публикаций
 * @see ru.job4j.cars.model.Post
 */
public interface PostRepository extends ManageRepository<Post> {

    List<Post> findAllByUserId(int id);

    List<Post> findAll();
}
