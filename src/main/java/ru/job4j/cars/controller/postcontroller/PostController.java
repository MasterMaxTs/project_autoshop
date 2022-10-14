package ru.job4j.cars.controller.postcontroller;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import ru.job4j.cars.controller.ManageSession;
import ru.job4j.cars.entity.Engine;
import ru.job4j.cars.entity.Post;
import ru.job4j.cars.entity.User;
import ru.job4j.cars.service.carservice.carbrandservice.CarBrandService;
import ru.job4j.cars.service.postservice.PostFilter;
import ru.job4j.cars.service.postservice.PostService;
import ru.job4j.cars.service.userservice.UserService;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Optional;

@Controller
@AllArgsConstructor
public class PostController implements ManageSession {

    private final PostService postService;
    private final PostFilter postFilter;
    private final CarBrandService carBrandService;
    private final UserService userService;

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
        model.addAttribute("posts", postService.findAll());
        return "post/post-all";
    }

    @GetMapping("/posts/user/{userId}")
    public String allByUserId(@PathVariable("userId") int id, Model model) {
        User user = (User) model.getAttribute("user");
        model.addAttribute("userPosts", postService.findAllByUserId(id));
        return "post/post-all-by-user";
    }

    @GetMapping("/posts/new")
    public String formAddPost(@ModelAttribute Post post, Model model) {
        model.addAttribute("brands", carBrandService.findAll());
        return "post/post-create";
    }

    @PostMapping("/addPost")
    public String add(@ModelAttribute Post post,
                      @ModelAttribute Engine engine
                      ) {

        return "redirect:/index";
    }
}
