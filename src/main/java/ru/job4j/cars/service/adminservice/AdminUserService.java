package ru.job4j.cars.service.adminservice;

import ru.job4j.cars.entity.User;

import java.util.List;

public interface AdminUserService {

    List<User> findAllDeletionRequests();

    void delete(User user);
}
