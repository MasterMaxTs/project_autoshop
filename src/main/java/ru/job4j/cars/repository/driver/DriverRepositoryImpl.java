package ru.job4j.cars.repository.driver;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.job4j.cars.repository.CrudRepository;
import ru.job4j.cars.model.Driver;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Реализация хранилища автовладельцев на сайте
 */
@Repository
@AllArgsConstructor
public class DriverRepositoryImpl implements DriverRepository {

    /**
     * Делегирование выполнения CRUD-операций
     * @see ru.job4j.cars.repository.crud.CrudRepositoryImpl
     */
    private final CrudRepository crudRepository;

    @Override
    public List<Driver> findAll() {
        return crudRepository.query("from Driver", Driver.class);
    }

    @Override
    public Driver create(Driver driver) {
        crudRepository.run(session -> session.save(driver));
        return driver;
    }

    @Override
    public void update(Driver driver) {
        crudRepository.run(session -> session.merge(driver));
    }

    @Override
    public void delete(Driver driver) {
        crudRepository.run(session -> session.delete(driver));
    }

    @Override
    public void deleteAll() {
        crudRepository.run("delete from Driver", Map.of());
    }

    @Override
    public Optional<Driver> findById(int id) {
        return crudRepository.optional(
                "from Driver where id = :fId",
                Driver.class,
                Map.of("fId", id)
        );
    }
}
