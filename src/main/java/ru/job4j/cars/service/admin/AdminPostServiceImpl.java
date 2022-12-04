package ru.job4j.cars.service.admin;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.job4j.cars.model.Post;
import ru.job4j.cars.repository.posts.PostRepository;

/**
 * Данный класс реализует функционал администрирования публикаций
 * пользователей сайта
 * @author Maxim Tsurkanov
 */
@Service
@AllArgsConstructor
public class AdminPostServiceImpl implements AdminPostService {

    /**
     * Делегирование выполнения CRUD-операций хранилищу публикаций
     */
    private PostRepository store;

    @Override
    public void deleteAllArchivedPosts() {
        store.findAll().stream()
                .filter(Post::isSold)
                .forEach(store::delete);
    }
}
