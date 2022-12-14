package ru.job4j.cars.controller.post;

import org.junit.Before;
import org.junit.Test;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;
import ru.job4j.cars.model.*;
import ru.job4j.cars.service.admin.AdminPostService;
import ru.job4j.cars.service.car.CarService;
import ru.job4j.cars.service.car.carbrand.CarBrandService;
import ru.job4j.cars.service.driver.DriverService;
import ru.job4j.cars.service.engine.EngineService;
import ru.job4j.cars.service.post.PostFilterService;
import ru.job4j.cars.service.post.PostService;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;

/**
 * Класс используется для выполнения модульных тестов
 * слоя контроллера публикаций
 */
public class PostControllerTest {

    private PostController postController;
    private PostService postService;
    private PostFilterService postFilterService;
    private CarBrandService carBrandService;
    private CarService carService;
    private EngineService engineService;
    private Model model;
    private MultipartFile multipartFile;
    private HttpServletRequest request;
    private List<Post> posts;
    private List<Brand> carBrands;

    /**
     * Инициализация объекта контроллера публикаций,
     * инициализвация начальных данных до выполнения тестов
     */
    @Before
    public void whenSetUp() {
        postService = mock(PostService.class);
        postFilterService = mock(PostFilterService.class);
        carBrandService = mock(CarBrandService.class);
        carService = mock(CarService.class);
        engineService = mock(EngineService.class);
        DriverService driverService = mock(DriverService.class);
        AdminPostService adminPostService = mock(AdminPostService.class);
        model = mock(Model.class);
        multipartFile = mock(MultipartFile.class);
        request = mock(HttpServletRequest.class);
        postController = new PostController(postService,
                                            postFilterService,
                                            engineService,
                                            carService,
                                            carBrandService,
                                            driverService,
                                            adminPostService);
        User user1 = new User("login1", "password1");
        user1.setId(1);
        User user2 = new User("login2", "password2");
        user2.setId(2);
        Post post1 = new Post();
        post1.setUser(user1);
        post1.setId(1);
        Post post2 = new Post();
        post2.setUser(user2);
        post2.setId(2);
        posts = List.of(post1, post2);
        carBrands = List.of(
                new Brand(1, "KIA"), new Brand(2, "Toyota")
        );
    }

    /**
     * Тест проверяет сценарий корректной вставки данных
     * в виде всех опубликованных и непроданных объявлений в объект модели
     * и корректного сопоставления вида
     */
    @Test
    public void whenFindAllUnsoldPostsThenVerifyDataInModelAndReturnViewName() {
        doReturn(posts).when(postService).findAll();
        doReturn(carBrands).when(carBrandService).findAll();
        String page = postController.allUnsold(model);
        verify(model).addAttribute("brands", carBrands);
        verify(model).addAttribute("posts", posts);
        assertThat(page, is("post/post-all"));
    }

    /**
     * Тест проверяет сценарий корректной вставки данных
     * в виде всех архивных публикаций в объект модели
     * и корректного сопоставления вида
     */
    @Test
    public void whenFindAllArchivePostsThenVerifyDataInModelAndReturnViewName() {
        Post post1 = posts.get(0);
        post1.setSold(true);
        List<Post> archived = List.of(post1);
        doReturn(archived).when(postService).findAllArchivedPosts();
        String page = postController.allArchive(model);
        verify(model).addAttribute("posts", archived);
        assertThat(page, is("/post/post-all-archived"));
    }

    /**
     * Тест проверяет сценарий корректной вставки данных
     * в виде всех публикаций пользователя в объект модели
     * и корректного сопоставления вида
     */
    @Test
    public void whenFindAllUserPostsThenVerifyDataInModelAndReturnViewName() {
        List<Post> user1Posts = List.of(posts.get(0));
        doReturn(user1Posts).when(postService).findAllByUserId(1);
        String page = postController.allByUserId(1, model);
        verify(model).addAttribute("userPosts", user1Posts);
        assertThat(page, is("post/post-all-by-user"));
    }

    /**
     * Тест проверяет сценарий корректной вставки данных
     * в виде всех публикаций, добавленных пользователем в избранное,
     * в объект модели и корректного сопоставления вида
     */
    @Test
    public void whenFindAllUserSubscriptionsToPostsThenVerifyDataInModelAndReturnViewName() {
        User current = posts.get(0).getUser();
        Post favorite = posts.get(1);
        List<Post> favoritePosts = List.of(favorite);
        doReturn(current).when(model).getAttribute("user");
        doReturn(favoritePosts).when(postService).findAllFavoritePostsByUser(current);
        String page = postController.allUserSubscriptionsToPosts(model);
        verify(model).addAttribute("subs", favoritePosts);
        assertThat(page, is("post/post-all-subscriptions"));
    }

    /**
     * Тест проверяет сценарий корректной вставки данных
     * в виде отфильтрованных за текущие сутки публикаций в объект модели
     * и корректного сопоставления вида
     */
    @Test
    public void whenFindAllFilteredPostsForLastDayThenVerifyDataInModelAndReturnViewName() {
        Post post1 = posts.get(0);
        List<Post> filteredPosts = List.of(post1);
        doReturn(filteredPosts).when(postFilterService).findAllForLastDay();
        String page = postController.allForLastDay(model);
        verify(model).addAttribute("posts", filteredPosts);
        verify(model).addAttribute("filter2", true);
        assertThat(page, is("post/post-all"));
    }

