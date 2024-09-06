package ru.yandex.practicum.filmorate.storage.user;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicLong;

@Component
public class InMemoryUserStorage implements UserStorage {

    private final List<User> users = new CopyOnWriteArrayList<>();
    private final AtomicLong nextId = new AtomicLong(1);

    @Override
    public User createUser(User user) {
        user.validate();
        if (isInvalidName(user.getName())) {
            throw new IllegalArgumentException("Name cannot be empty");
        }

        user.setId(nextId.getAndIncrement());
        users.add(user);
        return user;
    }

    @Override
    public User updateUser(User user) {
        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).getId().equals(user.getId())) {
                user.validate();
                if (isInvalidName(user.getName())) {
                    throw new IllegalArgumentException("Name cannot be empty");
                }

                users.set(i, user);
                return user;
            }
        }

        throw new IllegalArgumentException("User with ID " + user.getId() + " not found");
    }

    @Override
    public List<User> getAllUsers() {
        return users;
    }

    private boolean isInvalidName(String name) {
        return name == null || name.isBlank();
    }
}

