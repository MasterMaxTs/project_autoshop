package ru.job4j.cars.repository.carrepository;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.job4j.cars.repository.CrudRepository;
import ru.job4j.cars.entity.Car;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
@AllArgsConstructor
public class CarRepositoryImpl implements CarRepository {

    private final CrudRepository crudRepository;

    @Override
    public List<Car> findAll() {
        return crudRepository.query("from Car", Car.class);
    }

    @Override
    public Car create(Car car) {
        crudRepository.run(session -> session.save(car));
        return car;
    }

    @Override
    public void update(Car car) {
        crudRepository.run(session -> session.merge(car));
    }

    @Override
    public void delete(Car car) {
        crudRepository.run(session -> session.delete(car));
    }

    @Override
    public Optional<Car> findById(int id) {
        return crudRepository.optional(
                "from Car where id = :fId",
                Car.class,
                Map.of("fId", id)
        );
    }
}