    /**
     * Тест проверяет сценарий корректной вставки данных
     * в виде отфильтрованных по параметрам автомобиля публикаций
     * в объект модели и корректного сопоставления вида
     */
    @Test
    public void whenFindAllFilteredPostsByParametersThenVerifyDataInModelAndReturnViewName() {
        Post post1 = posts.get(0);
        List<Post> filteredPosts = List.of(post1);
        String[] filterParameters = {"KIA", "Sedan", "2018", "50000", "AT", "1.6"};
        doReturn(filterParameters).when(request).getParameterValues("p");
        doReturn(filteredPosts)
                .when(postFilterService).findAllByParameters(
                                            filterParameters[0],
                                            filterParameters[1],
                                            Integer.parseInt(filterParameters[2]),
                                            Integer.parseInt(filterParameters[3]),
                                            filterParameters[4],
                                            filterParameters[5]);
        String page = postController.allByParameters(request, model);
        verify(model).addAttribute("posts", filteredPosts);
        verify(model).addAttribute("filter1", true);
        assertThat(page, is("post/post-all"));
    }

    /**
     * Тест проверяет сценарий корректной вставки данных
     * в виде отфильтрованных по бренду и диапазону цены автомобиля публикаций
     * в объект модели и корректного сопоставления вида
     */
    @Test
    public void whenFindAllFilteredPostsByCarBrandAndPriceThenVerifyDataInModelAndReturnViewName() {
        Post post1 = posts.get(0);
        List<Post> filteredPosts = List.of(post1);
        String[] filterParameters = {"KIA", "600000", "900000"};
        doReturn(filterParameters).when(request).getParameterValues("p");
        doReturn(filteredPosts)
                .when(postFilterService).findAllByCarBrandAndPrice(
                                    filterParameters[0],
                                    Integer.parseInt(filterParameters[1]),
                                    Integer.parseInt(filterParameters[2])
                );
        String page = postController.allByCarBrandAndPrice(request, model);
        verify(model).addAttribute("posts", filteredPosts);
        verify(model).addAttribute("filter3", true);
        assertThat(page, is("post/post-all"));
    }

    /**
     * Тест проверяет сценарий корректной вставки данных
     * в виде публикации в объект модели и корректного сопоставления вида
     */
    @Test
    public void whenShowPostThenVerifyDataInModelAndReturnViewName() {
        Post show = posts.get(0);
        int id = show.getId();
        doReturn(Optional.of(show)).when(postService).findById(id);
        String page = postController.showPost(id, model);
        verify(model).addAttribute("post", show);
        assertThat(page, is("/post/post-id"));
    }

    /**
     * Тест проверяет сценарий корректной вставки данных
     * и корректного сопоставления вида для формы создания новой публикации
     */
    @Test
    public void whenGetFormForAddPostThenVerifyDataInModelAndReturnViewName() {
        doReturn(carBrands).when(carBrandService).findAll();
        String page = postController.formAddPost(new Post(), model);
        verify(model).addAttribute("brands", carBrands);
        assertThat(page, is("post/post-create"));
    }

    /**
     * Тест проверяет сценарий отработки метода по созданию новой публикации
     * и корректного сопоставления вида
     */
    @Test
    public void whenAddPostThenVerifyDataAndReturnViewName() {
        Post input = new Post();
        String page = postController.add(input,
                                         new Engine(),
                                         new Car(),
                                         new Price(),
                                         1,
                                         multipartFile,
                                         request,
                                         model);
        verify(postService).create(input);
        assertThat(page, is("redirect:/index"));
    }

    /**
     * Тест проверяет сценарий корректной вставки данных
     * и корректного сопоставления вида для формы обновления публикации
     */
    @Test
    public void whenGetFormForUpdatePostThenVerifyDataInModelAndReturnViewName() {
        Post updatedPost = posts.get(0);
        int id = updatedPost.getId();
        doReturn(Optional.of(updatedPost)).when(postService).findById(id);
        String page = postController.formUpdatePost(id, model);
        verify(model).addAttribute("post", updatedPost);
        assertThat(page, is("/post/post-update"));
    }

    /**
     * Тест проверяет сценарий отработки метода по обновлению публикации
     * и корректного сопоставления вида
     */
    @Test
    public void whenUpdatePostThenVerifyDataAndReturnViewName() throws IOException {
        Post updatedPost = posts.get(0);
        int id = updatedPost.getId();
        Engine engine = new Engine();
        Car car = new Car();
        car.setEngine(engine);
        updatedPost.setCar(car);
        doReturn("1").when(request).getParameter("post.id");
        doReturn("white").when(request).getParameter("color");
        doReturn(new byte[]{}).when(multipartFile).getBytes();
        doReturn("50000").when(request).getParameter("mileage");
        doReturn("newDesc").when(request).getParameter("text");
        doReturn(Optional.of(updatedPost)).when(postService).findById(id);
        String page = postController.update(new Engine(),
                                            new Price(),
                                            multipartFile,
                                            request);
        verify(engineService).update(engine);
        verify(carService).update(car);
        verify(postService).update(updatedPost);
        assertThat(page, is("redirect:/index"));
    }

    /**
     * Тест проверяет сценарий отработки метода по удалению публикации
     * и корректного сопоставления вида
     */
    @Test
    public void whenDeletePostThenVerifyDataAndReturnViewName() {
        Post deletedPost = posts.get(0);
        int id = deletedPost.getId();
        doReturn(Optional.of(deletedPost)).when(postService).findById(id);
        String page = postController.delete(id);
        verify(postService).delete(deletedPost);
        assertThat(page, is("redirect:/index"));
    }
}