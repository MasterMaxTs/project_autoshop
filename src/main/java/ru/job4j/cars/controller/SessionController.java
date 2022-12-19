package ru.job4j.cars.controller;

import ru.job4j.cars.model.User;

import javax.servlet.http.HttpSession;

/**
 * Абстракция контроллера сессии
 * @see ru.job4j.cars.controller.post.PostController
 * @see ru.job4j.cars.controller.user.UserController
 */
public abstract class SessionController {

    /**
     * Возвращает объект пользователя из активной сессии
     * @param session объект HttpSession
     * @return текущего пользователя
     */
    protected User getUserFromSession(HttpSession session) {
        String guest = "Гость";
        User user = (User) session.getAttribute("user");
        if (user == null) {
            user = new User();
            user.setName(guest);
        }
        return user;
    }
}
