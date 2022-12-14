package ru.job4j.cars.repository.users;

import org.junit.Before;
import org.junit.Test;
import ru.job4j.cars.model.User;

import java.util.Optional;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;


/**
 * Класс используется для выполнения модульных тестов
 * локального хранилища пользователей
 */
public class MemUserRepositoryTest {

    private UserRepository store;
    private User admin;
    private User user;

    /**
     * Инициализация локального хранилища пользователей,
     * добавление пользователей в хранилище до выполнения тестов
     */
    @Before
    public void whenSetUp() {
        store = new MemUserRepository();
        admin = new User("Админ", "phone", "email1",
                "admin", "adm123");
        user = new User("Пользователь", "phone2", "email2",
                "login", "password");
        store.create(admin);
        store.create(user);
    }

    /**
     * Тест проверяет сценарий корректного сохранения двух пользователей в
     * хранилище пользователей и правильного извлечения из него
     */
    @Test
    public void whenCreateSomeUsersThanUserStoreHasAll() {
        assertThat(store.findAll().size(), is(2));
        assertThat(store.findAll().get(0).getLogin(), is("admin"));
        assertThat(store.findAll().get(1).getLogin(), is("login"));
    }

    /**
     * Тест проверяет сценарий корректного сохранения
     * нового пользователя в хранилище пользователей
     */
    @Test
    public void whenCreateNewUserThanUserRepoHasSameUser() {
        User newUser = new User("newLogin", "newPwd");
        store.create(newUser);
        Optional<User> rsl = store.findById(newUser.getId());
        assertTrue(rsl.isPresent());
        assertThat(rsl.get().getLogin(), is("newLogin"));
    }

    /**
     * Тест проверяет сценарий корректного обновления данных
     * пользователя в хранилище пользователей
     */
    @Test
    public void whenUpdateUserThanUserStoreHasSameUser() {
        Optional<User> userInDb = store.findById(admin.getId());
        assertTrue(userInDb.isPresent());
        User userUpdated = userInDb.get();
        userUpdated.setName("Админ");
        userUpdated.setPassword("newPass");
        store.update(userUpdated);
        userInDb = store.findById(userUpdated.getId());
        assertTrue(userInDb.isPresent());
        assertThat(userInDb.get().getName(), is("Админ"));
        assertThat(userInDb.get().getPassword(), is("newPass"));
    }

    /**
     * Тест проверяет сценарий корректного удаления пользователя
     * из хранилища пользователей
     */
    @Test
    public void whenDeleteUserThanUserStoreHasNotSameUser() {
        store.delete(user);
        assertTrue(store.findById(user.getId()).isEmpty());
    }

    /**
     * Тест проверяет сценарий корректного нахождения пользователя
     * в хранилище пользователей по ID
     */
    @Test
    public void whenFindUserByIdThanUserStoreHasSameUser() {
        Optional<User> rsl = store.findById(user.getId());
        assertTrue(rsl.isPresent());
        assertThat(rsl.get().getLogin(), is(user.getLogin()));
    }

    /**
     * Тест проверяет сценарий корректного нахождения пользователя
     * в хранилище пользователей по ID
     */
    @Test
    public void whenFindUserByIncorrectIdThanUserStoreHasNotSameUser() {
        assertTrue(store.findById(3).isEmpty());
    }

    /**
     * Тест проверяет сценарий корректного нахождения пользователя
     * в хранилище пользователей по его учётным данным: Login и Password
     */
    @Test
    public void whenFindUserByCorrectLoginAndPasswordThanUserStoreHasSameUser() {
        assertTrue(store.findByLoginAndPassword(user).isPresent());
    }

    /**
     * Тест проверяет сценарий корректного нахождения пользователя
     * в хранилище пользователей по его учётным данным: Login и Password
     */
    @Test
    public void whenFindUserByInCorrectLoginAndPasswordThanUserStoreHasNotSameUser() {
        User newUser = new User("newLogin", "newPassword");
        assertTrue(store.findByLoginAndPassword(newUser).isEmpty());
    }

    /**
     * Тест проверяет сценарий корректного нахождения пользователя
     * в хранилище пользователей по его учётным данным: Login
     */
    @Test
    public void whenFindUserByCorrectLoginThanUserStoreHasSameUser() {
        assertEquals(Optional.of(user), store.findByLogin(user));
    }

    /**
     * Тест проверяет сценарий корректного нахождения пользователя
     * в хранилище пользователей по его учётным данным: Login
     */
    @Test
    public void whenFindUserByInCorrectLoginThanUserStoreHasNotSameUser() {
        User newUser = new User("newLogin", "adm123");
        assertTrue(store.findByLogin(newUser).isEmpty());
    }

    /**
     * Тест проверяет сценарий корректного нахождения пользователя
     * в хранилище пользователей по его учётным данным: Email
     */
    @Test
    public void whenFindUserByCorrectEmailThanUserStoreHasSameUser() {
        assertEquals(Optional.of(user), store.findByEmail(user));
    }

    /**
     * Тест проверяет сценарий корректного нахождения пользователя
     * в хранилище пользователей по его учётным данным: Email
     */
    @Test
    public void whenFindUserByInCorrectEmailThanUserStoreHasNotSameUser() {
        User newUser = new User("Админ", "phone", "newEmail",
                "admin", "adm123");
        assertTrue(store.findByEmail(newUser).isEmpty());
    }
}