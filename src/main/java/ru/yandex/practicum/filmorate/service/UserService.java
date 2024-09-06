package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserStorage userStorage;
    private final UserFinderService userFinderService;

    @Autowired
    public UserService(UserStorage userStorage, UserFinderService userFinderService) {
        this.userStorage = userStorage;
        this.userFinderService = userFinderService;
    }

    public User createUser(User user) {
        return userStorage.createUser(user);
    }

    public User updateUser(User user) {
        return userStorage.updateUser(user);
    }

    public List<User> getAllUsers() {
        return userStorage.getAllUsers();
    }

    public User getUserById(Long id) {
        return userFinderService.findUser(id);
    }

    public List<User> getFriends(Long userId) {
        User user = userFinderService.findUser(userId);
        return user.getFriends().stream()
                .map(userFinderService::findUser)
                .collect(Collectors.toList());
    }

    public void addFriend(Long userId, Long friendId) {
        User user = userFinderService.findUser(userId);
        User friend = userFinderService.findUser(friendId);
        user.getFriends().add(friendId);
        friend.getFriends().add(userId);
    }

    public void removeFriend(Long userId, Long friendId) {
        User user = userFinderService.findUser(userId);
        User friend = userFinderService.findUser(friendId);
        user.getFriends().remove(friendId);
        friend.getFriends().remove(userId);
    }

    public List<User> getMutualFriends(Long userId, Long otherUserId) {
        User user = userFinderService.findUser(userId);
        User otherUser = userFinderService.findUser(otherUserId);
        return user.getFriends().stream()
                .filter(otherUser.getFriends()::contains)
                .map(userFinderService::findUser)
                .collect(Collectors.toList());
    }
}







