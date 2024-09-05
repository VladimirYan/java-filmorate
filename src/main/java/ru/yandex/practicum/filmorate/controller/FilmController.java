package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;

import jakarta.validation.Valid;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@Slf4j
@RestController
@RequestMapping("/films")
@Validated
public class FilmController {

    private final List<Film> films = new ArrayList<>();
    private final AtomicLong nextId = new AtomicLong(1);
    private static final LocalDate EARLIEST_RELEASE_DATE = LocalDate.of(1895, 12, 28);

    /**
     * Adds a new film if it passes validation checks.
     *
     * @param film Film object
     * @return ResponseEntity with film data or error message
     */
    @PostMapping
    public ResponseEntity<?> addFilm(@Valid @RequestBody Film film) {
        if (isInvalidReleaseDate(film.getReleaseDate())) {
            return createErrorResponse("Release date cannot be before " + EARLIEST_RELEASE_DATE + ".");
        }

        film.setId(nextId.getAndIncrement());
        films.add(film);
        log.info("Film added: {}", film);
        return ResponseEntity.status(HttpStatus.CREATED).body(film);
    }

    /**
     * Updates an existing film if it is found and passes validation checks.
     *
     * @param film Film object
     * @return ResponseEntity with updated film data or error message
     */
    @PutMapping
    public ResponseEntity<?> updateFilm(@Valid @RequestBody Film film) {
        if (isInvalidReleaseDate(film.getReleaseDate())) {
            return createErrorResponse("Release date cannot be before " + EARLIEST_RELEASE_DATE + ".");
        }

        for (int i = 0; i < films.size(); i++) {
            if (films.get(i).getId().equals(film.getId())) {
                films.set(i, film);
                log.info("Film updated: {}", film);
                return ResponseEntity.ok(film);
            }
        }

        String errorMessage = "Film with ID " + film.getId() + " not found";
        log.error(errorMessage);
        return createErrorResponse(errorMessage, HttpStatus.NOT_FOUND);
    }

    /**
     * Retrieves a list of all films.
     *
     * @return List of films
     */
    @GetMapping
    public List<Film> getAllFilms() {
        return films;
    }

    private boolean isInvalidReleaseDate(LocalDate releaseDate) {
        return releaseDate.isBefore(EARLIEST_RELEASE_DATE);
    }

    private ResponseEntity<?> createErrorResponse(String message) {
        return createErrorResponse(message, HttpStatus.BAD_REQUEST);
    }

    private ResponseEntity<?> createErrorResponse(String message, HttpStatus status) {
        log.error(message);
        return ResponseEntity.status(status).body(Collections.singletonMap("error", message));
    }
}





