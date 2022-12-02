package ru.job4j.cars.model;

import lombok.*;

import javax.persistence.*;

/**
 * Модель данных бренд автомобиля
 */
@Entity
@Table(name = "auto_car_brands")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Brand {

    /**
     * Идентификатор автобренда
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    @EqualsAndHashCode.Include
    private int id;

    /**
     * Наименование автобренда
     */
    @Column(name = "brand")
    private String name;
}
