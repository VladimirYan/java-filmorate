package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import jakarta.validation.Valid;
import java.util.Collections;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/films")
@Validated
public class FilmController {

    private final FilmService filmService;

    @Autowired
    public FilmController(FilmService filmService) {
        this.filmService = filmService;
    }

    @PostMapping
    public ResponseEntity<?> addFilm(@Valid @RequestBody Film film) {
        try {
            Film addedFilm = filmService.addFilm(film);
            log.info("Film added: {}", addedFilm);
            return ResponseEntity.status(HttpStatus.CREATED).body(addedFilm);
        } catch (IllegalArgumentException e) {
            return createErrorResponse(e.getMessage());
        }
    }

    @PutMapping
    public ResponseEntity<?> updateFilm(@Valid @RequestBody Film film) {
        try {
            Film updatedFilm = filmService.updateFilm(film);
            log.info("Film updated: {}", updatedFilm);
            return ResponseEntity.ok(updatedFilm);
        } catch (IllegalArgumentException e) {
            return createErrorResponse(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping
    public List<Film> getAllFilms() {
        return filmService.getAllFilms();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getFilmById(@PathVariable Long id) {
        try {
            Film film = filmService.getFilmById(id);
            log.info("Film found: {}", film);
            return ResponseEntity.ok(film);
        } catch (IllegalArgumentException e) {
            return createErrorResponse(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/{id}/like/{userId}")
    public ResponseEntity<?> addLike(@PathVariable Long id, @PathVariable Long userId) {
        try {
            filmService.addLike(id, userId);
            log.info("User {} liked film {}", userId, id);
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException e) {
            return createErrorResponse(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}/like/{userId}")
    public ResponseEntity<?> removeLike(@PathVariable Long id, @PathVariable Long userId) {
        try {
            filmService.removeLike(id, userId);
            log.info("User {} removed like from film {}", userId, id);
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException e) {
            return createErrorResponse(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/popular")
    public List<Film> getPopularFilms(@RequestParam(defaultValue = "10") int limit) {
        return filmService.getPopularFilms(limit);
    }

    private ResponseEntity<?> createErrorResponse(String message) {
        return createErrorResponse(message, HttpStatus.BAD_REQUEST);
    }

    private ResponseEntity<?> createErrorResponse(String message, HttpStatus status) {
        log.error(message);
        return ResponseEntity.status(status).body(Collections.singletonMap("error", message));
    }
}









