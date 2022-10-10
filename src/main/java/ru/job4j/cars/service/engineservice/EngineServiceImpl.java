package ru.job4j.cars.service.engineservice;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.job4j.cars.entity.Engine;
import ru.job4j.cars.repository.enginerepository.EngineRepository;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class EngineServiceImpl implements EngineService {

    private final EngineRepository store;

    @Override
    public List<Engine> findAll() {
        return store.findAll();
    }

    @Override
    public Engine create(Engine engine) {
        return store.create(engine);
    }

    @Override
    public void update(Engine engine) {
        store.update(engine);
    }

    @Override
    public void delete(Engine engine) {
        store.delete(engine);
    }

    @Override
    public Optional<Engine> findById(int id) {
        return store.findById(id);
    }
}
