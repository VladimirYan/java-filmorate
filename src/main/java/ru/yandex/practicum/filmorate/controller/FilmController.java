package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import jakarta.validation.Valid;
import java.util.Collections;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/films")
@Validated
public class FilmController {

    private final FilmStorage filmStorage;

    public FilmController(FilmStorage filmStorage) {
        this.filmStorage = filmStorage;
    }

    @PostMapping
    public ResponseEntity<?> addFilm(@Valid @RequestBody Film film) {
        try {
            Film addedFilm = filmStorage.addFilm(film);
            log.info("Film added: {}", addedFilm);
            return ResponseEntity.status(HttpStatus.CREATED).body(addedFilm);
        } catch (IllegalArgumentException e) {
            return createErrorResponse(e.getMessage());
        }
    }

    @PutMapping
    public ResponseEntity<?> updateFilm(@Valid @RequestBody Film film) {
        try {
            Film updatedFilm = filmStorage.updateFilm(film);
            log.info("Film updated: {}", updatedFilm);
            return ResponseEntity.ok(updatedFilm);
        } catch (IllegalArgumentException e) {
            return createErrorResponse(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping
    public List<Film> getAllFilms() {
        return filmStorage.getAllFilms();
    }

    private ResponseEntity<?> createErrorResponse(String message) {
        return createErrorResponse(message, HttpStatus.BAD_REQUEST);
    }

    private ResponseEntity<?> createErrorResponse(String message, HttpStatus status) {
        log.error(message);
        return ResponseEntity.status(status).body(Collections.singletonMap("error", message));
    }
}







