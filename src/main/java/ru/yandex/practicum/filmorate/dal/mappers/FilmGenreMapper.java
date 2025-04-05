package ru.yandex.practicum.filmorate.dal.mappers;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.yandex.practicum.filmorate.dto.*;
import ru.yandex.practicum.filmorate.model.FilmGenre;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class FilmGenreMapper {
    public static FilmGenre mapToFilmGenre(NewFilmGenreRequest request) {
        FilmGenre filmGenre = new FilmGenre();

        filmGenre.setGenreId(request.getGenreId());
        filmGenre.setFilmId(request.getFilmId());

        return filmGenre;
    }

    public static FilmGenreDto mapToFilmGenreDto(FilmGenre filmGenre) {
        FilmGenreDto dto = new FilmGenreDto();

        dto.setGenreId(filmGenre.getGenreId());
        dto.setFilmId(filmGenre.getFilmId());

        return dto;
    }
}
