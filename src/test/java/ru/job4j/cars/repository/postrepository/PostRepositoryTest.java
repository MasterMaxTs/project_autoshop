package ru.job4j.cars.repository.postrepository;

import org.hibernate.SessionFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import ru.job4j.cars.Job4jCarsApplication;
import ru.job4j.cars.entity.*;
import ru.job4j.cars.repository.CrudRepository;
import ru.job4j.cars.repository.carrepository.CarRepository;
import ru.job4j.cars.repository.carrepository.CarRepositoryImpl;
import ru.job4j.cars.repository.carrepository.carbrandrepo.CarBrandRepository;
import ru.job4j.cars.repository.carrepository.carbrandrepo.CarBrandRepositoryImpl;
import ru.job4j.cars.repository.crudrepository.CrudRepositoryImpl;
import ru.job4j.cars.repository.driverrepository.DriverRepository;
import ru.job4j.cars.repository.driverrepository.DriverRepositoryImpl;
import ru.job4j.cars.repository.userrepository.UserRepository;
import ru.job4j.cars.repository.userrepository.UserRepositoryImpl;
import ru.job4j.cars.service.adminservice.AdminPostService;
import ru.job4j.cars.service.adminservice.AdminPostServiceImpl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

public class PostRepositoryTest {

    private static SimpleDateFormat sdf;
    private static SessionFactory sessionFactory;
    private CrudRepository crudRepository;
    private UserRepository userRepository;
    private PostRepository postRepository;
    private PostRepoFilter postRepoFilter;
    private CarBrandRepository carBrandRepository;
    private CarRepository carRepository;
    private DriverRepository driverRepository;
    private AdminPostService adminPostService;
    private Post post1;
    private Post post2;

    @BeforeClass
    public static void init() {
        sessionFactory = new Job4jCarsApplication().sf();
        sdf = new SimpleDateFormat("dd-MM-yyyy");
    }

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
                new Engine("1.6","AT", "front-wheel", 123),
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
        List<PriceHistory> priceHistories1 = List.of(
                new PriceHistory(
                        600000,
                        LocalDateTime.of(
                                2022, 10, 1, 10, 30)
                )
        );
        String description1 = "Sale1";
        post1 = addPostToDb(user1, car1, description1 ,priceHistories1, participants1);
        User user2 = new User("name2", "phone2", "email2",
                "login2", "password2");
        List<User> participants2 = List.of(user1);
        int brandId2 = 2;
        Car car2 = new Car(
                "black",
                "Hatchback",
                2015,
                200000,
                new Engine("2.0","MT", "front-wheel", 150),
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
        List<PriceHistory> priceHistories2 = List.of(
                new PriceHistory(
                        400000,
                        LocalDateTime.of(
                                2012, 12, 31, 23, 59)
                ),
                new PriceHistory(
                        350000,
                        LocalDateTime.of(
                                2013, 3, 8, 9, 0)
                )

        );
        String description2 = "Sale2";
        post2 = addPostToDb(user2, car2, description2 ,priceHistories2, participants2);
    }

