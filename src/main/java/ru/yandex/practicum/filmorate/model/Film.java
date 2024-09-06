package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.extern.slf4j.Slf4j;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@Slf4j
public class Film {
    private Long id;

    @NotBlank(message = "Film name cannot be empty.")
    private String name;

    @Size(max = 200, message = "Description length exceeds 200 characters.")
    private String description;

    private LocalDate releaseDate;

    @Positive(message = "Duration must be a positive number.")
    private int duration;

    private Set<Long> likes = new HashSet<>();

    public void validate() {
        LocalDate earliestReleaseDate = LocalDate.of(1895, 12, 28);
        if (releaseDate != null && releaseDate.isBefore(earliestReleaseDate)) {
            log.error("Invalid release date: {}. Must be after {}", releaseDate, earliestReleaseDate);
            throw new ValidationException("Release date cannot be earlier than December 28, 1895.");
        }
    }
}

