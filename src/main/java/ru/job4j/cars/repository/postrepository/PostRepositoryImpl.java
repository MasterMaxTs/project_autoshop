package ru.job4j.cars.repository.postrepository;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.job4j.cars.entity.Post;
import ru.job4j.cars.entity.User;
import ru.job4j.cars.repository.CrudRepository;

import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
@AllArgsConstructor
public class PostRepositoryImpl implements PostRepository, PostRepoFilter {

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

    @Override
    public List<Post> findAllForLastDay() {
        Calendar start = Calendar.getInstance();
        Calendar end = Calendar.getInstance();
        start.set(Calendar.HOUR, 0);
        start.set(Calendar.MINUTE, 0);
        start.set(Calendar.SECOND, 0);
        end.set(Calendar.HOUR, 23);
        end.set(Calendar.MINUTE, 59);
        end.set(Calendar.SECOND, 59);
        return crudRepository.query(
                "from Post where created between :start and :end ",
                Post.class,
                Map.of("start", start, "end", end)
        );
    }

    @Override
    public List<Post> findAllForCarBrand(String brand) {
        return crudRepository.query(
                "from Post p inner join p.car c with c.brand = :fBrand",
                Post.class,
                Map.of("fBrand", brand)
        );
    }
}
