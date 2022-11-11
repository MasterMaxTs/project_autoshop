package ru.job4j.cars.service.carservice;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.job4j.cars.entity.Car;
import ru.job4j.cars.repository.carrepository.CarRepository;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class CarServiceImpl implements CarService {

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
