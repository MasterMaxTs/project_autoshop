package ru.job4j.cars.service.carservice.carbrandservice;

import ru.job4j.cars.entity.CarBrand;

import java.util.List;
import java.util.Optional;

public interface CarBrandService {

    List<CarBrand> findAll();

    Optional<CarBrand> findById(int id);
}
