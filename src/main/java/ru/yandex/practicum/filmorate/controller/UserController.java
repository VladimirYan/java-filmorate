package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;

import jakarta.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/users")
@Validated
public class UserController {
    private final List<User> users = new ArrayList<>();
    private Long nextId = 1L;

    @PostMapping
    public ResponseEntity<User> createUser(@Valid @RequestBody User user) {
        user.setId(nextId++);
        users.add(user);
        log.info("User created: {}", user);
        return ResponseEntity.ok(user);
    }

    @PutMapping
    public ResponseEntity<User> updateUser(@Valid @RequestBody User user) {
        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).getId().equals(user.getId())) {
                users.set(i, user);
                log.info("User updated: {}", user);
                return ResponseEntity.ok(user);
            }
        }
        log.error("User with ID {} not found", user.getId());
        return ResponseEntity.notFound().build();
    }

    @GetMapping
    public List<User> getAllUsers() {

        return users;
    }
}



