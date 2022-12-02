package ru.job4j.cars.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.*;

/**
 * Модель данных автомобиль
 */
@Entity
@Table(name = "auto_cars")
@Data
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Car {

    /**
     * Идентификатор автомобиля
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    @EqualsAndHashCode.Include
    private int id;

    /**
     * Бренд автомобиля
     */
    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE,
            CascadeType.REFRESH, CascadeType.DETACH})
    @JoinColumn(name = "brand_id")
    private Brand brand;

    /**
     * Тип кузова автомобиля
     */
    @Column(name = "body_type")
    private String bodyType;

    /**
     * Цвет автомобиля
     */
    @Column(name = "color")
    private String color;

    /**
     * Год выпуска автомобиля
     */
    @Column(name = "model_year")
    private int modelYear;

    /**
     * Пробег автомобиля
     */
    @Column(name = "mileage")
    private int mileage;

    /**
     * Двигатель автомобиля
     */
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "engine_id")
    private Engine engine;

    /**
     * Фото автомобиля
     */
    @Column(name = "photo")
    private byte[] photo;

    /**
     * Владельцы автомобиля
     */
    @ManyToMany(cascade = CascadeType.ALL,
            fetch = FetchType.EAGER)
    @JoinTable(
            name = "history_owners",
            joinColumns = {@JoinColumn(name = "car_id")},
            inverseJoinColumns = {@JoinColumn(name = "driver_id")}
    )
    private Set<Driver> owners = new LinkedHashSet<>();

    /**
     * Конструктор - создание нового объекта с определенными значениями.
     * Используется в тестировании
     * @param color цвет
     * @param bodyType тип кузова
     * @param modelYear год выпуска
     * @param mileage пробег
     * @param engine двигатель
     * @param photo фото
     */
    public Car(String color,
               String bodyType,
               int modelYear,
               int mileage,
               Engine engine,
               byte[] photo) {
        this.color = color;
        this.bodyType = bodyType;
        this.modelYear = modelYear;
        this.mileage = mileage;
        this.engine = engine;
        this.photo = photo;
    }

    public void setPhoto(byte[] photo) {
        if (photo.length != 0) {
           this.photo = photo;
        }
    }

    /**
     * Добавляет очередного владельца в список автовладельцев
     * @param driver автовладелец
     */
    public void addCarDriver(Driver driver) {
        owners.add(driver);
    }
}
