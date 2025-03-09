package ru.yandex.practicum.filmorate.storage;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Getter
@Component
public class InMemoryStorage {
    private InMemoryUserStorage userStorage;
    private InMemoryFilmStorage filmStorage;

    @Autowired
    public InMemoryStorage(InMemoryUserStorage userStorage, InMemoryFilmStorage filmStorage) {
        this.userStorage = userStorage;
        this.filmStorage = filmStorage;
    }
}
