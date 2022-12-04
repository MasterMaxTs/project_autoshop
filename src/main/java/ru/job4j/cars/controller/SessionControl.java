package ru.job4j.cars.controller;

import ru.job4j.cars.model.User;

import javax.servlet.http.HttpSession;

/**
 * Доступ к информации о текущем пользователе
 */
public interface SessionControl {

    /**
     * Имя по умолчанию не прошедшего аутентификацию на сайте пользователя
     */
    String GUEST = "Гость";

    /**
     * Доступ к объекту пользователь из активной сессии
     * @param session объект HttpSession
     * @return текущего пользователя
     */
    default User getUserFromSession(HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            user = new User();
            user.setName(GUEST);
        }
        return user;
    }
}
