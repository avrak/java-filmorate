package ru.yandex.practicum.filmorate.storage;

import lombok.Getter;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.HashMap;
import java.util.Map;

@Getter
@Component
public class InMemoryFilmStorage {

    public static final int EARLIEST_FILM_YEAR = 1895;
    public static final int EARLIEST_FILM_MONTH = 12;
    public static final int EARLIEST_FILM_DAY = 28;
    private final Map<Long, Film> films;

    public InMemoryFilmStorage() {
        this.films = new HashMap<>();
    }

}
