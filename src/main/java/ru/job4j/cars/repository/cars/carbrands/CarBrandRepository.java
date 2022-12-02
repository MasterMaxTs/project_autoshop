package ru.job4j.cars.repository.cars.carbrands;

import ru.job4j.cars.model.Brand;

import java.util.List;
import java.util.Optional;

public interface CarBrandRepository {

    List<Brand> findAll();

    Optional<Brand> findById(int id);
}
