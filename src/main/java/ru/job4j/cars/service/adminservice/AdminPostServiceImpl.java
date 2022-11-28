package ru.job4j.cars.service.adminservice;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.job4j.cars.entity.Post;
import ru.job4j.cars.repository.postrepository.PostRepository;

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
