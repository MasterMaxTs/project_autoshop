package ru.job4j.cars.service.car.carbrand;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.job4j.cars.model.Brand;
import ru.job4j.cars.repository.cars.carbrands.CarBrandRepository;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class CarBrandServiceImpl implements CarBrandService {

    private final CarBrandRepository store;

    @Override
    public List<Brand> findAll() {
        return store.findAll();
    }

    @Override
    public Optional<Brand> findById(int id) {
        return store.findById(id);
    }
}
