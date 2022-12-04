package ru.job4j.cars.repository.posts;

import ru.job4j.cars.model.Post;

import java.util.List;

/**
 * Хранилище отфильтрованных публикаций
 * @see ru.job4j.cars.model.Post
 */
public interface PostFilterRepository {

    /**
     * Возвращает список всех объявлений, опубликованных
     * на сайте за текущие сутки
     * @return список отфильтрованных объявлений
     */
    List<Post> findAllForLastDay();

    /**
     * Возвращает список всех объявлений, опубликованных
     * на сайте и отфильтрованных по параметрам автомобиля
     * @param brand автобренд
     * @param bodyType тип кузова
     * @param modelYear год выпуска
     * @param mileage пробег
     * @param transmission тип коробки передач
     * @param volume объём двигателя
     * @return список отфильтрованных объявлений
     */
    List<Post> findAllByParameters(String brand, String bodyType, int modelYear,
                                   int mileage, String transmission, String volume);

    /**
     * Возвращает список всех объявлений, опубликованных
     * на сайте и отфильтрованных по бренду автомобиля
     * и диапазону цены
     * @param brand автобренд
     * @param minPrice минимальная цена диапазона
     * @param maxPrice максимальная цена диапазона
     * @return список отфильтрованных объявлений
     */
    List<Post> findAllByCarBrandAndPrice(String brand, int minPrice, int maxPrice);
}
