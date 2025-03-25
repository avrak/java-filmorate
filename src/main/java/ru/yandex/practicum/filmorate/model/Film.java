package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

/**
 * Film.
 */
@Data
@Getter
@Setter
public class Film {
    private Long id;
    @NotBlank(message = "Название фильма не может быть пустым")
    private String name;
    @Size(min = 1, max = 200, message = "Описание должно быть не более 200 символов")
    private String description;
    @NotEarliest()
    private LocalDate releaseDate;
    @Positive(message = "Продолжительность фильма должна быть положительным числом")
    private Integer duration;
    private Set<Long> likes = new HashSet<>();
}
