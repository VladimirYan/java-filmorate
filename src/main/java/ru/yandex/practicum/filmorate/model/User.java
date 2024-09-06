package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Pattern;
import lombok.extern.slf4j.Slf4j;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@Slf4j
public class User {
    private Long id;

    @Email(message = "Email must be valid and contain '@'.")
    private String email;

    @NotBlank(message = "Login cannot be empty or contain spaces.")
    @Pattern(regexp = "^\\S+$", message = "Login cannot contain spaces.")
    private String login;

    private String name;

    @PastOrPresent(message = "Birthday cannot be in the future.")
    private LocalDate birthday;

    private Set<Long> friends = new HashSet<>();

    public void validate() {
        if (name == null || name.isBlank()) {
            name = login;
            log.info("User name is empty or null. Setting login as name: {}", name);
        }
    }
}



