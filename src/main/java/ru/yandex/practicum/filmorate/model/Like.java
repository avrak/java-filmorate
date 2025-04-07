package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class Like {
    @NotBlank(message = "Фильм должен быть указан")
    private Long filmId;
    @NotBlank(message = "Пользователь должен быть указан")
    private Long userId;
}

