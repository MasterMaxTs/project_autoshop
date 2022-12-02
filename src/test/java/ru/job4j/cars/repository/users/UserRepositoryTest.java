package ru.job4j.cars.repository.users;

import org.hibernate.SessionFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import ru.job4j.cars.Job4jCarsApplication;
import ru.job4j.cars.model.User;
import ru.job4j.cars.repository.CrudRepository;
import ru.job4j.cars.repository.crud.CrudRepositoryImpl;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;


public class UserRepositoryTest {

    private static SessionFactory sessionFactory;
    private UserRepository userRepository;

    @BeforeClass
    public static void initSessionFactory() {
        sessionFactory = new Job4jCarsApplication().sf();
    }

    @Before
    public void whenSetUp() {
        CrudRepository crudRepo = new CrudRepositoryImpl(sessionFactory);
        userRepository = new UserRepositoryImpl(crudRepo);
    }

    @After
    public void wipeTable() {
        userRepository.deleteAll();
    }

    @Test
    public void whenCreateSomeUsersThanUserRepoHasAll() {
        User user1 = new User("name1", "phone1", "email1",
                "login1", "password1");
        User user2 = new User("name2", "phone2", "email2",
                "login2", "password2");
        List<User> users = List.of(user1, user2);
        users.forEach(userRepository::create);
        List<User> rsl = userRepository.findAll();
        assertEquals(2, rsl.size());
        assertThat(rsl.get(0).getId(), is(user1.getId()));
        assertThat(rsl.get(1).getId(), is(user2.getId()));
    }

    @Test
    public void whenCreateNewUserThanUserRepoHasSameUser() {
        User user = new User("name", "phone", "email",
                "login", "password");
        userRepository.create(user);
        Optional<User> rsl = userRepository.findById(user.getId());
        assertTrue(rsl.isPresent());
        assertThat(rsl.get().getLogin(), is("login"));
    }

    @Test
    public void whenUpdateUserThanUserRepoHasSameUser() {
        User user = new User("name", "phone", "email",
                "login", "password");
        userRepository.create(user);
        int id = user.getId();
        Optional<User> userFromDb = userRepository.findById(id);
        userFromDb.ifPresent(u -> user.setPassword("newPass"));
        userRepository.update(user);
        Optional<User> rsl = userRepository.findById(id);
        assertThat(rsl.get().getPassword(), is("newPass"));
    }

    @Test
    public void whenDeleteSingleUserThanUserRepoHasEmpty() {
        User user = new User("name", "phone", "email",
                "login", "password");
        userRepository.create(user);
        userRepository.delete(user);
        Optional<User> rsl = userRepository.findById(user.getId());
        assertTrue(rsl.isEmpty());
    }


    @Test
    public void whenFindUserByIdThanUserRepoHasSameUser() {
        User user = new User("name", "phone", "email",
                "login", "password");
        userRepository.create(user);
        Optional<User> rsl = userRepository.findById(user.getId());
        assertThat(rsl.get().getLogin(), is("login"));
    }

    @Test
    public void whenFindUserByIncorrectIdThanUserRepoHasEmpty() {
        User user = new User("name", "phone", "email",
                "login", "password");
        userRepository.create(user);
        int id = user.getId();
        Optional<User> rsl = userRepository.findById(id + 1);
        assertTrue(rsl.isEmpty());
    }

    @Test
    public void whenFindUserByLoginThanUserRepoIsNotEmpty() {
        User user = new User("name", "phone", "email",
                "login", "password");
        userRepository.create(user);
        user.setLogin("login");
        Optional<User> rsl = userRepository.findByLogin(user);
        assertTrue(rsl.isPresent());
    }

    @Test
    public void whenFindUserByIncorrectLoginThanUserRepoHasEmpty() {
        User user = new User("name", "phone", "email",
                "login", "password");
        userRepository.create(user);
        user.setLogin("otherLogin");
        Optional<User> rsl = userRepository.findByLogin(user);
        assertTrue(rsl.isEmpty());
    }

    @Test
    public void whenFindUserByCorrectEmailThanUserRepoIsNotEmpty() {
        User user = new User("name", "phone", "email",
                "login", "password");
        user.setEmail("email");
        userRepository.create(user);
        Optional<User> rsl = userRepository.findByEmail(user);
        assertTrue(rsl.isPresent());
    }

    @Test
    public void whenFindUserByIncorrectEmailThanUserRepoHasEmpty() {
        User user = new User("name", "phone", "email",
                "login", "password");
        userRepository.create(user);
        user.setEmail("otherEmail");
        Optional<User> rsl = userRepository.findByEmail(user);
        assertTrue(rsl.isEmpty());
    }
}