package ru.job4j.cars.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

/**
 * Модель данных владелец автомобиля
 */
@Entity
@Table(name = "auto_drivers")
@Data
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Driver {

    /**
     * Идентификатор автовладельца
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    @EqualsAndHashCode.Include
    private int id;

    /**
     * Имя автовладельца
     */
    @Column(name = "first_name")
    private String firstName;

    /**
     * Фамилия автовладельца
     */
    @Column(name = "last_name")
    private String lastName;

    /**
     * Дата начала владеления автомобилем
     */
    @Column(name = "ownership_start")
    @Temporal(TemporalType.DATE)
    private Date ownershipStartTime;

    /**
     * Дата окончания владеления автомобилем
     */
    @Column(name = "ownership_end")
    @Temporal(TemporalType.DATE)
    private Date ownershipEndTime;

    /**
     * Пользователь - текущий автовладелец
     */
    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE,
            CascadeType.REFRESH, CascadeType.DETACH})
    @JoinColumn(name = "user_id")
    private User user;

    /**
     * Конструктор - создание нового объекта с определенными значениями.
     * Используется в тестировании
     * @param firstName имя
     * @param ownershipStartTime начало владения
     * @param ownershipEndTime окончание владения
     */
    public Driver(String firstName, Date ownershipStartTime, Date ownershipEndTime) {
        this.firstName = firstName;
        this.ownershipStartTime = ownershipStartTime;
        this.ownershipEndTime = ownershipEndTime;
    }
}
