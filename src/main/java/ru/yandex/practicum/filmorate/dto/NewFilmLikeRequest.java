package ru.yandex.practicum.filmorate.dto;

import lombok.Data;

@Data
public class NewFilmLikeRequest {
    private Long filmId;
    private Integer userId;
}
