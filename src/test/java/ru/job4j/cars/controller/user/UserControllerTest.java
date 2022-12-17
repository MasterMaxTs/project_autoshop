package ru.job4j.cars.controller.user;

import org.junit.Before;
import org.junit.Test;
import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ru.job4j.cars.exception.IllegalUserEmailException;
import ru.job4j.cars.exception.IllegalUserLoginException;
import ru.job4j.cars.exception.IllegalUserNameException;
import ru.job4j.cars.model.User;
import ru.job4j.cars.service.admin.AdminUserService;
import ru.job4j.cars.service.user.UserService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;

/**
 * Класс используется для выполнения модульных тестов
 * слоя контроллера пользователей
 */
public class UserControllerTest {

    private UserService userService;
    private AdminUserService adminUserService;
    private UserController userController;
    private Model model;
    private RedirectAttributes redirectAttributes;
    private HttpServletRequest request;
    private HttpSession session;
    private List<User> users;

    /**
     * Инициализация объекта контроллера пользователей,
     * инициализвация начальных данных до выполнения тестов
     */
    @Before
    public void whenSetUp() {
        userService = mock(UserService.class);
        adminUserService = mock(AdminUserService.class);
        model = mock(Model.class);
        redirectAttributes = mock(RedirectAttributes.class);
        request = mock(HttpServletRequest.class);
        session = mock(HttpSession.class);
        userController = new UserController(userService, adminUserService);
        User admin = new User("Admin", "77777777777",
                "adm@test.ru", "admin", "adm123");
        User user = new User("User1", "11111111111",
                "user1@test.ru", "user1", "password1");
        users = List.of(admin, user);
    }

    /**
     * Тест проверяет сценарий корректной вставки данных
     * в виде всех зарегистрированных пользователей на сайте в объект модели
     * и корректного сопоставления вида
     */
    @Test
    public void whenFindAllUsersThenVerifyDataInModelAndReturnViewName() {
        doReturn(users).when(userService).findAll();
        String page = userController.all(model);
        verify(model).addAttribute("users", users);
        assertThat(page, is("/user/user-all"));
    }

    /**
     * Тест проверяет сценарий отработки метода по созданию нового пользователя,
     * когда введены корректные регистрационные данные,
     * и сопоставления вида
     */
    @Test
    public void whenAddNewUserWithCorrectRegisterDataThenVerifyDataAndReturnViewName() {
        User input = users.get(1);
        doReturn(Optional.empty()).when(userService).findByLogin(input);
        doReturn(Optional.empty()).when(userService).findByEmail(input);
        doReturn(session).when(request).getSession();
        String page = userController.add(null,
                                         input,
                                         redirectAttributes,
                                         request);
        verify(userService).create(input);
        assertThat(page, is("user/user-registration-confirm"));
    }

    /**
     * Тест проверяет сценарий отработки метода по созданию нового пользователя,
     * когда введено некорректное имя, и сопоставления вида
     */
    @Test
    public void whenAddNewUserWithIncorrectUserNameThenVerifyDataAndReturnViewName() {
        User input = users.get(1);
        input.setName("Гость");
        doThrow(IllegalUserNameException.class).when(userService).create(input);
        String page = userController.add(null,
                                         input,
                                         redirectAttributes,
                                         request);
        verify(userService).create(input);
        assertThat(page, is("redirect:/formAddUser"));
    }

    /**
     * Тест проверяет сценарий отработки метода по созданию нового пользователя,
     * когда введен некорректный login, и сопоставления вида
     */
    @Test
    public void whenAddNewUserWithIncorrectUserLoginThenVerifyDataAndReturnViewName() {
        User input = users.get(1);
        input.setLogin("admin");
        doThrow(IllegalUserLoginException.class).when(userService).create(input);
        String page = userController.add(null,
                                         input,
                                         redirectAttributes,
                                         request);
        verify(userService).create(input);
        assertThat(page, is("redirect:/formAddUser"));
    }

    /**
     * Тест проверяет сценарий отработки метода по созданию нового пользователя,
     * когда введен некорректный email, и сопоставления вида
     */
    @Test
    public void whenAddNewUserWithIncorrectUserEmailThenVerifyDataAndReturnViewName() {
        User input = users.get(1);
        input.setEmail("adm@test.ru");
        doThrow(IllegalUserEmailException.class).when(userService).create(input);
        String page = userController.add(null,
                                         input,
                                         redirectAttributes,
                                         request);
        verify(userService).create(input);
        assertThat(page, is("redirect:/formAddUser"));
    }

    /**
     * Тест проверяет сценарий отработки метода по обновлению данных
     * пользователя, когда данные введены корректно, и сопоставления вида
     */
    @Test
    public void whenUpdateUserWithCorrectRegisterDataThenVerifyDataAndReturnViewName() {
        User updatedUser = users.get(0);
        updatedUser.setName("Adm");
        updatedUser.setPhone("88888888888");
        updatedUser.setEmail("admin@test.ru");
        updatedUser.setPassword("newPass");
        doReturn(session).when(request).getSession();
        String page = userController.add(true,
                                         updatedUser,
                                         redirectAttributes,
                                         request);
        verify(userService).update(updatedUser);
        assertThat(page, is("user/user-update-confirm"));
    }

    /**
     * Тест проверяет сценарий отработки метода по обновлению данных
     * пользователя, когда введено некорректное имя, и сопоставления вида
     */
    @Test
    public void whenUpdateUserWithIncorrectUserNameThenVerifyDataAndReturnViewName() {
        User updatedUser = users.get(0);
        updatedUser.setName("Гость");
        updatedUser.setPassword("adm123");
        doThrow(IllegalUserNameException.class).when(userService).update(updatedUser);
        String page = userController.add(true,
                                         updatedUser,
                                         redirectAttributes,
                                         request);
        verify(userService).update(updatedUser);
        assertThat(page, is("redirect:/formAddUser"));
    }

    /**
     * Тест проверяет сценарий отработки метода по обновлению данных
     * пользователя, когда введен некорректный email, и сопоставления вида
     */
    @Test
    public void whenUpdateUserWithIncorrectEmailThenVerifyDataAndReturnViewName() {
        User admin = users.get(0);
        User updatedUser = users.get(1);
        updatedUser.setEmail(admin.getEmail());
        doThrow(IllegalUserEmailException.class).when(userService).update(updatedUser);
        String page = userController.add(true,
                                         updatedUser,
                                         redirectAttributes,
                                         request);
        verify(userService).update(updatedUser);
        assertThat(page, is("redirect:/formAddUser"));
    }

    /**
     * Тест проверяет сценарий отработки метода по удалению пользователя
     * и корректного сопоставления вида
     */
    @Test
    public void whenDeleteUserThenVerifyDataAndReturnViewName() {
        User deletedUser = users.get(1);
        int id = deletedUser.getId();
        doReturn(Optional.of(deletedUser)).when(userService).findById(id);
        String page = userController.delete(id);
        verify(adminUserService).delete(deletedUser);
        assertThat(page, is("redirect:/users"));
    }

    /**
     * Тест проверяет сценарий корректной вставки данных
     * в виде всех пользователей, подавших заявки на удаления профиля, в объект
     * модели и корректного сопоставления вида
     */
    @Test
    public void whenFindAllUsersForDeletionThenVerifyDataInModelAndReturnViewName() {
        List<User> allDeletion = List.of(users.get(1));
        doReturn(allDeletion).when(adminUserService).findAllDeletionRequests();
        String page = userController.allUserForDeletion(model);
        verify(model).addAttribute("users", allDeletion);
        assertThat(page, is("user/user-all-deletion-requests"));
    }
}