package ru.job4j.cars.service.postservice;

import ru.job4j.cars.entity.Post;

import java.util.List;

public interface PostFilter {

    List<Post> findAllForLastDay();

    List<Post> findAllByParameters(String brand, String bodyType, String modelYear,
                                   String mileage, String transmission, String volume);

    List<Post> findAllByCarBrandAndPrice(String brand, int minPrice,
                                         int maxPrice);
}
