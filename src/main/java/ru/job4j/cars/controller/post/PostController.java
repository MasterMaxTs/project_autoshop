package ru.job4j.cars.controller.post;

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
import ru.job4j.cars.controller.SessionController;
import ru.job4j.cars.model.*;
import ru.job4j.cars.service.admin.AdminPostService;
import ru.job4j.cars.service.car.CarService;
import ru.job4j.cars.service.car.carbrand.CarBrandService;
import ru.job4j.cars.service.driver.DriverService;
import ru.job4j.cars.service.engine.EngineService;
import ru.job4j.cars.service.post.PostFilterService;
import ru.job4j.cars.service.post.PostService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Контроллер публикаций
 */
@Controller
@AllArgsConstructor
public class PostController extends SessionController {

    /**
     * Ссылки на слои сервисов
     */
    private final PostService postService;
    private final PostFilterService postFilterService;
    private final EngineService engineService;
    private final CarService carService;
    private final CarBrandService carBrandService;
    private final DriverService driverService;
    private final AdminPostService adminPostService;

    /**
     * Добавляет пользователя во все модели,
     * определённые в контроллере публикаций
     * @param session HttpSession
     * @return пользователя из текущей сессии
     */
    @ModelAttribute("user")
    User addUserToModels(HttpSession session) {
        return getUserFromSession(session);
    }

    /**
     * Перенаправляет на страницу со всеми публикациями
     * @return перенаправление на страницу со всеми публикациями
     * /posts
     */
    @GetMapping("/index")
    public String index() {
        return "redirect:/posts";
    }

    /**
     * Добавляет в модель все публикации, помеченные в БД как непроданные,
     * добавляет в модель список всех автобрендов, найденных в БД,
     * для демонстрации пользователю
     * @param model Model
     * @return вид post/post-all
     */
    @GetMapping("/posts")
    public String allUnsold(Model model) {
        List<Post> posts = postService.findAll()
                                      .stream()
                                      .filter(p -> !p.isSold())
                                      .collect(Collectors.toList());
        model.addAttribute("posts", posts);
        model.addAttribute("brands", carBrandService.findAll());
        return "post/post-all";
    }

    /**
     * Добавляет в модель все публикации, помеченных в БД как проданные,
     * добавляет в модель список всех автобрендов, найденных в БД,
     * для демонстрации пользователю
     * @param model Model
     * @return вид /post/post-all-archived
     */
    @GetMapping("/posts/archive")
    public String allArchive(Model model) {
        model.addAttribute("posts", postService.findAllArchivedPosts());
        model.addAttribute("brands", carBrandService.findAll());
        return "/post/post-all-archived";
    }

    /**
     * Загружает на страницы сайта изображения автомобилей
     * @param id id публикации
     * @return ResponseEntity
     */
    @GetMapping("/postPhoto/{postId}")
    public ResponseEntity<Resource> downloadPhoto(@PathVariable("postId") int id) {
        ResponseEntity<Resource> rsl = null;
        Optional<Post> postInDb = postService.findById(id);
        if (postInDb.isPresent()) {
            Car car = postInDb.get().getCar();
            rsl = ResponseEntity.ok()
                    .headers(new HttpHeaders())
                    .contentLength(car.getPhoto().length)
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .body(new ByteArrayResource(car.getPhoto()));
        }
        return rsl;
    }

    /**
     * Добавляет в модель публикацию, найденную в БД по id,
     * для демонстрации пользователю
     * @param id id публикации
     * @param model Model
     * @return вид /post/post-id
     */
    @GetMapping("/posts/{postId}/edit")
    public String showPost(@PathVariable("postId") int id, Model model) {
        Optional<Post> postInDb = postService.findById(id);
        postInDb.ifPresent(post -> model.addAttribute("post", post));
        return "/post/post-id";
    }

    /**
     * Показывает в публикации информацию об автовладельцах
     * в хронологическом порядке
     * @param id id публикации
     * @return перенаправление на страницу публикации
     * /posts/{id}/edit?ownership_history=true
     */
    @GetMapping("/posts/{postId}/ownership-history")
    public String showOwnershipHistoryByPost(@PathVariable("postId") int id) {
        return String.format(
                "redirect:/posts/%d/edit?ownership_history=true", id
        );
    }

