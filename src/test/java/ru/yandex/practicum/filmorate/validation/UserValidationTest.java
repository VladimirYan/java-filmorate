package ru.yandex.practicum.filmorate.validation;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import jakarta.validation.*;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Set;
import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.*;

public class UserValidationTest {

    private Validator validator;

    @BeforeEach
    public void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    public void whenEmailIsInvalid_thenValidationFails() {
        User user = new User();
        user.setEmail("invalid-email");
        user.setLogin("validLogin");
        user.setBirthday(LocalDate.now());

        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().contains("Email must be valid and contain '@'.")));
    }

    @Test
    public void whenLoginIsEmpty_thenValidationFails() {
        User user = new User();
        user.setEmail("user@example.com");
        user.setLogin("");
        user.setBirthday(LocalDate.now());

        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().contains("Login cannot be empty or contain spaces.")));
    }

    @Test
    public void whenLoginContainsSpaces_thenValidationFails() {
        User user = new User();
        user.setEmail("user@example.com");
        user.setLogin("invalid login");
        user.setBirthday(LocalDate.now());

        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().contains("Login cannot contain spaces.")));
    }

    @Test
    public void whenBirthdayIsInFuture_thenValidationFails() {
        User user = new User();
        user.setEmail("user@example.com");
        user.setLogin("validLogin");
        user.setBirthday(LocalDate.now().plusDays(1));

        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().contains("Birthday cannot be in the future.")));
    }

    @Test
    public void whenUserIsValid_thenValidationPasses() {
        User user = new User();
        user.setEmail("user@example.com");
        user.setLogin("validLogin");
        user.setBirthday(LocalDate.now());

        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertTrue(violations.isEmpty());
    }

    @Test
    public void whenNameIsNull_thenNameIsSetToLogin() {
        User user = new User();
        user.setEmail("user@example.com");
        user.setLogin("validLogin");
        user.setBirthday(LocalDate.now());
        user.setName(null);

        user.validate();
        assertEquals("validLogin", user.getName());
    }

    @Test
    public void whenNameIsBlank_thenNameIsSetToLogin() {
        User user = new User();
        user.setEmail("user@example.com");
        user.setLogin("validLogin");
        user.setBirthday(LocalDate.now());
        user.setName("");

        user.validate();
        assertEquals("validLogin", user.getName());
    }
}
