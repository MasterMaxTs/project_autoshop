package ru.job4j.cars.service.postservice;

import ru.job4j.cars.entity.Post;

import java.util.List;

public interface PostFilter {

    List<Post> findAllForLastDay();

    List<Post> findAllForCarBrand(String brand);
}
