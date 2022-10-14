package ru.job4j.cars.repository.carrepository.carbrandrepo;

import ru.job4j.cars.entity.CarBrand;

import java.util.List;
import java.util.Optional;

public interface CarBrandRepository {

    List<CarBrand> findAll();

    Optional<CarBrand> findById(int id);
}
