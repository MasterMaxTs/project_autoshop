package ru.job4j.cars.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Модель данных публикация
 */
@Entity
@Table(name = "auto_posts")
@Data
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Post {

    /**
     * Идентификатор публикации
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    @EqualsAndHashCode.Include
    private int id;

    /**
     * Описание публикации
     */
    @Column(name = "text")
    private String text;

    /**
     * Локальное время публикации объявления на сайте
     */
    @Column(name = "created")
    private LocalDateTime created;

    /**
     * Локальное время обновления объявления на сайте
     */
    @Column(name = "updated")
    private LocalDateTime updated;

    /**
     * Локальное время архивации объявления на сайте
     */
    @Column(name = "saled")
    private LocalDateTime saled;

    /**
     * Статус продажи автомобиля в объявлении
     */
    @Column(name = "is_sold")
    private boolean isSold;

    /**
     * Автомобиль
     */
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "car_id")
    private Car car;

    /**
     * Автор объявления
     */
    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE,
            CascadeType.REFRESH, CascadeType.DETACH})
    @JoinColumn(name = "user_id")
    private User user;

    /**
     * История изменения цены продажи автомобиля
     */
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "post", fetch = FetchType.EAGER)
    private final List<Price> priceList = new ArrayList<>();

    /**
     * Подписчики на данное объявление
     */
    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE,
            CascadeType.REFRESH, CascadeType.DETACH},
            fetch = FetchType.EAGER)
    @JoinTable(
            name = "participants",
            joinColumns = {@JoinColumn(name = "post_id")},
            inverseJoinColumns = {@JoinColumn(name = "user_id")}
    )
    private final Set<User> participants = new HashSet<>();

    /**
     * Добавляет цену продажи в объявлении в список ценовой истории
     * @param price цена
     */
    public void addPriceHistoryToList(Price price) {
        int size = priceList.size();
        if (size == 0) {
            priceList.add(price);
            return;
        }
        if (priceList.get(size - 1).getPrice() != price.getPrice()) {
            priceList.add(price);
        }
    }

    /**
     * Добавляет подписчика в список подписчиков
     * @param user подписчик
     */
    public void addParticipantToPost(User user) {
        participants.add(user);
    }

    /**
     * Удаляет подписчика из списка подписчиков
     * @param user подписчик
     */
    public void removeParticipantFromPost(User user) {
        participants.remove(user);
    }
}
