package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class FilmService {

    private final FilmStorage filmStorage;
    private final UserFinderService userFinderService;

    @Autowired
    public FilmService(FilmStorage filmStorage, UserFinderService userFinderService) {
        this.filmStorage = filmStorage;
        this.userFinderService = userFinderService;
    }

    public Film addFilm(Film film) {
        return filmStorage.addFilm(film);
    }

    public Film updateFilm(Film film) {
        return filmStorage.updateFilm(film);
    }

    public List<Film> getAllFilms() {
        return filmStorage.getAllFilms();
    }

    public Film getFilmById(Long filmId) {
        return findFilm(filmId);
    }

    public void addLike(Long filmId, Long userId) {
        Film film = findFilm(filmId);
        userFinderService.findUser(userId);
        film.getLikes().add(userId);
    }

    public void removeLike(Long filmId, Long userId) {
        Film film = findFilm(filmId);
        userFinderService.findUser(userId);
        film.getLikes().remove(userId);
    }
    public List<Film> getPopularFilms(int limit) {
        return filmStorage.getAllFilms().stream()
                .sorted((f1, f2) -> Integer.compare(f2.getLikes().size(), f1.getLikes().size()))
                .limit(limit)
                .collect(Collectors.toList());
    }

    private Film findFilm(Long filmId) {
        return filmStorage.findById(filmId)
                .orElseThrow(() -> new IllegalArgumentException("Film not found: " + filmId));
    }
}






