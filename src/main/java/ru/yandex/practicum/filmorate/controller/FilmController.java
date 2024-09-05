package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;

import jakarta.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/films")
@Validated
public class FilmController {
    private final List<Film> films = new ArrayList<>();
    private Long nextId = 1L;

    @PostMapping
    public ResponseEntity<Film> addFilm(@Valid @RequestBody Film film) {
        film.setId(nextId++);
        films.add(film);
        log.info("Film added: {}", film);
        return ResponseEntity.ok(film);
    }

    @PutMapping
    public ResponseEntity<Film> updateFilm(@Valid @RequestBody Film film) {
        for (int i = 0; i < films.size(); i++) {
            if (films.get(i).getId().equals(film.getId())) {
                films.set(i, film);
                log.info("Film updated: {}", film);
                return ResponseEntity.ok(film);
            }
        }
        log.error("Film with ID {} not found", film.getId());
        return ResponseEntity.notFound().build();
    }

    @GetMapping
    public List<Film> getAllFilms() {

        return films;
    }
}



