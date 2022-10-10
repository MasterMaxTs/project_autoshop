package ru.job4j.cars.repository;

import java.util.List;
import java.util.Optional;

public interface ManageRepository<T> {

    List<T> findAll();

    T create(T model);

    void update(T model);

    void delete(T model);

    Optional<T> findById(int id);
}
