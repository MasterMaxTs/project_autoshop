package ru.job4j.cars.service.user;

import org.junit.Before;
import org.junit.Test;
import ru.job4j.cars.exception.IllegalUserEmailException;
import ru.job4j.cars.exception.IllegalUserLoginException;
import ru.job4j.cars.exception.IllegalUserNameException;
import ru.job4j.cars.model.User;
import ru.job4j.cars.repository.users.UserRepository;

import java.util.Optional;

import static org.mockito.Mockito.*;

/**
 * Класс используется для выполнения модульных тестов
 * слоя сервисов пользователей
 */
public class UserServiceTest {

    private UserRepository userRepository;
    private UserService userService;

    /**
     * Инициализация сервиса пользователей до выполнения тестов
     */
    @Before
    public void whenSetUp() {
        userRepository = mock(UserRepository.class);
        userService = new UserServiceImpl(userRepository);
    }

    /**
     * Тест проверяет сценарий корректной регистрации нового пользователя
     */
    @Test
    public void whenCreateUserWithCorrectRegistrationDataThatAddUserToDb() {
        User newUser = new User("userName", "phone", "email",
                "login", "password");
        doReturn(Optional.empty())
                .when(userRepository).findByLogin(newUser);
        doReturn(Optional.empty())
                .when(userRepository).findByEmail(newUser);
        userService.create(newUser);
    }

    /**
     * Тест проверяет сценарий регистрации нового пользователя c введённым name,
     * которое не может быть использовано в учётных данных,
     * с выбросом соответствующего исклдючения
     */
    @Test(expected = IllegalUserNameException.class)
    public void whenCreateUserWithIncorrectUserNameThanGetException() {
        User newUser = new User("Гость", "phone", "email",
                "login", "password");
        userService.create(newUser);
    }

    /**
     * Тест проверяет сценарий регистрации нового пользователя c введённым login,
     * который используется другим зарегистрированным пользователем
     * с выбросом соответствующего исклдючения
     */
    @Test(expected = IllegalUserLoginException.class)
    public void whenCreateUserWithIncorrectLoginThanGetException() {
        User newUser = new User("name1", "phone1", "email1",
                "login", "password1");
        User userInDb = new User("name2", "phone2", "email2",
                "login", "password2");
        doReturn(Optional.of(userInDb))
                .when(userRepository).findByLogin(newUser);
        userService.create(newUser);
    }

    /**
     * Тест проверяет сценарий регистрации нового пользователя c введённым email,
     * который используется другим зарегистрированным пользователем,
     * с выбросом соответствующего исклдючения
     */
    @Test(expected = IllegalUserEmailException.class)
    public void whenCreateUserWithIncorrectEmailThanGetException() {
        User newUser = new User("name1", "phone1", "email",
                "login1", "password1");
        User userInDb = new User("name2", "phone2", "email",
                "login2", "password2");
        doReturn(Optional.of(userInDb))
                .when(userRepository).findByEmail(newUser);
        userService.create(newUser);
    }

    /**
     * Тест проверяет сценарий успешной аутентификации пользователя
     */
    @Test
    public void whenFindUserByLoginAndPasswordThanGetNotNullUser() {
        User user = new User("name", "phone", "email",
                "login", "password");
        userService.create(user);
        doReturn(Optional.of(user))
                .when(userRepository).findByLoginAndPassword(user);
        userService.findByLoginAndPassword(user);
    }

    /**
     * Тест проверяет сценарий не успешной аутентификации пользователя
     * с выбросом соответствующего исклдючения
     */
    @Test(expected = IllegalArgumentException.class)
    public void whenFindUserByIncorrectLoginAndPasswordThanGetException() {
        User user = new User("name", "phone", "email",
                "login", "password");
        userService.create(user);
        user.setPassword("anotherPwd");
        doReturn(Optional.empty())
                .when(userRepository).findByLoginAndPassword(user);
        userService.findByLoginAndPassword(user);
    }
}