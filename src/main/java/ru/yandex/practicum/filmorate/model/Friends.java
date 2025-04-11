package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class Friends {
    @NotBlank(message = "Пользователь №1 должен быть указан")
    private Long user1;
    @NotBlank(message = "Пользователь №2 должен быть указан")
    private Long user2;
}
