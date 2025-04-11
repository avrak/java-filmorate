package ru.yandex.practicum.filmorate.dto;

import lombok.Data;

@Data
public class NewFilmGenreRequest {
    private Long filmId;
    private Integer genreId;
}
