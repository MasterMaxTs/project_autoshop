package ru.job4j.cars.service.postservice;

import ru.job4j.cars.entity.Post;
import ru.job4j.cars.entity.User;
import ru.job4j.cars.service.ManageService;

import java.util.List;

public interface PostService extends ManageService<Post> {

    List<Post> findAll(User user);
}