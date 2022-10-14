package ru.job4j.cars.repository.carrepository.carbrandrepo;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.job4j.cars.entity.CarBrand;
import ru.job4j.cars.repository.CrudRepository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
@AllArgsConstructor
public class CarBrandRepositoryImpl implements CarBrandRepository {

    private final CrudRepository crudRepository;

    @Override
    public List<CarBrand> findAll() {
        return crudRepository.query("from CarBrand", CarBrand.class);
    }

    @Override
    public Optional<CarBrand> findById(int id) {
        return crudRepository.optional(
                "from CarBrand where id = :fId",
                CarBrand.class,
                Map.of("fId", id)
        );
    }
}