    /**
     * Показывает в публикации информацию об изменении цены
     * продажи автомобиля в хронологическом порядке
     * @param id id публикации
     * @return перенаправление на страницу публикации
     * /posts/{id}/edit?price_history=true
     */
    @GetMapping("posts/{postId}/price-history")
    public String showPriceHistoryByUserPost(@PathVariable("postId") int id) {
        return String.format(
                "redirect:/posts/%d/edit?price_history=true", id
        );
    }

    /**
     * Предоставляет пользователю форму для обновления данных в публикации
     * @param id id публикации
     * @param model Model
     * @return вид /post/post-update
     */
    @GetMapping("posts/{postId}/update")
    public String formUpdatePost(@PathVariable("postId") int id,
                                 Model model) {
        Optional<Post> postInDb = postService.findById(id);
        postInDb.ifPresent(post -> model.addAttribute("post", post));
        return "/post/post-update";
    }

    /**
     * Показывает в публикации информацию о контактных данных продавца
     * @param id id публикации
     * @return перенаправление на страницу публикации
     * /posts/{id}/edit?contact=true
     */
    @GetMapping("/posts/contacts/{postId}")
    public String showPostContacts(@PathVariable("postId") int id) {
        return String.format("redirect:/posts/%d/edit?contact=true", id);
    }

    /**
     * Добавляет публикацию в список избранных пользователем публикаций
     * @param id id публикации
     * @param model Model
     * @return перенаправление на страницу со всеми избранными
     * пользователем публикациями
     * /posts/subscriptions
     */
    @GetMapping("/posts/subs/{postId}")
    public String addSubscriptionToPost(@PathVariable("postId") int id,
                                      Model model) {
        User user = (User) model.getAttribute("user");
        Optional<Post> postInDb = postService.findById(id);
        postInDb.ifPresent(post -> {
                                    post.addParticipantToPost(user);
                                    postService.update(post); }
        );
        return "redirect:/posts/subscriptions";
    }

    /**
     * Удаляет публикацию из списка избранных пользователем публикаций
     * @param id id публикации
     * @param model Model
     * @return перенаправление на страницу со всеми избранными
     * пользователем публикациями
     * /posts/subscriptions
     */
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

    /**
     * Добавляет в модель все избранные пользователем публикации,
     * добавляет в модель список всех автобрендов, найденных в БД,
     * для демонстрации пользователю
     * @param model Model
     * @return вид post/post-all-subscriptions
     */
    @GetMapping("/posts/subscriptions")
    public String allUserSubscriptionsToPosts(Model model) {
        User user = (User) model.getAttribute("user");
        model.addAttribute(
                "subs", postService.findAllFavoritePostsByUser(user)
        );
        model.addAttribute("brands", carBrandService.findAll());
        return "post/post-all-subscriptions";
    }
    /**
     * Добавляет в модель все публикации, созданные пользователем,
     * добавляет в модель список всех автобрендов, найденных в БД,
     * для демонстрации пользователю
     * @param model Model
     * @return вид post/post-all-by-user
     */

    @GetMapping("/posts/users/{userId}")
    public String allByUserId(@PathVariable("userId") int id,
                              Model model) {
        model.addAttribute("userPosts", postService.findAllByUserId(id));
        model.addAttribute("brands", carBrandService.findAll());
        return "post/post-all-by-user";
    }

    /**
     * Предоставляет зарегистрированному пользователю сайта форму
     * для создания публикации,
     * добавляет в модель список всех автобрендов, найденных в БД,
     * для демонстрации пользователю
     * @param post публикация
     * @param model Model
     * @return вид post/post-create
     */
    @GetMapping("/posts/new")
    public String formAddPost(@ModelAttribute Post post, Model model) {
        model.addAttribute("brands", carBrandService.findAll());
        return "post/post-create";
    }

    /**
     * Добавляет в модель все публикации,
     * отфильтрованные по дате создания за минувшие сутки,
     * добавляет в модель список всех автобрендов, найденных в БД,
     * добавляет в модель метку filter2 = true,
     * добавляет в модель сообщение msg,
     * для демонстрации пользователю
     * @param model Model
     * @return страницу со всеми публикациями с учетом фильтра (filter2)
     * post/post-all
     */
    @GetMapping("/posts/filter/last_day")
    public String allForLastDay(Model model) {
        String message = "Отфильтрованные за сутки объявления";
        model.addAttribute("posts", postFilterService.findAllForLastDay());
        model.addAttribute("brands", carBrandService.findAll());
        model.addAttribute("filter2", true);
        model.addAttribute("msg", message);
        return "post/post-all";
    }

