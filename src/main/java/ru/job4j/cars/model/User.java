package ru.job4j.cars.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Set;

/**
 * Модель данных пользователь
 */
@Entity
@Table(name = "auto_users")
@Data
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class User {

    /**
     * Идентификатор пользователя
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    @EqualsAndHashCode.Include
    private int id;

    /**
     * Имя пользователя
     */
    @Column(name = "name")
    private String name;

    /**
     * Сотовый телефон пользователя
     */
    @Column(name = "phone")
    private String phone;

    /**
     * Email пользователя
     */
    @Column(name = "email")
    private String email;

    /**
     * Логин учётной записи пользователя
     */
    @Column(name = "login")
    private String login;

    /**
     * Пароль учётной записи пользователя
     */
    @Column(name = "password")
    private String password;

    /**
     * Локальное время регистрации пользователя на сайте
     */
    @Column(name = "created")
    private LocalDateTime created;

    /**
     * Статус: подана ли заявка администратору сайта
     * для удаления профиля пользователя
     */
    @Column(name = "deletion_request")
    private boolean check;

    /**
     * Локальное время регистрации заявки на сайте
     * для удаления профиля пользователя
     */
    @Column(name = "deletion_request_created")
    private LocalDateTime checkCreated;

    /**
     * Совокупность всех публикаций пользователя
     */
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "user")
    private Set<Post> posts;

    /**
     * Совокупность всех автовладельцев
     */
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "user")
    private Set<Driver> drivers;

    /**
     * Конструктор - создание нового объекта с определенными значениями.
     * Используется в тестировании
     * @param name имя
     * @param phone сотовый телефон
     * @param email email
     * @param login логин учётной записи
     * @param password пароль учётной записи
     */
    public User(String name, String phone, String email, String login, String password) {
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.login = login;
        this.password = password;
    }
}
