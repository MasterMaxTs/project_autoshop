package ru.job4j.cars.repository.posts;

import org.hibernate.SessionFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import ru.job4j.cars.Job4jCarsApplication;
import ru.job4j.cars.model.*;
import ru.job4j.cars.repository.CrudRepository;
import ru.job4j.cars.repository.cars.CarRepository;
import ru.job4j.cars.repository.cars.CarRepositoryImpl;
import ru.job4j.cars.repository.cars.carbrands.CarBrandRepository;
import ru.job4j.cars.repository.cars.carbrands.CarBrandRepositoryImpl;
import ru.job4j.cars.repository.crud.CrudRepositoryImpl;
import ru.job4j.cars.repository.drivers.DriverRepository;
import ru.job4j.cars.repository.drivers.DriverRepositoryImpl;
import ru.job4j.cars.repository.users.UserRepository;
import ru.job4j.cars.repository.users.UserRepositoryImpl;
import ru.job4j.cars.service.admin.AdminPostService;
import ru.job4j.cars.service.admin.AdminPostServiceImpl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

/**
 * Класс используется для выполнения интеграционных тестов при выявлении
 * правильного взаимодействия приложения с хранилищем публикаций
 */
public class PostRepositoryTest {

    private static SimpleDateFormat sdf;
    private static SessionFactory sessionFactory;
    private CrudRepository crudRepository;
    private UserRepository userRepository;
    private PostRepository postRepository;
    private PostFilterRepository postFilterRepository;
    private CarBrandRepository carBrandRepository;
    private CarRepository carRepository;
    private DriverRepository driverRepository;
    private AdminPostService adminPostService;
    private Post post1;
    private Post post2;

    /**
     * Инициализация объекта SessionFactory,
     * иницализация объекта SimpleDateFormat на весь период тестирования
     */
    @BeforeClass
    public static void init() {
        sessionFactory = new Job4jCarsApplication().sf();
        sdf = new SimpleDateFormat("dd-MM-yyyy");
    }

    /**
     * Инициализация хранилищ пользователей и публикаций, вставка начальных
     * данных до выполнения тестов: добавление двух публикаций от двух разных
     * пользователей
     */
    @Before
    public void whenSetUpThatAddTwoPostsIntoDb() throws ParseException {
        initRepositories();
        User user1 = new User("name1", "phone1", "email1",
                "login1", "password1");
        List<User> participants1 = List.of();
        int brandId1 = 1;
        Car car1 = new Car(
                "slick silver",
                "Sedan",
                2018,
                30000,
                new Engine("1.6", "AT", "front-wheel", 123),
                new byte[]{}
        );
        addBrandToCar(brandId1, car1);
        List<Driver> owners1 = List.of(
                                        new Driver(
                                                "name11",
                                                sdf.parse("01-01-2020"),
                                                sdf.parse("01-12-2020"))
        );
        addOwnersToCar(user1, car1, owners1);
        List<Price> priceHistories1 = List.of(
                new Price(
                        600000,
                        LocalDateTime.of(
                                2022, 10, 1, 10, 30)
                )
        );
        String description1 = "Sale1";
        post1 = addPostToDb(user1, car1, description1, priceHistories1,
                participants1);
        User user2 = new User("name2", "phone2", "email2",
                "login2", "password2");
        List<User> participants2 = List.of(user1);
        int brandId2 = 2;
        Car car2 = new Car(
                "black",
                "Hatchback",
                2015,
                200000,
                new Engine("2.0", "MT", "front-wheel", 150),
                new byte[]{}
        );
        addBrandToCar(brandId2, car2);
        List<Driver> owners2 = List.of(
                new Driver(
                        "name21",
                        sdf.parse("01-01-2007"),
                        sdf.parse("01-12-2010")),
                new Driver(
                        "name22",
                        sdf.parse("02-12-2010"),
                        sdf.parse("01-01-2015"))
        );
        addOwnersToCar(user2, car2, owners2);
        List<Price> priceHistories2 = List.of(
                new Price(
                        400000,
                        LocalDateTime.of(
                                2012, 12, 31, 23, 59)
                ),
                new Price(
                        350000,
                        LocalDateTime.of(
                                2013, 3, 8, 9, 0)
                )

        );
        String description2 = "Sale2";
        post2 = addPostToDb(user2, car2, description2, priceHistories2,
                participants2);
    }

    /**
     * Очистка всех данных из связанных тестируемых таблиц H2Database после
     * завершения каждого теста
     */
    @After
    public void wipeTables() {
        deleteAllFromPriceHistoryTable();
        postRepository.deleteAll();
        carRepository.deleteAll();
        driverRepository.deleteAll();
        userRepository.deleteAll();
    }

