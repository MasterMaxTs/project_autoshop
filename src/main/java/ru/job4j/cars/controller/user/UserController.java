package ru.job4j.cars.controller.user;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ru.job4j.cars.controller.SessionControl;
import ru.job4j.cars.exception.IllegalUserEmailException;
import ru.job4j.cars.exception.IllegalUserLoginException;
import ru.job4j.cars.exception.IllegalUserNameException;
import ru.job4j.cars.model.User;
import ru.job4j.cars.service.admin.AdminUserService;
import ru.job4j.cars.service.user.UserService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.util.Optional;

/**
 * Контроллер пользователей
 */
@Controller
@AllArgsConstructor
public class UserController implements SessionControl {

    /**
     * Ссылки на слои сервисов
     */
    private final UserService userService;
    private final AdminUserService adminUserService;

    /**
     * Добавляет пользователя во все модели,
     * определённые в контроллере пользователей
     * @param session HttpSession
     * @return пользователя из текущей сессии
     */
    @ModelAttribute("user")
    User addUserToModels(HttpSession session) {
        return getUserFromSession(session);
    }

    /**
     * Добавляет в модель всех пользователей, найденных в БД сайта
     * @param model Model
     * @return вид /user/user-all
     */
    @GetMapping("/users")
    public String all(Model model) {
        model.addAttribute("users", userService.findAll());
        return "/user/user-all";
    }

    /**
     * Предоставляет регистрационную форму для нового пользователя сайта
     * @param msgName сообщение ошибки о регистрационном имени пользователя
     * @param msgLogin сообщение ошибки о регистрационном login пользователя
     * @param msgEmail сообщение ошибки о регистрационном email пользователя
     * @param flag метка, указывающая какая операция будет выполнена:
     * flag == null - регистрация нового пользователя;
     * flag == true - обновление данных зарегистрированного пользователя
     * @param model Model
     * @return вид user/user-registration
     */
    @GetMapping("/formAddUser")
    public String formAddUser(
            @RequestParam(value = "msgName", required = false) String msgName,
            @RequestParam(value = "msgLogin", required = false) String msgLogin,
            @RequestParam(value = "msgEmail", required = false) String msgEmail,
            @RequestParam(value = "flag", required = false) Boolean flag,
            Model model) {
        model.addAttribute("msgName", msgName);
        model.addAttribute("msgLogin", msgLogin);
        model.addAttribute("msgEmail", msgEmail);
        model.addAttribute("flag", flag);
        return "user/user-registration";
    }

    /**
     * Добавляет нового пользователя,
     * обновляет данные о пользователе в БД сайта
     * @param flag метка, указывающая какая операция будет выполнена:
     * flag == null - сохранение в БД нового пользователя;
     * flag == true - обновление данных пользователя в БД
     * @param user пользователь
     * @param ra RedirectAttributes
     * @param req HttpServletRequest
     * @return соотвествующий вид
     */
    @PostMapping("/addUser")
    public String add(@RequestParam(value = "flag", required = false) Boolean flag,
                      @ModelAttribute User user,
                      RedirectAttributes ra,
                      HttpServletRequest req
                      ) {
        if (flag != null) {
            try {
                userService.update(user);
                HttpSession session = req.getSession();
                session.setAttribute("user", user);
            } catch (IllegalUserNameException exception) {
                return getRedirectDueToInvalidUserName(flag, ra, exception);
            } catch (IllegalUserEmailException exception) {
                return getRedirectDueToInvalidEmailName(flag, ra, exception);
            }
                return "user/user-update-confirm";
        }
        try {
            user.setCreated(LocalDateTime.now());
            User savedUser = userService.create(user);
            HttpSession session = req.getSession();
            session.setAttribute("user", savedUser);
        } catch (IllegalUserNameException exception) {
            return getRedirectDueToInvalidUserName(flag, ra, exception);
        } catch (IllegalUserEmailException exception) {
            return getRedirectDueToInvalidEmailName(flag, ra, exception);
        } catch (IllegalUserLoginException exception) {
            return getRedirectDueToInvalidLoginName(flag, ra, exception);
        }
        return "user/user-registration-confirm";
    }

    /**
     * Предоставляет регистрационную форму пользователю сайта
     * для обновления его данных
     * @param ra RedirectAttributes
     * @return перенаправление на страницу с регистрационной формой
     * /formAddUser
     */
    @GetMapping("/formUpdateUser")
    public String update(RedirectAttributes ra) {
        ra.addAttribute("flag", true);
        return "redirect:/formAddUser";
    }

    /**
     * Помечает профиль пользователя для дальнейшего удаления администратором сайта
     * @param id id пользователя
     * @return вид /user/user-deletion-request-confirm
     */
    @GetMapping("/users/{userId}/delete-request")
    public String addUserToProfileDeletionRequests(@PathVariable("userId") int id) {
        Optional<User> userInDb = userService.findById(id);
        userInDb.ifPresent(user -> {
                                    user.setCheck(true);
                                    user.setCheckCreated(LocalDateTime.now());
                                    userService.update(user);
        });
        return "/user/user-deletion-request-confirm";
    }

