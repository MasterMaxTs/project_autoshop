package ru.job4j.cars.repository.engines;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.job4j.cars.repository.CrudRepository;
import ru.job4j.cars.model.Engine;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
@AllArgsConstructor
public class EngineRepositoryImpl implements EngineRepository {

    private final CrudRepository crudRepository;

    @Override
    public List<Engine> findAll() {
        return crudRepository.query("from Engine", Engine.class);
    }

    @Override
    public Engine create(Engine engine) {
        crudRepository.run(session -> session.save(engine));
        return engine;
    }

    @Override
    public void update(Engine engine) {
        crudRepository.run(session -> session.merge(engine));
    }

    @Override
    public void delete(Engine engine) {
        crudRepository.run(session -> session.delete(engine));
    }

    @Override
    public void deleteAll() {
        crudRepository.run("delete from Engine", Map.of());
    }

    @Override
    public Optional<Engine> findById(int id) {
        return crudRepository.optional(
                "from Engine where id = :fId",
                Engine.class,
                Map.of("fId", id)
        );
    }
}
