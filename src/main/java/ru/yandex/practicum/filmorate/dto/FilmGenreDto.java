package ru.yandex.practicum.filmorate.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class FilmGenreDto {
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long FilmId;
    private Integer GenreId;
}
