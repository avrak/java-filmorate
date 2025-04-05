package ru.yandex.practicum.filmorate.dal.mappers;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.yandex.practicum.filmorate.dto.*;
import ru.yandex.practicum.filmorate.model.Mpa;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MpaMapper {
    public static Mpa mapToDto(NewMpaRequest request) {
        Mpa rating = new Mpa();

        rating.setId(request.getRatingId());
        rating.setName(request.getCode());

        return rating;
    }

    public static MpaDto mapToRatingDto(Mpa rating) {
        MpaDto dto = new MpaDto();

        dto.setMpaId(rating.getId());
        dto.setName(rating.getName());

        return dto;
    }
}
