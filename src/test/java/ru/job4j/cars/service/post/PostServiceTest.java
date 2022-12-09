package ru.job4j.cars.service.post;

import org.junit.Before;
import org.junit.Test;
import ru.job4j.cars.model.Post;
import ru.job4j.cars.model.User;
import ru.job4j.cars.repository.posts.PostFilterRepository;
import ru.job4j.cars.repository.posts.PostRepository;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;


/**
 * Класс используется для выполнения модульных тестов
 * слоя сервисов публикаций
 */
public class PostServiceTest {

    private PostService postService;
    private PostRepository store;

    /**
     * Инициализация сервиса публикаций до выполнения тестов
     */
    @Before
    public void whenSetUp() {
        store = mock(PostRepository.class);
        PostFilterRepository storeFilter = mock(PostFilterRepository.class);
        postService = new PostServiceImpl(store, storeFilter);
    }

    /**
     * Тест проверяет сценарий корректного нахождения публикаций,
     * добавленных пользователем в избранное
     */
    @Test
    public void whenFindAllFavoritePostsByUserThanGetThem() {
        User user1 = new User("login1", "password1");
        User user2 = new User("login2", "password2");
        Post post1 = new Post();
        post1.setUser(user1);
        post1.addParticipantToPost(user2);
        Post post2 = new Post();
        post2.setUser(user2);
        doReturn(List.of(post1, post2)).when(store).findAll();
        assertThat(
                postService.findAllFavoritePostsByUser(user2).size())
                .isEqualTo(1);
        assertThat(
                postService.findAllFavoritePostsByUser(user2).get(0))
                .isEqualToComparingFieldByField(post1);
    }
}