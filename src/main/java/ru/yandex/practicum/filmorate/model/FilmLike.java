package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class FilmLike {
    @NotBlank(message = "Пользователь должен быть указан")
    private Long userId;
}
