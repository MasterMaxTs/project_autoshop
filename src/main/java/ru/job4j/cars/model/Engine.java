package ru.job4j.cars.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/**
 * Модель данных двигатель автомобиля
 */
@Entity
@Table(name = "auto_engines")
@Data
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Engine {

    /**
     * Идентификатор двигателя
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    @EqualsAndHashCode.Include
    private int id;

    /**
     * Наименование двигателя
     */
    @Column(name = "name")
    private String name;

    /**
     * Рабочий объём двигателя
     */
    @Column(name = "volume")
    private String volume;

    /**
     * Тип коробки передач
     */
    @Column(name = "transmission")
    private String transmission;

    /**
     * Тип привода автомобиля
     */
    @Column(name = "drive_unit")
    private String driveUnit;

    /**
     * мощность двигателя
     */
    @Column(name = "power")
    private int power;

    /**
     * Конструктор - создание нового объекта с определенными значениями.
     * Используется в тестировании
     * @param volume рабочий объём
     * @param transmission тип коробки передач
     * @param driveUnit тип привода
     * @param power мощность
     */
    public Engine(String volume, String transmission, String driveUnit, int power) {
        this.volume = volume;
        this.transmission = transmission;
        this.driveUnit = driveUnit;
        this.power = power;
    }
}
