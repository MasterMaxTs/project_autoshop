package ru.job4j.cars.service;

import java.util.List;
import java.util.Optional;

public interface ManageService<T> {

    List<T> findAll();

    T create(T model);

    void update(T model);

    void delete(T model);

    void deleteAll();

    Optional<T> findById(int id);
}
