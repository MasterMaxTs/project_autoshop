package ru.job4j.cars.controller.usercontroller;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ru.job4j.cars.controller.ManageSession;
import ru.job4j.cars.entity.User;
import ru.job4j.cars.service.adminservice.AdminUserService;
import ru.job4j.cars.service.userservice.UserService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.util.Optional;

@Controller
@AllArgsConstructor
public class UserController implements ManageSession {

    private final UserService userService;
    private final AdminUserService adminUserService;

    @ModelAttribute("user")
    User addUserToModels(HttpSession session) {
        return getUserFromSession(session);
    }

    @GetMapping("/users")
    public String all(Model model) {
        model.addAttribute("users", userService.findAll());
        return "/user/user-all";
    }

    @GetMapping("/formAddUser")
    public String formAddUser(
            @RequestParam(value = "msgName", required = false) String msgName,
            @RequestParam(value = "msgLogin", required = false) String msgLogin,
            @RequestParam(value = "msgEmail", required = false) String msgEmail,
            @RequestParam(value = "flag", required = false) Boolean flag,
            Model model
    ) {
        model.addAttribute("msgName", msgName);
        model.addAttribute("msgLogin", msgLogin);
        model.addAttribute("msgEmail", msgEmail);
        model.addAttribute("flag", flag);
        return "user/user-registration";
    }

    @PostMapping("/addUser")
    public String add(@RequestParam(value = "flag", required = false) Boolean flag,
                      @ModelAttribute User user,
                      RedirectAttributes ra,
                      HttpServletRequest req
                      ) {
        if (user.getName().equals(GUEST)) {
            return getRedirectDueToInvalidUserName(flag, ra);
        }
        if (userService.findByEmail(user).isPresent() && flag == null) {
            return getRedirectDueToInvalidEmailName(flag, ra);
        }
        if (userService.findByLogin(user).isPresent() && flag == null) {
            return getRedirectDueToInvalidLoginName(flag, ra);
        }
        if (flag != null) {
            HttpSession session = req.getSession();
            userService.update(user);
            session.setAttribute("user", user);
            return "user/user-update-confirm";
        }
        user.setCreated(LocalDateTime.now());
        HttpSession session = req.getSession();
        session.setAttribute("user", userService.create(user));
        return "user/user-registration-confirm";
    }

    @GetMapping("/formUpdateUser")
    public String update(RedirectAttributes ra) {
        ra.addAttribute("flag", true);
        return "redirect:/formAddUser";
    }

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

    @GetMapping("/requests/profile-deletion")
    public String allUserForDeletion(Model model) {
        model.addAttribute("users", adminUserService.findAllDeletionRequests());
        return "user/user-all-deletion-requests";
    }

    @GetMapping("users/{userId}/delete")
    public String delete(@PathVariable("userId") int id) {
        Optional<User> userInDb = userService.findById(id);
        userInDb.ifPresent(adminUserService::delete);
        return "redirect:/users";
    }

    @GetMapping("/users/{userId}/undo-deletion")
    public String doNotDelete(@PathVariable("userId") int id) {
        Optional<User> userInDb = userService.findById(id);
        userInDb.ifPresent(user -> {
                                    user.setCheck(false);
                                    userService.update(user);
        });
        return "redirect:/users";
    }

    @GetMapping("/loginPage")
    public String formLoginPage(
                    @ModelAttribute User user,
                    @RequestParam(value = "msgFail", required = false) String msg,
                    Model model) {
        model.addAttribute("msgFail", msg);
        return "user/user-login";
    }

    @PostMapping("/login")
    public String login(@ModelAttribute User user,
                        RedirectAttributes ra,
                        HttpServletRequest req) {
        Optional<User> userInDb = userService.findByLoginAndPassword(user);
        if (userInDb.isPresent()) {
            HttpSession session = req.getSession();
            session.setAttribute("user", userInDb.get());
            return "redirect:/index";
        }
        String msg = "Некорректный login или password!";
        ra.addAttribute("msgFail", msg);
        return "redirect:/loginPage";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/loginPage";
    }

    private String getRedirectDueToInvalidLoginName(Boolean flag, RedirectAttributes ra) {
        String msg = "Пользователь с таким login уже существует в БД! "
                + "Придумайте другой login!";
        ra.addAttribute("msgLogin", msg);
        ra.addAttribute("flag", flag);
        return "redirect:/formAddUser";
    }

    private String getRedirectDueToInvalidEmailName(Boolean flag, RedirectAttributes ra) {
        String msg = "Пользователь с таким email уже существует в БД! "
                + "Введите другой email!";
        ra.addAttribute("msgEmail", msg);
        ra.addAttribute("flag", flag);
        return "redirect:/formAddUser";
    }

    private String getRedirectDueToInvalidUserName(Boolean flag, RedirectAttributes ra) {
        String msg = String.format("Пользователь с именем '%s' не может "
                + "быть создан в системе! Придумайте другое имя!", GUEST);
        ra.addAttribute("msgName", msg);
        ra.addAttribute("flag", flag);
        return "redirect:/formAddUser";
    }
}
