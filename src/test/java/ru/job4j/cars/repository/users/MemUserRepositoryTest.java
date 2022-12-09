package ru.job4j.cars.repository.users;

import org.junit.Before;
import org.junit.Test;
import ru.job4j.cars.model.User;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

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
        assertThat(store.findAll().size()).isEqualTo(2);
        assertThat(store.findAll().get(0)).isEqualToComparingFieldByField(admin);
        assertThat(store.findAll().get(1)).isEqualToComparingFieldByField(user);
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
        assertThat(rsl.isPresent()).isTrue();
        assertThat(rsl.get()).isEqualToComparingFieldByField(newUser);
    }

    /**
     * Тест проверяет сценарий корректного обновления данных
     * пользователя в хранилище пользователей
     */
    @Test
    public void whenUpdateUserThanUserStoreHasSameUser() {
        Optional<User> userInDb = store.findById(admin.getId());
        assertThat(userInDb.isPresent()).isTrue();
        User userUpdated = userInDb.get();
        userUpdated.setName("Админ");
        userUpdated.setPassword("newPass");
        store.update(userUpdated);
        userInDb = store.findById(userUpdated.getId());
        assertThat(userInDb.isPresent()).isTrue();
        assertThat(userInDb.get()).isEqualToComparingFieldByField(userUpdated);
    }

    /**
     * Тест проверяет сценарий корректного удаления пользователя
     * из хранилища пользователей
     */
    @Test
    public void whenDeleteUserThanUserStoreHasNotSameUser() {
        store.delete(user);
        assertThat(store.findById(user.getId()).isPresent()).isFalse();
    }

    /**
     * Тест проверяет сценарий корректного нахождения пользователя
     * в хранилище пользователей по ID
     */
    @Test
    public void whenFindUserByIdThanUserStoreHasSameUser() {
        Optional<User> rsl = store.findById(user.getId());
        assertThat(rsl.isPresent()).isTrue();
        assertThat(rsl.get()).isEqualToComparingFieldByField(user);
    }

    /**
     * Тест проверяет сценарий корректного нахождения пользователя
     * в хранилище пользователей по ID
     */
    @Test
    public void whenFindUserByIncorrectIdThanUserStoreHasNotSameUser() {
        assertThat(store.findById(3).isPresent()).isFalse();
    }

    /**
     * Тест проверяет сценарий корректного нахождения пользователя
     * в хранилище пользователей по его учётным данным: Login и Password
     */
    @Test
    public void whenFindUserByCorrectLoginAndPasswordThanUserStoreHasSameUser() {
        assertThat(store.findByLoginAndPassword(user).isPresent()).isTrue();
    }

    /**
     * Тест проверяет сценарий корректного нахождения пользователя
     * в хранилище пользователей по его учётным данным: Login и Password
     */
    @Test
    public void whenFindUserByInCorrectLoginAndPasswordThanUserStoreHasNotSameUser() {
        User newUser = new User("newLogin", "newPassword");
        assertThat(store.findByLoginAndPassword(newUser).isPresent()).isFalse();
    }

    /**
     * Тест проверяет сценарий корректного нахождения пользователя
     * в хранилище пользователей по его учётным данным: Login
     */
    @Test
    public void whenFindUserByCorrectLoginThanUserStoreHasSameUser() {
        assertThat(store.findByLogin(user)).isEqualTo(Optional.of(user));
    }

    /**
     * Тест проверяет сценарий корректного нахождения пользователя
     * в хранилище пользователей по его учётным данным: Login
     */
    @Test
    public void whenFindUserByInCorrectLoginThanUserStoreHasNotSameUser() {
        User newUser = new User("newLogin", "adm123");
        assertThat(store.findByLogin(newUser).isPresent()).isFalse();
    }

    /**
     * Тест проверяет сценарий корректного нахождения пользователя
     * в хранилище пользователей по его учётным данным: Email
     */
    @Test
    public void whenFindUserByCorrectEmailThanUserStoreHasSameUser() {
        assertThat(store.findByEmail(user)).isEqualTo(Optional.of(user));
    }

    /**
     * Тест проверяет сценарий корректного нахождения пользователя
     * в хранилище пользователей по его учётным данным: Email
     */
    @Test
    public void whenFindUserByInCorrectEmailThanUserStoreHasNotSameUser() {
        User newUser = new User("Админ", "phone", "newEmail",
                "admin", "adm123");
        assertThat(store.findByEmail(newUser).isPresent()).isFalse();
    }
}