    /**
     * Добавляет в модель все публикации,
     * отфильтрованные по бренду автомобиля и диапазону цены продажи,
     * добавляет в модель список всех автобрендов, найденных в БД,
     * добавляет в модель метку filter3 = true,
     * добавляет в модель сообщение msg,
     * добавляет в модель данные для заполнения поля фильтра бренда fBrand,
     * добавляет в модель данные для заполнения поля фильтра нижней границы цены fLprice,
     * добавляет в модель данные для заполнения поля фильтра верхней границы цены fUprice,
     * для демонстрации пользователю
     * @param model Model
     * @return страницу со всеми публикациями с учетом фильтра (filter3)
     * post/post-all
     */
    @GetMapping("posts/filter/by_brand_price")
    public String allByCarBrandAndPrice(HttpServletRequest req, Model model) {
        String[] parameters = req.getParameterValues("p");
        String brand = parameters[0];
        String lowerPrice = parameters[1];
        String upperPrice = parameters[2];
        String message = "Отфильтрованные объявления по марке авто и цене";
        Map<String, String> attributes = Map.of(
                                            "msg", message,
                                            "fBrand", brand,
                                            "fLprice", lowerPrice,
                                            "fUprice", upperPrice);
        model.addAttribute(
                "posts",
                postFilterService.findAllByCarBrandAndPrice(
                                                brand,
                                                Integer.parseInt(lowerPrice),
                                                Integer.parseInt(upperPrice)
                ));
        model.addAttribute("brands", carBrandService.findAll());
        model.addAttribute("filter3", true);
        model.mergeAttributes(attributes);
        return "post/post-all";
    }

    /**
     * Добавляет в модель все публикации,отфильтрованные по параметрам автомобиля,
     * добавляет в модель список всех автобрендов, найденных в БД,
     * добавляет в модель метку filter1 = true,
     * добавляет в модель сообщение msg,
     * добавляет в модель данные для заполнения поля фильтра бренда fBrand,
     * добавляет в модель данные для заполнения поля фильтра тип кузова fBodyType,
     * добавляет в модель данные для заполнения поля фильтра год выпуска fModelYear
     * добавляет в модель данные для заполнения поля фильтра пробег fMileage,
     * добавляет в модель данные для заполнения поля фильтра тип коробки передач fTransmission
     * добавляет в модель данные для заполнения поля фильтра объём двигателя fVolume
     * для демонстрации пользователю
     * @param model Model
     * @return страницу со всеми публикациями с учетом фильтра (filter1)
     * post/post-all
     */
    @GetMapping("/posts/filter/by_parameters")
    public String allByParameters(HttpServletRequest req, Model model) {
        String[] parameters = req.getParameterValues("p");
        String brand = parameters[0];
        String bodyType = parameters[1];
        String modelYear = parameters[2];
        String mileage = parameters[3];
        String transmission = parameters[4];
        String volume = parameters[5];
        String message = "Отфильтрованные объявления по параметрам авто";
        Map<String, String> attributes = Map.of(
                                            "msg", message,
                                            "fBrand", brand,
                                            "fBodyType", bodyType,
                                            "fModelYear", modelYear,
                                            "fMileage", mileage,
                                            "fTransmission", transmission,
                                            "fVolume", volume);
        List<Post> filteredPosts = postFilterService.findAllByParameters(brand,
                                                        bodyType,
                                                        Integer.parseInt(modelYear),
                                                        Integer.parseInt(mileage),
                                                        transmission,
                                                        volume);
        model.addAttribute("posts", filteredPosts);
        model.addAttribute("brands", carBrandService.findAll());
        model.addAttribute("filter1", true);
        model.mergeAttributes(attributes);
        return "post/post-all";
    }

