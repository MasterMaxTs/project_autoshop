package ru.job4j.cars.service.car.carbrand;

import ru.job4j.cars.model.Brand;

import java.util.List;
import java.util.Optional;

public interface CarBrandService {

    List<Brand> findAll();

    Optional<Brand> findById(int id);
}
