package ru.job4j.cars.service.admin;

import ru.job4j.cars.model.User;

import java.util.List;

public interface AdminUserService {

    List<User> findAllDeletionRequests();

    void delete(User user);
}
