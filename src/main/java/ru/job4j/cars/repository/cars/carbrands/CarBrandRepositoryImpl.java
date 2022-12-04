package ru.job4j.cars.repository.cars.carbrands;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.job4j.cars.model.Brand;
import ru.job4j.cars.repository.CrudRepository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Реализация хранилища автобрендов на сайте
 */
@Repository
@AllArgsConstructor
public class CarBrandRepositoryImpl implements CarBrandRepository {

    /**
     * Делегирование выполнения CRUD-операций
     * @see ru.job4j.cars.repository.crud.CrudRepositoryImpl
     */
    private final CrudRepository crudRepository;

    @Override
    public List<Brand> findAll() {
        return crudRepository.query("from Brand", Brand.class);
    }

    @Override
    public Optional<Brand> findById(int id) {
        return crudRepository.optional(
                "from Brand where id = :fId",
                Brand.class,
                Map.of("fId", id)
        );
    }
}
