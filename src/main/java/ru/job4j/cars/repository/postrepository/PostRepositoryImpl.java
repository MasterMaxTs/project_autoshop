package ru.job4j.cars.repository.postrepository;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.job4j.cars.entity.Post;
import ru.job4j.cars.repository.CrudRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
@AllArgsConstructor
public class PostRepositoryImpl implements PostRepository, PostRepoFilter {

    private final CrudRepository crudRepository;

    @Override
    public List<Post> findAllByUserId(int id) {
        return crudRepository.query(
                "from Post p where p.user.id = :fId",
                Post.class,
                Map.of("fId", id)
        );
    }

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
    public List<Post> findAllForLastDay() {
        LocalDateTime end = LocalDateTime.now();
        LocalDateTime start = end.minusDays(1L);
        return crudRepository.query(
                "from Post where created between :start and :end ",
                Post.class,
                Map.of("start", start, "end", end)
        );
    }

    @Override
    public List<Post> findAllForCarBrand(String brand) {
        return crudRepository.query(
                "from Post p where p.car.brand = :fBrand",
                Post.class,
                Map.of("fBrand", brand)
        );
    }
}
