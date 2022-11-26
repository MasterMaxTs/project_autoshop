package ru.job4j.cars.repository.postrepository;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.job4j.cars.entity.Post;
import ru.job4j.cars.entity.PriceHistory;
import ru.job4j.cars.repository.CrudRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
@AllArgsConstructor
public class PostRepositoryImpl implements PostRepository, PostRepoFilter {

    private final CrudRepository crudRepository;

    /**
     * Найти в базе все объявления, опубликованные конкретным пользователем
     * @param id id пользователя
     * @return список объявлений пользователя, отсортированных по времени
     * обновления
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
     * Найти в базе все объявления
     * @return список объявлений, отсортированных по времени обновления
     */
    @Override
    public List<Post> findAll() {
        return crudRepository.query(
                "from Post order by updated desc", Post.class
        );
    }

    /**
     * Сохранить объявление в базе
     * @param post объявление
     * @return объявление с id
     */
    @Override
    public Post create(Post post) {
        crudRepository.run(session -> session.save(post));
        return post;
    }

    /**
     * Обновить объявление в базе
     * @param post объявление
     */
    @Override
    public void update(Post post) {
        crudRepository.run(session -> session.merge(post));
    }

    /**
     * Удалить объявление из базы
     * @param post объявление
     */
    @Override
    public void delete(Post post) {
        crudRepository.run(session -> session.delete(post));
    }

    /**
     * Удалить все объявления из базы
     */
    @Override
    public void deleteAll() {
        crudRepository.run("delete from Post", Map.of());
    }

    /**
     * Найти объявление в базе по ID
     * @param id id объявления
     * @return объявление в виде Optional
     */
    @Override
    public Optional<Post> findById(int id) {
        return crudRepository.optional(
                "from Post where id = :fId",
                Post.class,
                Map.of("fId", id)
        );
    }

    /**
     * Отфильтровать и вывести из базы все объявления,
     * опубликованные за сутки
     * @return отфильтрованные за сутки объявления
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
     * Отфильтровать и вывести из базы все объявления,
     * отвещающие критериям поиска
     * @return отфильтрованные по параметрам авто объявления
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
     * Отфильтровать и вывести из базы все объявления,
     * отвещающие критериям поиска
     * @return отфильтрованные по марке авто и стоимости,
     * указанной в виде диапазона, объявления
     */
    @Override
    public List<Post> findAllByCarBrandAndPrice(String brand, int minPrice,
                                                int maxPrice) {
        return crudRepository.query(
        "select p from Post p"
            + " join p.priceHistoryList phl"
            + " where"
            + " p.car.brand.name = :fBrand"
            + " AND"
            + " phl.id IN (select MAX(ph.id) from PriceHistory ph group by ph.post.id)"
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
