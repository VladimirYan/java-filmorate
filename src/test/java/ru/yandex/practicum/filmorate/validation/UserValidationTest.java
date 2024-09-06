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
        User user = createUser("invalid-email", "validLogin", LocalDate.now(), "Valid Name");

        Set<ConstraintViolation<User>> violations = validator.validate(user);

        assertEquals(1, violations.size(), "Expected one violation for invalid email");
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().contains("Email must be valid and contain '@'.")),
                "Expected violation message about invalid email");
    }

    @Test
    public void whenLoginIsEmpty_thenValidationFails() {
        User user = createUser("user@example.com", "", LocalDate.now(), "Valid Name");

        Set<ConstraintViolation<User>> violations = validator.validate(user);

        violations.forEach(v -> System.out.println(v.getMessage()));

        assertFalse(violations.isEmpty(), "Expected violations for empty login");
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().contains("Login cannot be empty or contain spaces.")),
                "Expected violation message about empty login");
    }


    @Test
    public void whenLoginContainsSpaces_thenValidationFails() {
        User user = createUser("user@example.com", "invalid login", LocalDate.now(), "Valid Name");

        Set<ConstraintViolation<User>> violations = validator.validate(user);

        assertEquals(1, violations.size(), "Expected one violation for login containing spaces");
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().contains("Login cannot contain spaces.")),

                "Expected violation message about login containing spaces");
    }

    @Test
    public void whenBirthdayIsInFuture_thenValidationFails() {
        User user = createUser("user@example.com", "validLogin", LocalDate.now().plusDays(1), "Valid Name");

        Set<ConstraintViolation<User>> violations = validator.validate(user);

        assertEquals(1, violations.size(), "Expected one violation for future birthday");
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().contains("Birthday cannot be in the future.")),
                "Expected violation message about future birthday");
    }

    @Test
    public void whenUserIsValid_thenValidationPasses() {
        User user = createUser("user@example.com", "validLogin", LocalDate.now(), "Valid Name");

        Set<ConstraintViolation<User>> violations = validator.validate(user);

        assertTrue(violations.isEmpty(), "Expected no violations for valid user");
    }

    @Test
    public void whenNameIsNull_thenNameIsSetToLogin() {
        User user = createUser("user@example.com", "validLogin", LocalDate.now(), null);

        user.validate();
        assertEquals("validLogin", user.getName(), "Expected name to be set to login if name is null");
    }

    @Test
    public void whenNameIsBlank_thenNameIsSetToLogin() {
        User user = createUser("user@example.com", "validLogin", LocalDate.now(), "");

        user.validate();
        assertEquals("validLogin", user.getName(), "Expected name to be set to login if name is blank");
    }

    private User createUser(String email, String login, LocalDate birthday, String name) {
        User user = new User();
        user.setEmail(email);
        user.setLogin(login);
        user.setBirthday(birthday);
        user.setName(name);
        return user;
    }
}

