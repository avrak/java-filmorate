package ru.yandex.practicum.filmorate.service;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.exception.ParameterNotValidException;
import ru.yandex.practicum.filmorate.storage.InMemoryStorage;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class FilmService implements FilmStorage {
    private long currentMaxId = 0L;

    @Getter
    Map<Long, Film> films;
    Map<Long, User> users;

    @Autowired
    public FilmService(InMemoryStorage inMemoryStorage) {
        this.films = inMemoryStorage.getFilmStorage().getFilms();
        this.users = inMemoryStorage.getUserStorage().getUsers();
    }

    public Film getFilmById(Long filmId) {
        Film film = films.get(filmId);
        if (film == null) throw new NotFoundException("Фильм с id " + filmId + " не найден.");
        return film;
    }

    public Film create(Film film) {
        try {
            validateFilm(film);
        } catch (ParameterNotValidException e) {
            throw e;
        }

        film.setId(getNextId());

        try {
            films.put(film.getId(), film);
        } catch (Exception e) {
            throw new ParameterNotValidException(e.getMessage());
        }

        return film;
    }

    public Film update(Film newFilm) {
        if (newFilm.getId() == null) {
            throw new ParameterNotValidException("Id должен быть указан");
        }
        if (films.containsKey(newFilm.getId())) {
            Film oldFilm = films.get(newFilm.getId());

            if (!newFilm.getName().isEmpty()) {
                oldFilm.setName(newFilm.getName());
            }
            if (!newFilm.getDescription().isEmpty()) {
                try {
                    validateFilmDescription(newFilm);
                } catch (ParameterNotValidException e) {
                    throw e;
                }
                oldFilm.setDescription(newFilm.getDescription());
            }
            if (newFilm.getReleaseDate() != null) {
                try {
                    validateFilmReleaseDate(newFilm);
                } catch (ParameterNotValidException e) {
                    throw e;
                }
                oldFilm.setReleaseDate(newFilm.getReleaseDate());
            }
            if (newFilm.getDuration() != null) {
                try {
                    validateFilmDuration(newFilm);
                } catch (ParameterNotValidException e) {
                    throw e;
                }
                oldFilm.setDuration(newFilm.getDuration());
            }
            films.put(oldFilm.getId(), oldFilm);
            return oldFilm;
        }
            throw new NotFoundException("Фильм с id = " + newFilm.getId() + " не найден");
    }

    public void validateFilm(Film film) {
        validateFilmName(film);
        validateFilmDescription(film);
        validateFilmDuration(film);
        validateFilmReleaseDate(film);
    }

    public void validateFilmName(Film film) {
        if (film.getName().isEmpty()) {
            throw new ParameterNotValidException("Название фильма не может быть пустым");
        }
    }

    public void validateFilmDescription(Film film) {
        if (film.getDescription().length() > 200) {
            throw new ParameterNotValidException("Описание должно быть не более 200 символов");
        }
    }

    public void validateFilmDuration(Film film) {
        if (film.getDuration() < 0) {
            throw new ParameterNotValidException("Продолжительность фильма должна быть положительным числом");
        }
    }

    public void validateFilmReleaseDate(Film film) {
        if (film.getReleaseDate().isBefore(LocalDate.of(InMemoryFilmStorage.EARLIEST_FILM_YEAR
                , InMemoryFilmStorage.EARLIEST_FILM_MONTH
                , InMemoryFilmStorage.EARLIEST_FILM_DAY))) {
            throw new ParameterNotValidException("Дата релиза должна быть не раньше 28 декабря 1895 года");
        }
    }

    public long getNextId() {
        return ++currentMaxId;
    }

    public void setLikeByUserId(Long filmId, Long userId) {
        try {
            Film film = films.get(filmId);

            if (film == null) throw new NotFoundException("Фильм с id = " + filmId + " не найден");

            if (users.get(userId) == null) throw new NotFoundException("Пользователь с id = " + filmId + " не найден");

            film.getLikes().add(userId);
        }
        catch (Exception e) {
            String exp = e.getMessage();
            throw e;
        }
    }

    public void deleteLikeByUserId(Long filmId, Long userId) {
        Film film = films.get(filmId);

        if (film == null) throw new NotFoundException("Фильм с id = " + filmId + " не найден");

        if (users.get(userId) == null) throw new NotFoundException("Пользователь с id = " + filmId + " не найден");

        film.getLikes().remove(userId);
    }

    public Collection<Film> getPopularFilms(Long count) {
        return new ArrayList<>(films.values())
                .stream()
                .sorted((Film f1, Film f2) -> {
                    return -1 * (f1.getLikes().size() - f2.getLikes().size());
                })
                .limit(count)
                .collect(Collectors.toList());
    }
}
