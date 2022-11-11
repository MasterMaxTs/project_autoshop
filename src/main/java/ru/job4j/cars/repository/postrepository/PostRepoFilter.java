package ru.job4j.cars.repository.postrepository;

import ru.job4j.cars.entity.Post;

import java.util.List;

public interface PostRepoFilter {

    List<Post> findAllForLastDay();

    List<Post> findAllByParameters(String brand, String bodyType, String modelYear,
                                   String mileage, String transmission, String volume);

    List<Post> findAllByCarBrandAndPrice(String brand, int minPrice,
                                         int maxPrice);
}