    /**
     * Тест проверяет сценарий корректного сохранения публикаций в
     * хранилище публикаций и правильного извлечения из него
     */
    @Test
    public void whenCreateNewPostThanPostRepoHasSamePost() {
        Optional<Post> postFromDb1 = postRepository.findById(post1.getId());
        Optional<Post> postFromDb2 = postRepository.findById(post2.getId());
        assertTrue(postFromDb1.isPresent() && postFromDb2.isPresent());
        Iterator<User> iterator =
                postFromDb2.get().getParticipants().iterator();
        User participant = iterator.next();
        assertThat(
                    postFromDb1.get().getUser().getLogin(),
                    is(post1.getUser().getLogin())
        );
        assertThat(
                postFromDb2.get().getCar().getBrand().getName(),
                is(post2.getCar().getBrand().getName())
        );
        assertThat(
                    postFromDb1.get().getCar().getEngine().getTransmission(),
                    is(post1.getCar().getEngine().getTransmission())
        );
        assertThat(
                     postFromDb2.get().getPriceList().get(1).getPrice(),
                     is(350000)
        );
        assertThat(participant.getLogin(), is("login1"));
    }

    /**
     * Тест проверяет сценарий корректного обновления публикации в
     * хранилище публикаций
     */
    @Test
    public void whenUpdatePostThanPostRepoHasSamePost() {
        int postId = post1.getId();
        Optional<Post> postFromDb = postRepository.findById(postId);
        assertTrue(postFromDb.isPresent());
        Post updatedPost = postFromDb.get();
        updatedPost.getCar().setColor("dazzlingWhite");
        updatedPost.getCar().getEngine().setPower(170);
        updatedPost.setText("New sale description");
        postRepository.update(updatedPost);
        Optional<Post> rsl = postRepository.findById(postId);
        assertTrue(rsl.isPresent());
        assertThat(rsl.get().getCar().getColor(), is("dazzlingWhite"));
        assertThat(rsl.get().getCar().getEngine().getPower(), is(170));
        assertThat(rsl.get().getText(), is("New sale description"));
    }

    /**
     * Тест проверяет сценарий корректного удаления публикаций из
     * хранилища публикаций
     */
    @Test
    public void whenDeleteAllPostsThanPostRepoHasEmpty() {
        postRepository.delete(post1);
        postRepository.delete(post2);
        List<Post> rsl = postRepository.findAll();
        List<Driver> listOwners = driverRepository.findAll();
        assertTrue(rsl.isEmpty());
        assertTrue(listOwners.isEmpty());
    }

    /**
     * Тест проверяет сценарий корректного удаления всех архивных публикаций из
     * хранилища публикаций
     */
    @Test
    public void whenAdminDeleteAllArchivedPostsThanTheyAreNotInPostRepo() {
        post1.setSold(true);
        postRepository.update(post1);
        adminPostService.deleteAllArchivedPosts();
        assertTrue(postRepository.findById(post1.getId()).isEmpty());
        assertTrue(postRepository.findById(post2.getId()).isPresent());
    }

    /**
     * Тест проверяет сценарий корректного нахождения всех публикаций,
     * принадлежащих пользователю, в хранилище публикаций
     */
    @Test
    public void whenFindAllPostsByUserIdThanGetUserPost() {
        List<Post> rsl =
                postRepository.findAllByUserId(post1.getUser().getId());
        assertThat(rsl.get(0).getText(), is("Sale1"));
        assertThat(rsl.get(0).getCar().getModelYear(), is(2018));
        assertThat(rsl.size(), is(1));
    }

    /**
     * Тест проверяет сценарий корректного нахождения всех публикаций
     * в хранилище публикаций
     */
    @Test
    public void whenCreateSomePostsThanPostRepoHasAll() {
        List<Post> rsl = postRepository.findAll();
        assertThat(rsl.get(0).getCar().getModelYear(), is(2018));
        assertThat(rsl.get(1).getCar().getModelYear(), is(2015));
        assertThat(rsl.size(), is(2));
    }

    /**
     * Тест проверяет сценарий корректного нахождения отфильтрованных за
     * минувшие сутки публикаций в хранилище публикаций
     */
    @Test
    public void whenFindAllPostsForLastDayThanGetFilteredPost() {
        int postId = post1.getId();
        Optional<Post> postFromDb = postRepository.findById(postId);
        assertTrue(postFromDb.isPresent());
        Post post1 = postFromDb.get();
        post1.setCreated(post1.getCreated().minusDays(2L));
        postRepository.update(post1);
        List<Post> rsl = postFilterRepository.findAllForLastDay();
        assertThat(rsl.size(), is(1));
        assertThat(rsl.get(0).getCar().getColor(), is("black"));
        assertThat(rsl.get(0).getText(), is("Sale2"));
    }

    /**
     * Тест проверяет сценарий корректного нахождения отфильтрованных по
     * параметрам автомобиля публикаций в хранилище публикаций
     */
    @Test
    public void whenFindAllPostsByParametersThanGetFilteredPost() {
        List<Post> rsl = postFilterRepository.findAllByParameters(
                                                        "Toyota",
                                                        "Sedan",
                                                        2017,
                                                        50000,
                                                        "AT",
                                                        "1.6"
                                                                      );
        assertThat(rsl.size(), is(1));
        assertThat(rsl.get(0).getCar().getBrand().getName(), is("Toyota"));
        assertThat(rsl.get(0).getCar().getModelYear(), is(2018));
        assertThat(rsl.get(0).getCar().getMileage(), is(30000));
        assertThat(rsl.get(0).getCar().getEngine().getTransmission(), is("AT"));
        assertThat(rsl.get(0).getCar().getEngine().getVolume(), is("1.6"));
    }

