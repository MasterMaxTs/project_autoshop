package ru.job4j.cars.service.adminservice;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.job4j.cars.entity.User;
import ru.job4j.cars.repository.userrepository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class AdminUserServiceImpl implements AdminUserService {

    private final UserRepository store;

    @Override
    public List<User> findAllDeletionRequests() {
        return store.findAll()
                            .stream()
                            .filter(User::isCheck)
                            .collect(Collectors.toList());
    }

    @Override
    public void delete(User user) {
        store.delete(user);
    }
}
