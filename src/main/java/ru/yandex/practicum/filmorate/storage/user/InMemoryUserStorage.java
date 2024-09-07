package ru.yandex.practicum.filmorate.storage.user;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicLong;
import java.util.Optional;

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
        Optional<User> existingUserOpt = users.stream()
                .filter(u -> u.getId().equals(user.getId()))
                .findFirst();

        User existingUser = existingUserOpt.orElseThrow(() ->
                new IllegalArgumentException("User with ID " + user.getId() + " not found"));

        user.validate();
        if (isInvalidName(user.getName())) {
            throw new IllegalArgumentException("Name cannot be empty");
        }

        int index = users.indexOf(existingUser);
        users.set(index, user);
        return user;
    }

    @Override
    public List<User> getAllUsers() {
        return users;
    }

    @Override
    public Optional<User> findById(Long id) {
        return users.stream()
                .filter(user -> user.getId().equals(id))
                .findFirst();
    }

    private static boolean isInvalidName(String name) {
        return name == null || name.isBlank();
    }
}




