package ru.job4j.cars.service.post;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.job4j.cars.model.Post;
import ru.job4j.cars.model.User;
import ru.job4j.cars.repository.post.PostFilterRepository;
import ru.job4j.cars.repository.post.PostRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Реализация сервиса публикаций с функционалом фильтрации
 */
@Service
@AllArgsConstructor
public class PostServiceImpl implements PostService, PostFilterService {

    /**
     * Делегирование выполнения CRUD-операций хранилищу публикаций
     */
    private final PostRepository store;

    /**
     * Делегирование выполнения CRUD-операций хранилищу отфильтрованных
     * публикаций
     */
    private final PostFilterRepository storeFilter;

    @Override
    public List<Post> findAllByUserId(int id) {
        return store.findAllByUserId(id);
    }

    @Override
    public List<Post> findAllArchivedPosts() {
        return store.findAllArchivedPosts();
    }

    @Override
    public List<Post> findAllFavoritePostsByUser(User user) {
        return findAll().stream()
                        .filter(p -> p.getParticipants().contains(user))
                        .collect(Collectors.toList());
    }

    @Override
    public List<Post> findAll() {
        return store.findAll();
    }

    @Override
    public Post create(Post post) {
        return store.create(post);
    }

    @Override
    public void update(Post post) {
        store.update(post);
    }

    @Override
    public void delete(Post post) {
        store.delete(post);
    }

    @Override
    public void deleteAll() {
        store.deleteAll();
    }

    @Override
    public Optional<Post> findById(int id) {
        return store.findById(id);
    }

    @Override
    public List<Post> findAllForLastDay() {
        return storeFilter.findAllForLastDay();
    }

    @Override
    public List<Post> findAllByCarBrandAndPrice(String brand,
                                                int minPrice, int maxPrice) {
        return storeFilter.findAllByCarBrandAndPrice(brand, minPrice, maxPrice);
    }

    @Override
    public List<Post> findAllByParameters(String brand,
                                          String bodyType,
                                          int modelYear,
                                          int mileage,
                                          String transmission,
                                          String volume) {
        return storeFilter.findAllByParameters(brand,
                                               bodyType,
                                               modelYear,
                                               mileage,
                                               transmission,
                                               volume);
    }
}
