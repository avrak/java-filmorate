package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDate;
import java.util.Objects;

@Data
@Getter
@Setter
@Validated
public class User {
    private Long id;
    @Email(message = "Некорректный имейл")
    @NotBlank(message = "Имейл должен быть указан")
    private String email;
    @NotBlank(message = "Логин пользователя не может быть пустым")
    @NoSpaces
    private String login;
    private String name;
    @Past
    private LocalDate birthday;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(id, user.getId());

    }
}