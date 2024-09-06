package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import jakarta.validation.Valid;
import java.util.Collections;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/users")
@Validated
public class UserController {

    private final UserStorage userStorage;

    public UserController(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    @PostMapping
    public ResponseEntity<?> createUser(@Valid @RequestBody User user) {
        try {
            User createdUser = userStorage.createUser(user);
            log.info("User created: {}", createdUser);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
        } catch (IllegalArgumentException e) {
            return createErrorResponse(e.getMessage());
        }
    }

    @PutMapping
    public ResponseEntity<?> updateUser(@Valid @RequestBody User user) {
        try {
            User updatedUser = userStorage.updateUser(user);
            log.info("User updated: {}", updatedUser);
            return ResponseEntity.ok(updatedUser);
        } catch (IllegalArgumentException e) {
            return createErrorResponse(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping

    public List<User> getAllUsers() {
        return userStorage.getAllUsers();
    }

    private ResponseEntity<?> createErrorResponse(String message) {
        return createErrorResponse(message, HttpStatus.BAD_REQUEST);
    }

    private ResponseEntity<?> createErrorResponse(String message, HttpStatus status) {
        log.error(message);
        return ResponseEntity.status(status).body(Collections.singletonMap("error", message));
    }
}








