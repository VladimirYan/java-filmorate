package ru.yandex.practicum.filmorate.storage.film;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

@Component
public class InMemoryFilmStorage implements FilmStorage {

    private final List<Film> films = new CopyOnWriteArrayList<>();
    private final Map<Long, Film> filmMap = new ConcurrentHashMap<>();
    private final AtomicLong nextId = new AtomicLong(1);
    private static final LocalDate EARLIEST_RELEASE_DATE = LocalDate.of(1895, 12, 28);

    @Override
    public Film addFilm(Film film) {
        if (isInvalidReleaseDate(film.getReleaseDate())) {
            throw new IllegalArgumentException("Release date cannot be before " + EARLIEST_RELEASE_DATE + ".");
        }
        film.setId(nextId.getAndIncrement());
        films.add(film);
        filmMap.put(film.getId(), film);
        return film;
    }

    @Override
    public Film updateFilm(Film film) {
        if (isInvalidReleaseDate(film.getReleaseDate())) {
            throw new IllegalArgumentException("Release date cannot be before " + EARLIEST_RELEASE_DATE + ".");
        }

        if (!filmMap.containsKey(film.getId())) {
            throw new IllegalArgumentException("Film with ID " + film.getId() + " not found");
        }

        int index = films.indexOf(filmMap.get(film.getId()));
        films.set(index, film);
        filmMap.put(film.getId(), film);
        return film;
    }

    @Override
    public List<Film> getAllFilms() {
        return films;
    }

    @Override
    public Optional<Film> findById(Long id) {
        return Optional.ofNullable(filmMap.get(id));
    }

    private static boolean isInvalidReleaseDate(LocalDate releaseDate) {
        return releaseDate.isBefore(EARLIEST_RELEASE_DATE);
    }
}



