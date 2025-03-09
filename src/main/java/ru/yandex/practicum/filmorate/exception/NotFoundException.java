package ru.yandex.practicum.filmorate.exception;

import lombok.Getter;

@Getter
public class NotFoundException extends RuntimeException {
    private final String reason;

    public NotFoundException(String reason) {
        super("Ресурс не найден");
        this.reason = reason;
    }
}
