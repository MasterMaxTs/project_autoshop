package ru.job4j.cars.repository.post;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.job4j.cars.model.Post;
import ru.job4j.cars.repository.CrudRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Реализация хранилища публикаций на сайте с функционалом фильтрации
 */
@Repository
@AllArgsConstructor
public class PostRepositoryImpl implements PostRepository, PostFilterRepository {

    /**
     * Делегирование выполнения CRUD-операций
     * @see ru.job4j.cars.repository.crud.CrudRepositoryImpl
     */
    private final CrudRepository crudRepository;

    /**
     * @return список публикаций пользователя, отсортированных
     * по времени обновления публикации в обратном порядке
     */
    @Override
    public List<Post> findAllByUserId(int id) {
        return crudRepository.query(
                "from Post p where p.user.id = :fId order by p.updated desc",
                Post.class,
                Map.of("fId", id)
        );
    }

    /**
     * @return список архивных публикаций пользователя, отсортированных
     * по времени обновления публикации в обратном порядке
     */
    @Override
    public List<Post> findAllArchivedPosts() {
        return crudRepository.query(
                "from Post where isSold = true order by updated desc",
                Post.class
        );
    }

    /**
     * @return список всех публикаций, отсортированных
     * по времени обновления публикации в обратном порядке
     */
    @Override
    public List<Post> findAll() {
        return crudRepository.query(
                "from Post order by updated desc", Post.class
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
    public void deleteAll() {
        crudRepository.run("delete from Post", Map.of());
    }

    @Override
    public Optional<Post> findById(int id) {
        return crudRepository.optional(
                "from Post where id = :fId",
                Post.class,
                Map.of("fId", id)
        );
    }

    /**
     * @return отфильтрованные объявления, опубликованные за сутки и
     * отсортированные по времени обновления публикации в обратном порядке
     */
    @Override
    public List<Post> findAllForLastDay() {
        LocalDateTime end = LocalDateTime.now();
        LocalDateTime start = end.minusDays(1L);
        return crudRepository.query(
                "from Post"
                        + " where created between :start and :end"
                        + " order by updated desc",
                Post.class,
                Map.of("start", start, "end", end));
    }

    /**
     * @return отфильтрованные объявления по параметрам автомобиля и
     * отсортированные по времени обновления публикации в обратном порядке
     */
    @Override
    public List<Post> findAllByParameters(String brand,
                                          String bodyType,
                                          int modelYear,
                                          int mileage,
                                          String transmission,
                                          String volume
                                ) {
        return crudRepository.query(
                "from Post p"
                        + " where"
                        + " p.car.brand.name =:fBrand"
                        + " AND p.car.bodyType =:fBody"
                        + " AND p.car.modelYear >=:fYear"
                        + " AND p.car.mileage <= :fMileage"
                        + " AND p.car.engine.transmission = :fTransmission"
                        + " AND p.car.engine.volume = :fVolume"
                        + " order by p.updated desc",
                Post.class,
                Map.of(
                        "fBrand", brand,
                        "fBody", bodyType,
                        "fYear", modelYear,
                        "fMileage", mileage,
                        "fTransmission", transmission,
                        "fVolume", volume
                ));
    }


    /**
     * @return отфильтрованные объявления по марке автомобиля и стоимости,
     * указанной в виде диапазона, отсортированные по времени
     * обновления публикации в обратном порядке
     */
    @Override
    public List<Post> findAllByCarBrandAndPrice(String brand, int minPrice,
                                                int maxPrice) {
        return crudRepository.query(
        "select p from Post p"
            + " join p.priceList phl"
            + " where"
            + " p.car.brand.name = :fBrand"
            + " AND"
            + " phl.id IN (select MAX(p.id) from Price p group by p.post.id)"
            + " AND"
            + " phl.price between :fMinPrice and :fMaxPrice"
            + " order by p.updated desc",
                Post.class,
                Map.of(
                        "fBrand", brand,
                        "fMinPrice", minPrice,
                        "fMaxPrice", maxPrice)
        );
    }
}
