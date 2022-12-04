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

@Controller
@AllArgsConstructor
public class UserController implements SessionControl {

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
            Model model) {
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
        if (flag != null) {
            HttpSession session = req.getSession();
            userService.update(user);
            session.setAttribute("user", user);
            return "user/user-update-confirm";
        }
        user.setCreated(LocalDateTime.now());
        HttpSession session = req.getSession();
        try {
            User savedUser = userService.create(user);
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
        try {
            Optional<User> userInDb = userService.findByLoginAndPassword(user);
            HttpSession session = req.getSession();
            session.setAttribute("user", userInDb.get());
        } catch (IllegalArgumentException exception) {
            String msg = "Некорректный login или password!";
            ra.addAttribute("msgFail", msg);
            return "redirect:/loginPage";
        }
        return "redirect:/index";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/loginPage";
    }

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
