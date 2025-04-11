package ru.yandex.practicum.filmorate.dal.mappers;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.yandex.practicum.filmorate.dto.*;
import ru.yandex.practicum.filmorate.model.FilmLike;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class FilmLikeMapper {
    public static FilmLike mapToFilmLike(NewFilmLikeRequest request) {
        FilmLike filmLike = new FilmLike();

//        filmLike.setFilmId(request.getFilmId());
        filmLike.setUserId(request.getFilmId());

        return filmLike;
    }

    public static FilmLikeDto mapToFilmLikeDto(FilmLike filmLike) {
        FilmLikeDto dto = new FilmLikeDto();

        dto.setUserId(filmLike.getUserId());
//        dto.setFilmId(filmLike.getFilmId());

        return dto;
    }
}
