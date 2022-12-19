package ru.job4j.cars.repository.car.carbrand;

import ru.job4j.cars.model.Brand;

import java.util.List;
import java.util.Optional;

/**
 * Хранилище автобрендов
 * @see ru.job4j.cars.model.Brand
 */
public interface CarBrandRepository {

    /**
     * Возвращает список всех автобрендов, находящихся в базе данных
     * @return  список всех автобрендов
     */
    List<Brand> findAll();

    /**
     * Найти автобренд в базе данных по ID
     * @param id id автобренда
     * @return Optional.of(brand), если автобренд найден в базе данных,
     * иначе Optional.empty()
     */
    Optional<Brand> findById(int id);
}