    /**
     * Добавляет новую публикацию в БД сайта
     * @param post публикация
     * @param engine двигатель
     * @param car автомобиль
     * @param price цена
     * @param brandId id автобренда
     * @param file фото автомобиля
     * @param req HttpServletRequest
     * @param model Model
     * @return перенаправление на главную страницу
     * /index
     */
    @PostMapping("/addPost")
    public String add(@ModelAttribute Post post,
                      @ModelAttribute Engine engine,
                      @ModelAttribute Car car,
                      @ModelAttribute Price price,
                      @RequestParam("brand.id") int brandId,
                      @RequestParam("file") MultipartFile file,
                      HttpServletRequest req,
                      Model model) {
        User user = (User) model.getAttribute("user");
        post.setCreated(LocalDateTime.now());
        post.setSold(false);
        Optional<Brand> carBrandInDb = carBrandService.findById(brandId);
        carBrandInDb.ifPresent(brand -> {
                                         car.setBrand(brand);
                                         car.setEngine(engine);
                                         try {
                                             car.setPhoto(file.getBytes());
                                             addDriversToCar(car, user, req);
                                         } catch (Exception e) {
                                             throw new RuntimeException(e);
                                         }
        });
        post.setCar(car);
        post.setUser(user);
        price.setPost(post);
        post.addPriceHistoryToList(price);
        post.setUpdated(post.getCreated());
        postService.create(post);
        return "redirect:/index";
    }

    /**
     * Формирует список автовладельцев автомобиля
     * @param car автомобиль
     * @param user автор публикации (последний автовладелец)
     * @param req HttpServletRequest
     */
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
                                                  driverService.create(driver);
                                                  car.addCarDriver(driver);
                                            });
    }

    /**
     * Обновляет содержимое публикации в БД сайта
     * @param engine двигатель
     * @param price цена
     * @param file фото автомобиля
     * @param req HttpServletRequest
     * @return перенаправление на главную страницу
     * /index
     */
    @PostMapping("/updatePost")
    public String update(@ModelAttribute Engine engine,
                         @ModelAttribute Price price,
                         @RequestParam("file") MultipartFile file,
                         HttpServletRequest req) {
        Optional<Post> postInDb = postService.findById(
                Integer.parseInt(req.getParameter("post.id"))
        );
        postInDb.ifPresent(
            post -> {
                    Car car = post.getCar();
                    car.setColor(req.getParameter("color"));
                    try {
                        byte[] fileBytes = file.getBytes();
                        car.setPhoto(fileBytes);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                    car.setMileage(
                            Integer.parseInt(req.getParameter("mileage"))
                    );
                    engine.setId(car.getEngine().getId());
                    car.setEngine(engine);
                    post.setCar(car);
                    engineService.update(engine);
                    carService.update(car);
                    post.setText(req.getParameter("text"));
                    price.setPost(post);
                    post.addPriceHistoryToList(price);
                    post.setUpdated(LocalDateTime.now());
                    postService.update(post);
                    }
        );
        return "redirect:/index";
    }

    /**
     * Удаляет публикацию из БД сайта по id (роль - Пользователь)
     * @param id id публикации
     * @return перенаправление на главную страницу
     * /index
     */
    @GetMapping("/posts/{postId}/delete")
    public String delete(@PathVariable("postId") int id) {
        deletePostById(id);
        return "redirect:/index";
    }

    /**
     * Удаляет публикацию из БД сайта по id (роль - Администратор)
     * @param id id публикации
     * @return перенаправление на страницу с архивными публикациями
     * /posts/archive
     */
    @GetMapping("posts/{postId}/delete_by_admin")
    public String deleteByAdmin(@PathVariable("postId") int id) {
        deletePostById(id);
        return "redirect:/posts/archive";
    }

    /**
     * Помечает публикацию в БД сайта, как проданную
     * @param id id публикации
     * @param model Model
     * @return перенаправление на страницу со всеми публикации,
     * созданными пользователем
     * /posts/users/{userId}
     */
    @GetMapping("/posts/{postId}/archive")
    public String addToArchive(@PathVariable("postId") int id, Model model) {
        Optional<Post> postInDb = postService.findById(id);
        postInDb.ifPresent(post -> {
                                    post.setSold(true);
                                    post.setSaled(LocalDateTime.now());
                                    postService.update(post); }
        );
        User user = (User) model.getAttribute("user");
        return String.format("redirect:/posts/users/%d", Objects.requireNonNull(user).getId());
    }

    /**
     * Удаляет из БД сайта все публикации, помеченные как проданные
     * (роль - Админитстратор)
     * @return перенаправление на страницу со всеми архивными публикациями
     * /posts/archive
     */
    @GetMapping("/posts/archive/delete/all")
    public String deleteAllFromArchive() {
        adminPostService.deleteAllArchivedPosts();
        return "redirect:/posts/archive";
    }

    /**
     * Удаляет публикацию из БД по id
     * @param id публикации
     */
    private void deletePostById(int id) {
        Optional<Post> postInDb = postService.findById(id);
        postInDb.ifPresent(postService::delete);
    }
}
