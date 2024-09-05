package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;

import jakarta.validation.Valid;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@Slf4j
@RestController
@RequestMapping("/users")
@Validated
public class UserController {

    private final List<User> users = new ArrayList<>();
    private final AtomicLong nextId = new AtomicLong(1);

    /**
     * Creates a new user if it passes validation checks.
     *
     * @param user User object
     * @return ResponseEntity with user data or error message
     */
    @PostMapping
    public ResponseEntity<?> createUser(@Valid @RequestBody User user) {
        user.validate();
        if (isInvalidName(user.getName())) {
            return createErrorResponse();
        }

        user.setId(nextId.getAndIncrement());
        users.add(user);
        log.info("User created: {}", user);
        return ResponseEntity.status(HttpStatus.CREATED).body(user);
    }

    /**
     * Updates an existing user if it is found and passes validation checks.
     *
     * @param user User object
     * @return ResponseEntity with updated user data or error message
     */
    @PutMapping
    public ResponseEntity<?> updateUser(@Valid @RequestBody User user) {
        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).getId().equals(user.getId())) {
                user.validate();
                if (isInvalidName(user.getName())) {
                    return createErrorResponse();
                }
                users.set(i, user);
                log.info("User updated: {}", user);
                return ResponseEntity.ok(user);
            }
        }

        String errorMessage = "User with ID " + user.getId() + " not found";
        log.error(errorMessage);
        return createErrorResponse(errorMessage, HttpStatus.NOT_FOUND);
    }

    /**
     * Retrieves a list of all users.
     *
     * @return List of users
     */
    @GetMapping
    public List<User> getAllUsers() {
        return users;
    }

    private boolean isInvalidName(String name) {
        return name == null || name.isBlank();
    }

    private ResponseEntity<?> createErrorResponse() {
        return createErrorResponse("Name cannot be empty", HttpStatus.BAD_REQUEST);
    }

    private ResponseEntity<?> createErrorResponse(String message, HttpStatus status) {
        log.error(message);
        return ResponseEntity.status(status).body(Collections.singletonMap("error", message));
    }
}





