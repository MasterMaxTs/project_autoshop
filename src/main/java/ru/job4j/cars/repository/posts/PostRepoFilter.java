package ru.job4j.cars.repository.posts;

import ru.job4j.cars.model.Post;

import java.util.List;

public interface PostRepoFilter {

    List<Post> findAllForLastDay();

    List<Post> findAllByParameters(String brand, String bodyType, int modelYear,
                                   int mileage, String transmission,
                                   String volume);

    List<Post> findAllByCarBrandAndPrice(String brand, int minPrice,
                                         int maxPrice);
}
