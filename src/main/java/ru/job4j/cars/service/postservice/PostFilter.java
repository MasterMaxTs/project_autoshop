package ru.job4j.cars.service.postservice;

import ru.job4j.cars.entity.Post;

import java.util.List;

public interface PostFilter {

    List<Post> findAllForLastDay();

    List<Post> findAllForCarBrand(String brand);

    List<Post> findAllBy(String brand, String modelYear, String mileage,
                         String transmission, String volume);

    List<Post> findAllByPrice(int minPrice, int maxPrice);
}
