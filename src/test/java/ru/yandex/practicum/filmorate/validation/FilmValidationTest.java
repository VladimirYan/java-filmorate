package ru.yandex.practicum.filmorate.validation;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;

import jakarta.validation.*;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.Set;
import static org.junit.jupiter.api.Assertions.*;

public class FilmValidationTest {

    private Validator validator;

    @BeforeEach
    public void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    public void whenNameIsEmpty_thenValidationFails() {
        Film film = new Film();
        film.setName("");
        film.setDescription("Description");
        film.setReleaseDate(LocalDate.of(2000, 1, 1));
        film.setDuration(90);

        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().contains("Film name cannot be empty.")));
    }

    @Test
    public void whenDescriptionIsTooLong_thenValidationFails() {
        Film film = new Film();
        film.setName("Valid Name");
        film.setDescription("This is a very long description that exceeds two hundred characters. " +
                "Lorem ipsum dolor sit amet, consectetur adipiscing elit. " +
                "Pellentesque auctor dapibus velit, non blandit metus ultricies sit amet. " +
                "Integer in arcu id justo rhoncus mollis.");
        film.setReleaseDate(LocalDate.of(2000, 1, 1));
        film.setDuration(90);

        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().contains("Description length exceeds 200 characters.")));
    }

    @Test
    public void whenDurationIsNotPositive_thenValidationFails() {
        Film film = new Film();
        film.setName("Valid Name");
        film.setDescription("Valid Description");
        film.setReleaseDate(LocalDate.of(2000, 1, 1));
        film.setDuration(0);

        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().contains("Duration must be a positive number.")));
    }

    @Test
    public void whenReleaseDateIsTooEarly_thenValidationExceptionIsThrown() {
        Film film = new Film();
        film.setName("Valid Name");
        film.setDescription("Valid Description");
        film.setReleaseDate(LocalDate.of(1800, 1, 1));
        film.setDuration(90);

        ValidationException exception = assertThrows(ValidationException.class, film::validate);
        assertTrue(exception.getMessage().contains("Release date cannot be earlier than December 28, 1895."));
    }

    @Test
    public void whenFilmIsValid_thenValidationPasses() {
        Film film = new Film();
        film.setName("Valid Name");
        film.setDescription("Valid Description");
        film.setReleaseDate(LocalDate.of(2000, 1, 1));
        film.setDuration(90);

        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertTrue(violations.isEmpty());
    }
}

