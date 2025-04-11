package ru.yandex.practicum.filmorate.model;


import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class Mpa {
    private Integer id;
    @NotBlank(message = "Рейтинг должен быть указан")
    private String name;
}
