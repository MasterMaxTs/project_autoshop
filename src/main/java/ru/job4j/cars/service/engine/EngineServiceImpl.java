package ru.job4j.cars.service.engine;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.job4j.cars.model.Engine;
import ru.job4j.cars.repository.engines.EngineRepository;

import java.util.List;
import java.util.Optional;

/**
 * Реализация сервиса автодвигателей
 */
@Service
@AllArgsConstructor
public class EngineServiceImpl implements EngineService {

    /**
     * Делегирование выполнения CRUD-операций хранилищу автодвигателей
     */
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
    public void deleteAll() {
        store.deleteAll();
    }

    @Override
    public Optional<Engine> findById(int id) {
        return store.findById(id);
    }
}
