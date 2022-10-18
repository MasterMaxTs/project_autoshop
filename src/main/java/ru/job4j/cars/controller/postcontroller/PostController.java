package ru.job4j.cars.controller.postcontroller;

import lombok.AllArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.job4j.cars.controller.ManageSession;
import ru.job4j.cars.entity.*;
import ru.job4j.cars.service.carservice.carbrandservice.CarBrandService;
import ru.job4j.cars.service.driverservice.DriverService;
import ru.job4j.cars.service.postservice.PostFilter;
import ru.job4j.cars.service.postservice.PostService;
import ru.job4j.cars.service.userservice.UserService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
@AllArgsConstructor
public class PostController implements ManageSession {

    private final PostService postService;
    private final PostFilter postFilter;
    private final CarBrandService carBrandService;
    private final DriverService driverService;
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
        model.addAttribute("brands", carBrandService.findAll());
        model.addAttribute("filter", "false");
        return "post/post-all";
    }

    @GetMapping("/postPhoto/{postId}")
    public ResponseEntity<Resource> downloadPhoto(@PathVariable("postId") int id) {
        ResponseEntity<Resource> rsl = null;
        Optional<Post> postInDb = postService.findById(id);
        if (postInDb.isPresent()) {
            Car car = postInDb.get().getCar();
            rsl = ResponseEntity.ok()
                    .headers(new HttpHeaders())
                    .contentLength(car.getPhoto().length)
                    .contentType(MediaType.parseMediaType("application/octet-stream"))
                    .body(new ByteArrayResource(car.getPhoto()));
        }
        return rsl;
    }

    @GetMapping("/posts/{postId}/edit")
    public String showPost(@PathVariable("postId") int id, Model model) {
        Optional<Post> postInDb = postService.findById(id);
        postInDb.ifPresent(post -> model.addAttribute("post", post));
        return "/post/post-id";
    }

    @GetMapping("/posts/{postId}/ownership-history")
    public String showOwnershipHistoryByPost(@PathVariable("postId") int id) {
        return String.format(
                "redirect:/posts/%d/edit?ownership_history=true", id
        );
    }

    @GetMapping("/posts/{postId}/by_user/edit")
    public String showUserPost(@PathVariable("postId") int id, Model model) {
        Optional<Post> postInDb = postService.findById(id);
        postInDb.ifPresent(post -> model.addAttribute("post", post));
        return "/post/post-id-user";
    }

    @GetMapping("posts/{postId}/price-history")
    public String showPriceHistoryByUserPost(@PathVariable("postId") int id) {
        return String.format(
                "redirect:/posts/%d/by_user/edit?price_history=true", id
        );
    }

    @GetMapping("posts/{postId}/update")
    public String formUpdatePost(@PathVariable("postId") int id,
                                 Model model) {
        Optional<Post> postInDb = postService.findById(id);
        postInDb.ifPresent(post -> model.addAttribute("post", post));
        return "/post/post-update";
    }


    @GetMapping("/posts/contacts/{postId}")
    public String showPostContacts(@PathVariable("postId") int id) {
        return String.format("redirect:/posts/%d/edit?contact=true", id);
    }

    @GetMapping("/posts/subs/{postId}")
    public String addSubscriptionToPost(@PathVariable("postId") int id,
                                      Model model) {
        User user = (User) model.getAttribute("user");
        Optional<Post> postInDb = postService.findById(id);
        postInDb.ifPresent(post -> {
                                    post.addParticipantToPost(user);
                                    postService.update(post); }
        );
        return "redirect:/index";
    }

    @GetMapping("posts/subscription/{postId}/remove")
    public String removeSubscriptionFromPost(@PathVariable("postId") int id,
                                             Model model) {
        User user = (User) model.getAttribute("user");
        Optional<Post> postInDb = postService.findById(id);
        postInDb.ifPresent(post -> {
                                    post.removeParticipantFromPost(user);
                                    postService.update(post); }
        );
        return "redirect:/posts/subscriptions";

    }

    @GetMapping("/posts/subscriptions")
    public String allSubscriptionsByUser(Model model) {
        User user = (User) model.getAttribute("user");
        List<Post> subsPosts = postService.findAll()
                                    .stream()
                                    .filter(p -> p.getParticipants().contains(user))
                                    .collect(Collectors.toList());
        model.addAttribute("subs", subsPosts);
        return "post/post-all-subscriptions";
    }

    @GetMapping("/posts/users/{userId}")
    public String allByUserId(@PathVariable("userId") int id,
                              Model model) {
        model.addAttribute("userPosts", postService.findAllByUserId(id));
        return "post/post-all-by-user";
    }

    @GetMapping("/posts/new")
    public String formAddPost(@ModelAttribute Post post, Model model) {
        model.addAttribute("brands", carBrandService.findAll());
        return "post/post-create";
    }

    @GetMapping("/posts/filter/last_day")
    public String allForLastDay(Model model) {
        String message = "Отфильтрованные за сутки объявления";
        model.addAttribute("posts", postFilter.findAllForLastDay());
        model.addAttribute("brands", carBrandService.findAll());
        model.addAttribute("filter2", true);
        model.addAttribute("msg", message);
        return "post/post-all";
    }

    @GetMapping("/posts/filter/by_parameters")
    public String allByParameters(HttpServletRequest req, Model model) {
        String[] parameters = req.getParameterValues("p");
        String brand = parameters[0];
        String modelYear = parameters[1];
        String mileage = parameters[2];
        String transmission = parameters[3];
        String volume = parameters[4];
        List<Post> filteredPosts = postFilter.findAllBy(brand,
                                                        modelYear,
                                                        mileage,
                                                        transmission,
                                                        volume);
        String message = "Отфильтрованные по параметрам объявления";
        Map<String, String> attributes = Map.of(
                                            "msg", message,
                                            "fBrand", brand,
                                            "fModelYear", modelYear,
                                            "fMileage", mileage,
                                            "fTransmission", transmission,
                                            "fVolume", volume);
        model.addAttribute("filter1", true);
        model.addAttribute("posts", filteredPosts);
        model.addAttribute("brands", carBrandService.findAll());
        model.mergeAttributes(attributes);
        return "post/post-all";
    }

    @PostMapping("/addPost")
    public String add(@ModelAttribute Post post,
                      @ModelAttribute Engine engine,
                      @ModelAttribute Car car,
                      @ModelAttribute PriceHistory priceHistory,
                      @RequestParam("brand.id") int brandId,
                      @RequestParam("file") MultipartFile file,
                      HttpServletRequest req,
                      Model model
                                                    ) {
        User user = (User) model.getAttribute("user");
        Optional<CarBrand> carBrandInDb = carBrandService.findById(brandId);
        carBrandInDb.ifPresent(brand -> {
                                         car.setBrand(brand);
                                         car.setEngine(engine);
                                         try {
                                             car.setPhoto(file.getBytes());
                                             addDriversToCar(car, user, req);
                                         } catch (Exception e) {
                                             throw new RuntimeException(e);
                                         }
                                         post.setCar(car); }
        );
        post.setUser(user);
        priceHistory.setPost(post);
        post.addPriceHistoryToList(priceHistory);
        post.setUpdated(post.getCreated());
        postService.create(post);
        return "redirect:/index";
    }

    private void addDriversToCar(Car car, User user, HttpServletRequest req)
                                                        throws ParseException {
        String[] driverNames = req.getParameterValues("firstname");
        String[] driverLastNames = req.getParameterValues("lastname");
        String[] starts = req.getParameterValues("start");
        String[] ends = req.getParameterValues("end");
        int length = driverNames.length;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Driver[] drivers = new Driver[length];
        for (int i = 0; i < length; i++) {
            drivers[i] = new Driver();
            drivers[i].setFirstName(driverNames[i]);
            drivers[i].setLastName(driverLastNames[i]);
            drivers[i].setOwnershipStartTime(sdf.parse(starts[i]));
            drivers[i].setOwnershipEndTime(sdf.parse(ends[i]));
        }
        Arrays.stream(drivers).forEach(driver -> {
                                                 driver.setUser(user);
                                                 car.addCarDriver(driver);
                                                 driverService.create(driver);
                                            });
    }

    @PostMapping("/updatePost")
    public String update(@ModelAttribute Engine engine,
                         @ModelAttribute Car car,
                         @ModelAttribute PriceHistory priceHistory,
                         @RequestParam("post.id") int postId,
                         @RequestParam("brand.id") int brandId,
                         @RequestParam("file") MultipartFile file,
                         @RequestParam("text") String text,
                         HttpServletRequest req,
                         Model model) throws IOException {
        User user = (User) model.getAttribute("user");
        Optional<CarBrand> carBrandInDb = carBrandService.findById(brandId);
        Optional<Post> postInDb = postService.findById(postId);
        carBrandInDb.ifPresent(brand -> {
                                         car.setBrand(brand);
                                         car.setEngine(engine);
                                         try {
                                             addDriversToCar(car, user, req);
                                             car.setPhoto(file.getBytes());
                                         } catch (Exception e) {
                                            throw new RuntimeException(e);
                                         }
        });
        postInDb.ifPresent(post -> {
                                 post.setCar(car);
                                 post.setText(text);
                                 post.setUser(user);
                                 priceHistory.setPost(post);
                                 post.addPriceHistoryToList(priceHistory);
                                 post.setUpdated(LocalDateTime.now());
                                 postService.update(post);
        });
        return "redirect:/index";
    }

    @GetMapping("/posts/{postId}/delete")
    public String delete(@PathVariable("postId") int id) {
        Optional<Post> postInDb = postService.findById(id);
        postInDb.ifPresent(postService::delete);
        return "redirect:/index";
    }
}
