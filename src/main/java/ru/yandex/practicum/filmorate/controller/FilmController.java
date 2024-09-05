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

    @PostMapping
    public ResponseEntity<?> addFilm(@Valid @RequestBody Film film) {
        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            log.error("Film release date {} is before 1895-12-28", film.getReleaseDate());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Collections.singletonMap("error", "Release date cannot be before December 28, 1895."));
        }

        film.setId(nextId.getAndIncrement());
        films.add(film);
        log.info("Film added: {}", film);
        return ResponseEntity.status(HttpStatus.CREATED).body(film);
    }

    @PutMapping
    public ResponseEntity<?> updateFilm(@Valid @RequestBody Film film) {
        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            log.error("Film release date {} is before 1895-12-28", film.getReleaseDate());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Collections.singletonMap("error", "Release date cannot be before December 28, 1895."));
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
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Collections.singletonMap("error", errorMessage));
    }

    @GetMapping
    public List<Film> getAllFilms() {
        return films;
    }
}





