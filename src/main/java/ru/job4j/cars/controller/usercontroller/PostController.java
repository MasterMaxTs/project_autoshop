package ru.job4j.cars.controller.usercontroller;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import ru.job4j.cars.controller.ManageSession;
import ru.job4j.cars.entity.User;
import ru.job4j.cars.service.postservice.PostService;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
@AllArgsConstructor
public class PostController implements ManageSession {

    private final PostService postService;

    @ModelAttribute("user")
    User addUserToModels(HttpSession session) {
        return getUserFromSession(session);
    }

    @GetMapping("/index")
    public String index() {
        return "redirect:/posts";
    }

    @GetMapping("/posts")
    public String all(Model model) {
        User user = (User) model.getAttribute("user");
        model.addAttribute("posts", user.getId() == 0
                             ? List.of() : postService.findAll(user)
        );
        return "post/post-all";
    }
}
