package ru.job4j.cars.service.admin;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.job4j.cars.model.Post;
import ru.job4j.cars.repository.posts.PostRepository;

/**
 * Данный класс реализует функционал администрирования объявлений сайта
 * @author Maxim Tsurkanov
 */
@Service
@AllArgsConstructor
public class AdminPostServiceImpl implements AdminPostService {

    private PostRepository store;

    /**
     * Удалить из базы все объявления, попавшие в архив
     */
    @Override
    public void deleteAllArchivedPosts() {
        store.findAll().stream()
                .filter(Post::isSold)
                .forEach(store::delete);
    }
}