    @After
    public void wipeTables() {
        deleteAllFromPriceHistoryTable();
        postRepository.deleteAll();
        carRepository.deleteAll();
        driverRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    public void whenCreateNewPostThanPostRepoHasSamePost() {
        Optional<Post> postFromDb = postRepository.findById(post2.getId());
        assertTrue(postFromDb.isPresent());
        Iterator<User> iterator =
                postFromDb.get().getParticipants().iterator();
        User participant = iterator.next();
        assertThat(
                    postFromDb.get().getUser().getLogin()
                    , is(post2.getUser().getLogin())
        );
        assertThat(
                     postFromDb.get().getCar().getBrand().getName()
                    , is(post2.getCar().getBrand().getName())
        );
        assertThat(
                    postFromDb.get().getCar().getEngine().getTransmission()
                    , is(post2.getCar().getEngine().getTransmission())
        );
        assertThat(
                     postFromDb.get().getPriceHistoryList().get(1).getPrice()
                    , is(350000)
        );
        assertThat(
                     participant.getLogin()
                    , is("login1")
        );
    }

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

    @Test
    public void whenDeleteAllPostsThanPostRepoHasEmpty() {
        postRepository.delete(post1);
        postRepository.delete(post2);
        List<Post> rsl = postRepository.findAll();
        List<Driver> listOwners = driverRepository.findAll();
        assertTrue(rsl.isEmpty());
        assertTrue(listOwners.isEmpty());
    }

    @Test
    public void whenAdminDeleteAllArchivedPostsThanTheyAreNotInPostRepo() {
        post1.setSold(true);
        postRepository.update(post1);
        adminPostService.deleteAllArchivedPosts();
        assertTrue(postRepository.findById(post1.getId()).isEmpty());
        assertTrue(postRepository.findById(post2.getId()).isPresent());
    }

    @Test
    public void whenFindAllPostsByUserIdThanGetUserPost() {
        List<Post> rsl =
                postRepository.findAllByUserId(post1.getUser().getId());
        assertThat(rsl.get(0).getText(), is("Sale1"));
        assertThat(rsl.get(0).getCar().getModelYear(), is(2018));
        assertThat(rsl.size(), is(1));
    }

    @Test
    public void whenCreateSomePostsThanPostRepoHasAll() {
        List<Post> rsl = postRepository.findAll();
        assertThat(rsl.get(0).getCar().getModelYear(), is(2018));
        assertThat(rsl.get(1).getCar().getModelYear(), is(2015));
        assertThat(rsl.size(), is(2));
    }

    @Test
    public void whenFindAllPostsForLastDayThanGetFilteredPost() {
        int postId = post1.getId();
        Optional<Post> postFromDb = postRepository.findById(postId);
        assertTrue(postFromDb.isPresent());
        Post post1 = postFromDb.get();
        post1.setCreated(post1.getCreated().minusDays(2L));
        postRepository.update(post1);
        List<Post> rsl = postRepoFilter.findAllForLastDay();
        assertThat(rsl.size(), is(1));
        assertThat(rsl.get(0).getCar().getColor(), is("black"));
        assertThat(rsl.get(0).getText(), is("Sale2"));
    }

    @Test
    public void whenFindAllPostsByParametersThanGetFilteredPost() {
        List<Post> rsl = postRepoFilter.findAllByParameters(
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

    @Test
    public void whenFindAllPostsByParametersThanGetEmptyList() {
        List<Post> rsl = postRepoFilter.findAllByParameters(
                                                        "Toyota",
                                                        "Sedan",
                                                        2020,
                                                        40000,
                                                        "AT",
                                                        "1.6"
                                                                      );
        assertTrue(rsl.isEmpty());
    }

    @Test
    public void whenFindAllPostsByCarBrandAndPriceThanGetFilteredPost() {
        List<Post> rsl = postRepoFilter.findAllByCarBrandAndPrice(
                "Ford", 300000, 600000
        );
        assertThat(rsl.size(), is(1));
        assertThat(rsl.get(0).getCar().getBrand().getName(), is("Ford"));
        assertThat(rsl.get(0).getPriceHistoryList().get(1).getPrice(), is(350000));
    }

    @Test
    public void whenFindAllPostsByCarBrandAndPriceThanGetEmptyList() {
        List<Post> rsl = postRepoFilter.findAllByCarBrandAndPrice(
                "Ford", 400000, 600000
        );
        assertTrue(rsl.isEmpty());
    }

    @Test
    public void whenAddPostToArchiveThanPostRepoHasSameArchivedPost() {
        post1.setSold(true);
        postRepository.update(post1);
        List<Post> rsl = findAllArchivedPosts();
        assertThat(rsl.size(), is(1));
        assertThat(rsl.get(0).getCar().getBrand().getId(), is(1));
    }

    private void initRepositories() {
        crudRepository = new CrudRepositoryImpl(sessionFactory);
        userRepository = new UserRepositoryImpl(crudRepository);
        postRepository = new PostRepositoryImpl(crudRepository);
        postRepoFilter = new PostRepositoryImpl(crudRepository);
        carBrandRepository = new CarBrandRepositoryImpl(crudRepository);
        carRepository = new CarRepositoryImpl(crudRepository);
        driverRepository = new DriverRepositoryImpl(crudRepository);
        adminPostService = new AdminPostServiceImpl(postRepository);
    }

    private void deleteAllFromPriceHistoryTable() {
        crudRepository.run("delete from PriceHistory", Map.of());
    }

    private List<Post> findAllArchivedPosts() {
        return postRepository.findAll()
                                    .stream()
                                    .filter(Post::isSold)
                                    .collect(Collectors.toList());
    }

    private void addBrandToCar(int brandId, Car car) {
        Optional<CarBrand> carBrandFromDB = carBrandRepository.findById(brandId);
        carBrandFromDB.ifPresent(car::setBrand);
    }

    private void addOwnersToCar(User user, Car car, List<Driver> owners) {
        owners.forEach(driver -> {
                                    driver.setUser(user);
                                    car.addCarDriver(driver);}
        );
    }

    private void addPriceHistoriesToPost(List<PriceHistory> priceHistories, Post post) {
        priceHistories.forEach(ph -> {
                                        ph.setPost(post);
                                        post.addPriceHistoryToList(ph);}
        );
    }

    private Post addPostToDb(User user, Car car, String description,
                             List<PriceHistory> priceHistories,
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