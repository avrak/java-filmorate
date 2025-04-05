package ru.yandex.practicum.filmorate.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class FilmLikeDto {
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long filmId;
    private Long userId;
}
