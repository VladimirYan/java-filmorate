package ru.yandex.practicum.filmorate.storage.film;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicLong;

@Component
public class InMemoryFilmStorage implements FilmStorage {

    private final List<Film> films = new CopyOnWriteArrayList<>();
    private final AtomicLong nextId = new AtomicLong(1);
    private static final LocalDate EARLIEST_RELEASE_DATE = LocalDate.of(1895, 12, 28);

    @Override
    public Film addFilm(Film film) {
        if (isInvalidReleaseDate(film.getReleaseDate())) {
            throw new IllegalArgumentException("Release date cannot be before " + EARLIEST_RELEASE_DATE + ".");
        }

        film.setId(nextId.getAndIncrement());
        films.add(film);
        return film;
    }

    @Override
    public Film updateFilm(Film film) {
        if (isInvalidReleaseDate(film.getReleaseDate())) {
            throw new IllegalArgumentException("Release date cannot be before " + EARLIEST_RELEASE_DATE + ".");
        }

        for (int i = 0; i < films.size(); i++) {
            if (films.get(i).getId().equals(film.getId())) {
                films.set(i, film);
                return film;
            }
        }

        throw new IllegalArgumentException("Film with ID " + film.getId() + " not found");
    }

    @Override
    public List<Film> getAllFilms() {
        return films;
    }

    private boolean isInvalidReleaseDate(LocalDate releaseDate) {
        return releaseDate.isBefore(EARLIEST_RELEASE_DATE);
    }
}

