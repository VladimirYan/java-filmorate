package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

@Service
public class UserFinderService {

    private final UserStorage userStorage;

    @Autowired
    public UserFinderService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public User findUser(Long userId) {
        return userStorage.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + userId));
    }
}
