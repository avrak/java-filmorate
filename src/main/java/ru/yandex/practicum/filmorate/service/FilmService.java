package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.exception.ParameterNotValidException;
import ru.yandex.practicum.filmorate.storage.InMemoryStorage;

import java.util.Collection;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class FilmService  {

    private final InMemoryStorage inMemoryStorage;

    public Map<Long, Film> getFilms() {
        return inMemoryStorage.getFilmStorage().getAll();
    }

    public Film getFilmById(Long filmId) {
        Film film = inMemoryStorage.getFilmStorage().getFilmById(filmId);
        if (film == null) throw new NotFoundException("Фильм с id " + filmId + " не найден.");
        return film;
    }

    public User getUserById(Long userId) {
        return inMemoryStorage.getUserStorage().getUserById(userId);
    }

    public Film create(Film film) {
        inMemoryStorage.getFilmStorage().create(film);
        return film;
    }

    public Film update(Film newFilm) {
        if (newFilm.getId() == null) {
            throw new ParameterNotValidException("Id должен быть указан");
        }

        if (!getFilms().containsKey(newFilm.getId())) {
            throw new NotFoundException("Фильм с id = " + newFilm.getId() + " не найден");
        }

        Film oldFilm = getFilmById(newFilm.getId());

        if (!newFilm.getName().isEmpty()) {
            oldFilm.setName(newFilm.getName());
        }

        if (!newFilm.getDescription().isEmpty()) {
            oldFilm.setDescription(newFilm.getDescription());
        }

        if (newFilm.getReleaseDate() != null) {
            oldFilm.setReleaseDate(newFilm.getReleaseDate());
        }

        inMemoryStorage.getFilmStorage().update(oldFilm);
        return oldFilm;
    }

    public void setLikeByUserId(Long filmId, Long userId) {
        Film film = getFilmById(filmId);

        if (film == null) throw new NotFoundException("Фильм с id = " + filmId + " не найден");

        if (getUserById(userId) == null) throw new NotFoundException("Пользователь с id = " + filmId + " не найден");

        film.getLikes().add(userId);
    }

    public void deleteLikeByUserId(Long filmId, Long userId) {
        Film film = getFilmById(filmId);

        if (film == null) throw new NotFoundException("Фильм с id = " + filmId + " не найден");

        if (getUserById(userId) == null) throw new NotFoundException("Пользователь с id = " + filmId + " не найден");

        film.getLikes().remove(userId);
    }

    public Collection<Film> getPopularFilms(Long count) {
        return inMemoryStorage.getFilmStorage().getPopularFilms(count);
    }
}
