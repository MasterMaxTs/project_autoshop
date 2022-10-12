package ru.job4j.cars.repository.postrepository;

import ru.job4j.cars.entity.Post;

import java.util.List;

public interface PostRepoFilter {

    List<Post> findAllForLastDay();

    List<Post> findAllForCarBrand(String brand);
}