    /**
     * Добавляет в модель всех пользователей, у которых профиль помечен
     * для дальнейшего удаления
     * @param model Model
     * @return вид user/user-all-deletion-requests
     */
    @GetMapping("/requests/profile-deletion")
    public String allUserForDeletion(Model model) {
        model.addAttribute("users", adminUserService.findAllDeletionRequests());
        return "user/user-all-deletion-requests";
    }

    /**
     * Удаляет пользователя из БД по id
     * @param id id пользователя
     * @return перенаправление на страницу со всеми пользователями сайта
     * /users
     */
    @GetMapping("users/{userId}/delete")
    public String delete(@PathVariable("userId") int id) {
        Optional<User> userInDb = userService.findById(id);
        userInDb.ifPresent(adminUserService::delete);
        return "redirect:/users";
    }

    /**
     * Отменяет (снимает метку) заявку на удаление профиля пользователя
     * (роль - Администратор)
     * @param id id пользователя
     * @return перенаправление на страницу со всеми пользователями сайта
     * /users
     */
    @GetMapping("/users/{userId}/undo-deletion")
    public String doNotDelete(@PathVariable("userId") int id) {
        Optional<User> userInDb = userService.findById(id);
        userInDb.ifPresent(user -> {
                                    user.setCheck(false);
                                    userService.update(user);
        });
        return "redirect:/users";
    }

    /**
     * Предоставляет пользователю форму аутентификации на сайте
     * @param user пользователь
     * @param msg сообщение ошибки о некорректном вводе login и password
     * @param model Model
     * @return вид user/user-login
     */
    @GetMapping("/loginPage")
    public String formLoginPage(
                    @ModelAttribute User user,
                    @RequestParam(value = "msgFail", required = false) String msg,
                    Model model) {
        model.addAttribute("msgFail", msg);
        return "user/user-login";
    }

    /**
     * Выполняет процедуру аутентификации пользователя сайта
     * @param user пользователь
     * @param ra RedirectAttributes
     * @param req HttpServletRequest
     * @return перенаправление на главную страницу сайта
     * /index, если аутентификация прошла успешно,
     * /loginPage в противном случае
     */
    @PostMapping("/login")
    public String login(@ModelAttribute User user,
                        RedirectAttributes ra,
                        HttpServletRequest req) {
        try {
            Optional<User> userInDb = userService.findByLoginAndPassword(user);
            userInDb.ifPresent(u -> {
                                     HttpSession session = req.getSession();
                                     session.setAttribute("user", u);
            });
        } catch (IllegalArgumentException exception) {
            ra.addAttribute("msgFail",
                exception.getMessage() + "Повторите ввод login и password!"
            );
            return "redirect:/loginPage";
        }
        return "redirect:/index";
    }

    /**
     * Выполняет процедуру завершения сессии
     * @param session HttpSession
     * @return перенаправление на форму аутентификации
     * /loginPage
     */
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/loginPage";
    }

    /**
     * Возвращает перенаправление на регистрационную форму в случае,
     * когда введённый пользователем login не валиден
     * @param flag метка, указывающая какая операция будет выполнена:
     * flag == null - сохранение в БД нового пользователя;
     * flag == true - обновление данных пользователя в БД
     * @param ra RedirectAttributes
     * @param ex Exception
     * @return перенаправление на регистрационную форму
     * /formAddUser
     */
    private String getRedirectDueToInvalidLoginName(Boolean flag,
                                                    RedirectAttributes ra,
                                                    Exception ex) {
        ra.addAttribute(
                "msgLogin",
                ex.getMessage() + "Придумайте другой login!"
        );
        ra.addAttribute("flag", flag);
        return "redirect:/formAddUser";
    }

    /**
     * Возвращает перенаправление на регистрационную форму в случае,
     * когда введённый пользователем email не валиден
     * @param flag метка, указывающая какая операция будет выполнена:
     * flag == null - сохранение в БД нового пользователя;
     * flag == true - обновление данных пользователя в БД
     * @param ra RedirectAttributes
     * @param ex Exception
     * @return перенаправление на регистрационную форму
     * /formAddUser
     */
    private String getRedirectDueToInvalidEmailName(Boolean flag,
                                                    RedirectAttributes ra,
                                                    Exception ex) {
        ra.addAttribute(
                "msgEmail",
                ex.getMessage() + "Введите другой email!"
        );
        ra.addAttribute("flag", flag);
        return "redirect:/formAddUser";
    }

    /**
     * Возвращает перенаправление на регистрационную форму в случае,
     * когда введённое пользователем имя не валидно
     * @param flag метка, указывающая какая операция будет выполнена:
     * flag == null - сохранение в БД нового пользователя;
     * flag == true - обновление данных пользователя в БД
     * @param ra RedirectAttributes
     * @param ex Exception
     * @return перенаправление на регистрационную форму
     * /formAddUser
     */
    private String getRedirectDueToInvalidUserName(Boolean flag,
                                                   RedirectAttributes ra,
                                                   Exception ex) {
        ra.addAttribute(
                "msgName",
                ex.getMessage() + "Придумайте другое имя!"
        );
        ra.addAttribute("flag", flag);
        return "redirect:/formAddUser";
    }
}
