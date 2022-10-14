package ru.job4j.cars.repository.postrepository;

import ru.job4j.cars.entity.Post;
import ru.job4j.cars.repository.ManageRepository;

import java.util.List;

public interface PostRepository extends ManageRepository<Post> {

    List<Post> findAllByUserId(int id);

    List<Post> findAll();
}
