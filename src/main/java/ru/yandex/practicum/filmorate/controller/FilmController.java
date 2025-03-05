package ru.yandex.practicum.filmorate.controller;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/films")
public class FilmController {
    private final Logger log = (Logger) LoggerFactory.getLogger(FilmController.class);

    private long currentMaxId = 0L;

    public static final int EARLIEST_FILM_YEAR = 1895;
    public static final int EARLIEST_FILM_MONTH = 12;
    public static final int EARLIEST_FILM_DAY = 28;
    private final Map<Long, Film> films = new HashMap<>();

    public FilmController() {
        log.setLevel(Level.TRACE);
    }

    @GetMapping
    public Collection<Film> findAll() {
        log.trace("Вывести список фильмов");
        return films.values();
    }

    @PostMapping
    public Film create(@RequestBody Film film) {
        log.info("Сохранить фильм");

        validateFilm(film);

        film.setId(getNextId());

        films.put(film.getId(), film);
        return film;
    }

    @PutMapping
    public Film update(@RequestBody Film newFilm) {
        log.info("Обновить фильм");

        if (newFilm.getId() == null) {
            String idWarning = "Id должен быть указан";
            log.warn(idWarning);
            throw new ValidationException(idWarning);
        }
        if (films.containsKey(newFilm.getId())) {
            Film oldFilm = films.get(newFilm.getId());

            if (!newFilm.getName().isEmpty()) {
                oldFilm.setName(newFilm.getName());
            }
            if (!newFilm.getDescription().isEmpty()) {
                validateFilmDescription(newFilm);
                oldFilm.setDescription(newFilm.getDescription());
            }
            if (newFilm.getReleaseDate() != null) {
                validateFilmReleaseDate(newFilm);
                oldFilm.setReleaseDate(newFilm.getReleaseDate());
            }
            if (newFilm.getDuration() != null) {
                validateFilmDuration(newFilm);
                oldFilm.setDuration(newFilm.getDuration());
            }
            films.put(oldFilm.getId(), oldFilm);
            return oldFilm;
        }
        throw new NotFoundException("Фильм с id = " + newFilm.getId() + " не найден");
    }

    private long getNextId() {
        return ++currentMaxId;
    }

    private void validateFilm(Film film) {
        validateFilmName(film);
        validateFilmDescription(film);
        validateFilmDuration(film);
        validateFilmReleaseDate(film);
    }

    private void validateFilmName(Film film) {
        if (film.getName().isEmpty()) {
            String filmNameWarning = "Название фильма не может быть пустым";
            log.warn(filmNameWarning);
            throw new ValidationException(filmNameWarning);
        }
    }

    private void validateFilmDescription(Film film) {
        if (film.getDescription().length() > 200) {
            String filmDescriptionWarning = "Описание должно быть не более 200 символов";
            log.warn(filmDescriptionWarning);
            throw new ValidationException(filmDescriptionWarning);
        }
    }

    private void validateFilmDuration(Film film) {
        if (film.getDuration() < 0) {
            String filmDurationWarning = "Продолжительность фильма должна быть положительным числом";
            log.warn(filmDurationWarning);
            throw new ValidationException(filmDurationWarning);
        }
    }

    private void validateFilmReleaseDate(Film film) {
        if (film.getReleaseDate().isBefore(LocalDate.of(EARLIEST_FILM_YEAR, EARLIEST_FILM_MONTH, EARLIEST_FILM_DAY))) {
            String filmReleaseDateWarning = "Дата релиза должна быть не раньше 28 декабря 1895 года";
            log.warn(filmReleaseDateWarning);
            throw new ValidationException(filmReleaseDateWarning);
        }
    }

}