    /**
     * Тест проверяет сценарий корректного нахождения отфильтрованных по
     * параметрам автомобиля публикаций в хранилище публикаций
     */
    @Test
    public void whenFindAllPostsByParametersThanGetEmptyList() {
        List<Post> rsl = postFilterRepository.findAllByParameters(
                                                        "Toyota",
                                                        "Sedan",
                                                        2020,
                                                        40000,
                                                        "AT",
                                                        "1.6"
                                                                      );
        assertTrue(rsl.isEmpty());
    }

    /**
     * Тест проверяет сценарий корректного нахождения отфильтрованных по
     * бренду автомобиля и цене публикаций в хранилище публикаций
     */
    @Test
    public void whenFindAllPostsByCarBrandAndPriceThanGetFilteredPost() {
        List<Post> rsl = postFilterRepository.findAllByCarBrandAndPrice(
                "Ford", 300000, 600000
        );
        assertThat(rsl.size(), is(1));
        assertThat(rsl.get(0).getCar().getBrand().getName(), is("Ford"));
        assertThat(rsl.get(0).getPriceList().get(1).getPrice(), is(350000));
    }

    /**
     * Тест проверяет сценарий корректного нахождения отфильтрованных по
     * бренду автомобиля и цене публикаций в хранилище публикаций
     */
    @Test
    public void whenFindAllPostsByCarBrandAndPriceThanGetEmptyList() {
        List<Post> rsl = postFilterRepository.findAllByCarBrandAndPrice(
                "Ford", 400000, 600000
        );
        assertTrue(rsl.isEmpty());
    }

    /**
     * Тест проверяет сценарий корректного нахождения архивных публикаций в
     * хранилище публикаций
     */
    @Test
    public void whenAddPostToArchiveThanPostRepoHasSameArchivedPost() {
        post1.setSold(true);
        postRepository.update(post1);
        List<Post> rsl = postRepository.findAllArchivedPosts();
        assertThat(rsl.size(), is(1));
        assertThat(rsl.get(0).getCar().getBrand().getId(), is(1));
    }

    /**
     * Выполняет инициализацию репозиториев и сервисов
     */
    private void initRepositories() {
        crudRepository = new CrudRepositoryImpl(sessionFactory);
        userRepository = new UserRepositoryImpl(crudRepository);
        postRepository = new PostRepositoryImpl(crudRepository);
        postFilterRepository = new PostRepositoryImpl(crudRepository);
        carBrandRepository = new CarBrandRepositoryImpl(crudRepository);
        carRepository = new CarRepositoryImpl(crudRepository);
        driverRepository = new DriverRepositoryImpl(crudRepository);
        adminPostService = new AdminPostServiceImpl(postRepository);
    }

    /**
     * Выполняет удаление всех данных из таблицы price_history
     */
    private void deleteAllFromPriceHistoryTable() {
        crudRepository.run("delete from Price", Map.of());
    }

    /**
     * Устанавливает бренд автомобилю
     * @param brandId id бренда
     * @param car автомобиль
     */
    private void addBrandToCar(int brandId, Car car) {
        Optional<Brand> carBrandFromDB = carBrandRepository.findById(brandId);
        carBrandFromDB.ifPresent(car::setBrand);
    }

    /**
     * Устанавливает автовладельцев автомобиля
     * @param user пользователь
     * @param car автомобиль
     * @param owners список автовладельцев
     */
    private void addOwnersToCar(User user, Car car, List<Driver> owners) {
        owners.forEach(driver -> {
                                    driver.setUser(user);
                                    car.addCarDriver(driver); }
        );
    }

    /**
     * Добавляет в публикацию хронологию изменения цены продажи
     * @param priceHistories список цен
     * @param post публикация
     */
    private void addPriceHistoriesToPost(List<Price> priceHistories, Post post) {
        priceHistories.forEach(ph -> {
                                        ph.setPost(post);
                                        post.addPriceHistoryToList(ph); }
        );
    }

    /**
     * Создаёт публикацию в базе данных
     * @param user пользователь
     * @param car автомобиль
     * @param description описание публикации
     * @param priceHistories список цен
     * @param participants список подписчиков
     * @return Создаёт публикацию в базе данных
     */
    private Post addPostToDb(User user, Car car, String description,
                             List<Price> priceHistories,
                             List<User> participants) {
        User userCreated = userRepository.create(user);
        Post post = new Post();
        post.setCreated(LocalDateTime.now());
        post.setUser(userCreated);
        post.setCar(car);
        post.setText(description);
        addPriceHistoriesToPost(priceHistories, post);
        participants.forEach(post::addParticipantToPost);
        postRepository.create(post);
        return post;
    }
}