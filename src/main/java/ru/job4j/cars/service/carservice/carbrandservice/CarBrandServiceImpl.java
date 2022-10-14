package ru.job4j.cars.service.carservice.carbrandservice;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.job4j.cars.entity.CarBrand;
import ru.job4j.cars.repository.carrepository.carbrandrepo.CarBrandRepository;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class CarBrandServiceImpl implements CarBrandService {

    private final CarBrandRepository store;

    @Override
    public List<CarBrand> findAll() {
        return store.findAll();
    }

    @Override
    public Optional<CarBrand> findById(int id) {
        return store.findById(id);
    }
}
