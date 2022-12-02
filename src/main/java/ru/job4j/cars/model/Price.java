package ru.job4j.cars.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * Модель данных цена
 */
@Entity
@Table(name = "price_history")
@Data
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Price {

    /**
     * Идентификатор цены
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    @EqualsAndHashCode.Include
    private int id;

    /**
     * Значение цены
     */
    @Column(name = "price")
    private int price;

    /**
     * Локальное время задания цены продажи
     */
    @Column(name = "created")
    private LocalDateTime created = LocalDateTime.now();

    /**
     * Публикация
     */
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "post_id")
    private Post post;

    /**
     * Конструктор - создание нового объекта с определенными значениями.
     * Используется в тестировании
     * @param price цена
     * @param created локальное время создания
     */
    public Price(int price, LocalDateTime created) {
        this.price = price;
        this.created = created;
    }
}
