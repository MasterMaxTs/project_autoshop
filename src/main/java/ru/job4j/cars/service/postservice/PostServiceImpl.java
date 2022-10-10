package ru.job4j.cars.service.postservice;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.job4j.cars.entity.Post;
import ru.job4j.cars.entity.User;
import ru.job4j.cars.repository.postrepository.PostRepository;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class PostServiceImpl implements PostService {

    private final PostRepository store;

    @Override
    public List<Post> findAll() {
        return store.findAll();
    }

    @Override
    public Post create(Post post) {
        return store.create(post);
    }

    @Override
    public void update(Post post) {
        store.update(post);
    }

    @Override
    public void delete(Post post) {
        store.delete(post);
    }

    @Override
    public Optional<Post> findById(int id) {
        return store.findById(id);
    }

    @Override
    public List<Post> findAll(User user) {
        return store.findAll(user);
    }
}
