package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

/**
 * Genre.
 */
@Data
@Getter
@Setter
public class FilmGenre {
    @NotBlank(message = "Фильм должен быть указан")
    private Long filmId;
    @NotBlank(message = "Жанр должен быть указан")
    private Integer genreId;
}
