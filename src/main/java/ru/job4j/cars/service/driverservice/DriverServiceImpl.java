package ru.job4j.cars.service.driverservice;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.job4j.cars.entity.Driver;
import ru.job4j.cars.repository.driverrepository.DriverRepository;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class DriverServiceImpl implements DriverService {

    private final DriverRepository store;

    @Override
    public List<Driver> findAll() {
        return store.findAll();
    }

    @Override
    public Driver create(Driver driver) {
        return store.create(driver);
    }

    @Override
    public void update(Driver driver) {
        store.update(driver);
    }

    @Override
    public void delete(Driver driver) {
        store.delete(driver);
    }

    @Override
    public void deleteAll() {
        store.deleteAll();
    }

    @Override
    public Optional<Driver> findById(int id) {
        return store.findById(id);
    }
}
