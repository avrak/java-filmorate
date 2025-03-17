package ru.yandex.practicum.filmorate.exception;

import lombok.Getter;

@Getter
 public class ParameterNotValidException extends RuntimeException {
    private final String reason;

    public ParameterNotValidException(String reason) {
        super("Ошибка ввода");
        this.reason = reason;
    }
}
