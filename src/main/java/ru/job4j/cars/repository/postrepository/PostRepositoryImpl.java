package ru.job4j.cars.repository.postrepository;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.job4j.cars.entity.User;
import ru.job4j.cars.repository.CrudRepository;
import ru.job4j.cars.entity.Post;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
@AllArgsConstructor
public class PostRepositoryImpl implements PostRepository {

    private final CrudRepository crudRepository;

    @Override
    public List<Post> findAll() {
        return crudRepository.query(
                "from Post order by created desc", Post.class
        );
    }

    @Override
    public Post create(Post post) {
        crudRepository.run(session -> session.save(post));
        return post;
    }

    @Override
    public void update(Post post) {
        crudRepository.run(session -> session.merge(post));
    }

    @Override
    public void delete(Post post) {
        crudRepository.run(session -> session.delete(post));
    }

    @Override
    public Optional<Post> findById(int id) {
        return crudRepository.optional(
                "from Post where id = :fId",
                Post.class,
                Map.of("fId", id)
        );
    }

    @Override
    public List<Post> findAll(User user) {
        return crudRepository.query(
                "from Post where user = :fUser",
                Post.class,
                Map.of("fUser", user)
        );
    }
}
