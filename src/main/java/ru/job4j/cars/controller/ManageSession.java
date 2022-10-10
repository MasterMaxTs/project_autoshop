package ru.job4j.cars.controller;

import ru.job4j.cars.entity.User;

import javax.servlet.http.HttpSession;

public interface ManageSession {

    String GUEST = "Гость";

    default User getUserFromSession(HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            user = new User();
            user.setName(GUEST);
        }
        return user;
    }
}
