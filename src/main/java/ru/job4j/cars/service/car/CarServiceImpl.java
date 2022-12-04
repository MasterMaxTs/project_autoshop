package ru.job4j.cars.service.car;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.job4j.cars.model.Car;
import ru.job4j.cars.repository.cars.CarRepository;

import java.util.List;
import java.util.Optional;

/**
 * Реализация сервиса автомобилей
 */
@Service
@AllArgsConstructor
public class CarServiceImpl implements CarService {

    /**
     * Делегирование выполнения CRUD-операций хранилищу автомобилей
     */
    private final CarRepository store;

    @Override
    public List<Car> findAll() {
        return store.findAll();
    }

    @Override
    public Car create(Car car) {
        return store.create(car);
    }

    @Override
    public void update(Car car) {
        store.update(car);
    }

    @Override
    public void delete(Car car) {
        store.delete(car);
    }

    @Override
    public void deleteAll() {
        store.deleteAll();
    }

    @Override
    public Optional<Car> findById(int id) {
        return store.findById(id);
    }
}
