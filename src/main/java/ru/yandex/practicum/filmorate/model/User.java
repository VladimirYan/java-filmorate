package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Pattern;
import java.time.LocalDate;

@Data
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

    public void validate() {
        if (name == null || name.isBlank()) {
            name = login;
        }
    }
}
