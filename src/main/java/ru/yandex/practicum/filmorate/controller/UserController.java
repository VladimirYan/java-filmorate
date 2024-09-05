package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;

import jakarta.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@Slf4j
@RestController
@RequestMapping("/users")
@Validated
public class UserController {
    private final List<User> users = new ArrayList<>();
    private final AtomicLong nextId = new AtomicLong(1);

    @PostMapping
    public ResponseEntity<?> createUser(@Valid @RequestBody User user) {
        if (user.getName() == null || user.getName().isEmpty()) {
            log.error("User name cannot be empty");
            return ResponseEntity.badRequest().body("Name cannot be empty");
        }

        user.setId(nextId.getAndIncrement());
        users.add(user);
        log.info("User created: {}", user);
        return ResponseEntity.ok(user);
    }

    @PutMapping
    public ResponseEntity<?> updateUser(@Valid @RequestBody User user) {
        if (user.getName() == null || user.getName().isEmpty()) {
            log.error("User name cannot be empty");
            return ResponseEntity.badRequest().body("Name cannot be empty");
        }

        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).getId().equals(user.getId())) {
                users.set(i, user);
                log.info("User updated: {}", user);
                return ResponseEntity.ok(user);
            }
        }

        String errorMessage = "User with ID " + user.getId() + " not found";
        log.error(errorMessage);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorMessage);
    }

    @GetMapping
    public List<User> getAllUsers() {
        return users;
    }
}




